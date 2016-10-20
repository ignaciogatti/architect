package architect.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import architect.engine.architecture.ArchitectureResults;
import architect.engine.architecture.DesignBotResults;
import architect.engine.architecture.TacticChange;
import architect.engine.architecture.algorithms.TrackAlgorithm;
import architect.engine.negotiation.utility.IUtilityFunction;
import architect.engine.negotiation.zeuthen.IZeuthenFunction;
import architect.model.Architecture;
import architect.model.ArchitectureAnalysis;
import architect.model.NegotiationChoice;
import architect.model.Scenario;
import architect.model.Tactic;

/**
 * 
 * Esta clase facilita la creacion de un thread asociado a un DBot para utilizar en caso de negociacion.
 * Delega parte de su funcionalidad al DBotExecutor asociado y agrega comportamiento adicional para soportar el proceso de negociacion (decorator).
 *
 */
public class DBNegotiationExecutor extends DesignBotExecutor {
	
	private final static double INFINITE_VALUE = 999999e9;
	
	private DesignBotExecutor dbot;
	
	// lista de propuestas candidatas
	private List<NegotiationChoice> results;
	
	private List<NegotiationChoice> choices;

	private int indexCandidate = 0;
	
	// valor umbral: porcentaje que expresa el punto minimo de utilidad hasta el cual un agente puede conceder
	private double threshold;
	
	// costo de mejor y peor candidatos
	private Double minValue;
	private Double maxValue;
	
	private boolean waiting = false;
	
	private IUtilityFunction functionUtility;
	
	private static IZeuthenFunction zeuthen;
	
	private static Logger log = Logger.getLogger("negotiation");
	
	public DBNegotiationExecutor(DesignBotExecutor dbot,IUtilityFunction functionUtility) {
		super();
		this.dbot = dbot;
		this.results = new ArrayList<NegotiationChoice>();
		this.threshold = 0;
		this.minValue = null;
		this.maxValue = null;
		this.functionUtility = functionUtility;
	}
	
	public DBNegotiationExecutor(ArchitectureAnalysis architectureAnalysis, DesignBotExecutor dbot, IUtilityFunction functionUtility) {
		super(architectureAnalysis);
		this.dbot = dbot;
		this.results = new ArrayList<NegotiationChoice>();
		this.threshold = 0;
		this.minValue = null;
		this.maxValue = null;
		this.functionUtility = functionUtility;
	}
		
	public List<NegotiationChoice> getResults() {
		return results;
	}
	
	public void setListChoices(List<NegotiationChoice> choices){
		this.choices = choices;
	}

	public void setThreshold(double value){
		this.threshold = value;
	}
	
	public Double getScenarioCost(Scenario scenario){
		return dbot.getScenarioCost(scenario);
	}
	
	public Double getUtility(Scenario scenario){
		return this.getUtility(dbot.getScenarioCost(scenario));
	}
	
	
	public void setArchitectureAnalysis(ArchitectureAnalysis architectureAnalysis){
		super.setArchitectureAnalysis(architectureAnalysis);
		dbot.setArchitectureAnalysis(architectureAnalysis);
	}
	
	protected Architecture selectBestArchitecture(
			Architecture bestArchi, Architecture backArchi, Scenario scenario,
			List<TacticChange> bestChanges, List<TacticChange> localChanges){
		return dbot.selectBestArchitecture(bestArchi, backArchi, scenario, bestChanges, localChanges);
	}
	
	
	@Override
	@Transactional
	public void run() {
		this.esperar();
		
		// 	1 - tomar arquitectura inicial
		Scenario scenario = this.dbot.getArchitectureAnalysis().getScenario();
		Architecture archi = scenario.getArchitecture();
		
		List<TacticChange> changes = new ArrayList<TacticChange>();
		
		List<Tactic> tactics = new ArrayList<Tactic>(architectureAnalysis.getDesignBot().getTactics());
		int index = 0;

		//  2 - calcular alternativa candidata para cada tactica disponible y almacenar candidatos resultantes en arreglo comunitario. Repetir proceso con cada arquitectura resultante
		while (!stopped){
			for (Tactic tactic : tactics){
				NegotiationChoice choice = searchChoice(this.trackAlgorithm, tactic, scenario, archi, changes);
				if (choice != null){
					this.choices.add(choice);
				}
			}
			NegotiationChoice choice = this.choices.get(index);
			// System.out.println("Dbot: "+this.getArchitectureAnalysis().getDesignBot().getId()+" -- index: "+index+" -- choices: "+this.choices.size());
			index++;
			archi = choice.getArchitecture();
			List<Scenario> scenariosArchiActual = new ArrayList<Scenario>(archi.getScenarios());
			scenario = scenariosArchiActual.get(scenariosArchiActual.indexOf(scenario));
			changes = choice.getChanges();
			esperar();
		}
		
		List<NegotiationChoice> listChoices = new ArrayList<NegotiationChoice>();
		List<Scenario> archiScenarios = null;
		
		//  3 - ordenar arreglo comunitario en base a prioridades (utilidad). Analizar si se muestra en pantalla el mejor resultado.
		for (NegotiationChoice choice : this.choices){
			NegotiationChoice newChoice = new NegotiationChoice();
			newChoice.setArchitecture(choice.getArchitecture());
			newChoice.setChanges(choice.getChanges());
			archiScenarios = new ArrayList<Scenario>(newChoice.getArchitecture().getScenarios());
			newChoice.setCost(getUtility(archiScenarios.get(archiScenarios.indexOf(this.architectureAnalysis.getScenario()))));
			listChoices.add(newChoice);
		}
		this.results = listChoices;

		Collections.sort(this.results, new Comparator<NegotiationChoice>() {
			@Override
			public int compare(NegotiationChoice o1, NegotiationChoice o2) {
				return o1.getCost().compareTo(o2.getCost())*(-1);
			}
		});
		
		// 4 - se encola el mejor resultado (para visualizar)
		if (architectureAnalysisResults!=null) {
			ArchitectureResults architectureResults = new ArchitectureResults(this.results.get(0).getArchitecture(),architectureAnalysisResults.getActualArchitectureResult());
			DesignBotResults designBotResults = new DesignBotResults();
			designBotResults.setArchitectureAnalysis(architectureAnalysis);
			designBotResults.setChanges(this.results.get(0).getChanges());
			designBotResults.setArchitecture(this.results.get(0).getArchitecture());
			designBotResults.setArchitectureResults(architectureResults);
			architectureAnalysisResults.addDesignBotResult(designBotResults);	
		}
		
		// valores maximos y minimos de las propuestas obtenidas (considera tambien el valor actual)
		Double actualCost = this.architectureAnalysisResults.getActualArchitectureResult().getScenarioCost(this.architectureAnalysis.getScenario());
		this.maxValue = Math.max(this.results.get(0).getCost(), this.getUtility(actualCost));
		this.minValue = Math.min(this.results.get(results.size()-1).getCost(), this.getUtility(actualCost));
		log.info(this.getName());
		log.info("- MinValue: " + this.minValue);
		log.info("- MaxValue: " + this.maxValue);
	}
	
	
	public synchronized void esperar(){
		try {
			this.setWaiting(true);
			wait();
		} catch (InterruptedException e) {
			log.error("ocurrio un error al intentar ejecutar sentencia wait()");
		}
	}
	
	
	public synchronized void notificar(){
		this.setWaiting(false);
		notify();
		
	}

	/**
	 * Este metodo permite efectuar una busqueda de alternativas de un DBot para aplicar en una negociacion
	 * 
	 * @param trackAlgorithm algoritmo de busqueda
	 * @param tacticsList lista de posibles tacticas aplicables (vinculadas al DBot)
	 * @param scenario escenario a aplicar el conjunto de tacticas
	 * @param changes lista de cambios de la mejor solucion
	 * @return lista de alternativas para un DBot
	 */
	private NegotiationChoice searchChoice(TrackAlgorithm trackAlgorithm, Tactic tactic, Scenario scenario, Architecture architecture,
			List<TacticChange> changes) {

		TacticExecutor tacticExecutor = tactic.getTacticExecutor();		
		
		TacticChange tacticChange = new TacticChange();
		tacticChange.setTacticName(tactic.getName());
			
		// ejecucion del algoritmo de busqueda de la mejor arquitectura de acuerdo a la tactica seleccionada
		// como asi tambien un conjunto de soluciones para incluir en la lista de posibles candidatos
		Architecture newArchi = trackAlgorithm.searchAndApply(tacticExecutor, scenario, tacticChange, null);
		
		changes.add(tacticChange);
		if (newArchi != null){
			NegotiationChoice choice = new NegotiationChoice();
			choice.setArchitecture(newArchi);
			choice.setChanges(changes);
			return choice;
		}
		else{
			return null;
		}
	}


	public double getThreshold() {
		return threshold;
	}


	public Double getMinValue() {
		return minValue;
	}


	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}


	public Double getMaxValue() {
		return maxValue;
	}


	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}
	
	
	/**
	 * Metodo utilizado para realizar una concesion. <br><br>
	 * En este caso solo se retorna el siguiente candidato de la lista (egocentric concession).
	 * Si se desea extender el comportamiento para cambiar la estrategia de concesion, se debera modificar este metodo.
	 * @return proximo candidato (null si no se puede conceder)
	 */
	public NegotiationChoice getNextCandidate(){
		NegotiationChoice choice = null;
		if ((this.results != null) && (results.size() > this.indexCandidate)){
			NegotiationChoice candidate = this.results.get(this.indexCandidate);
			if (isGoodChoice(candidate)){
				choice = candidate;
				this.indexCandidate++;
			}
		}
		return choice;	
	}
	
	
	public NegotiationChoice getActualCandidate(){
		if (this.results != null){
			if (this.indexCandidate > 0)
				return this.results.get(this.indexCandidate-1);
			else
				return this.results.get(this.indexCandidate);
		}
		return null;
	}
	
	
	/**
	 * Evalua que una propuesta de concesion sea conveniente. 
	 * Es decir, que este dentro de los valores maximos y minimos aceptables (considerando tambien el valor umbral seteado).
	 * @param choice alternativa a evaluar
	 * @return evaluacion de propuesta
	 */
	private boolean isGoodChoice(NegotiationChoice choice){
		List<Scenario> archiScenarios = new ArrayList<Scenario>(choice.getArchitecture().getScenarios());
		Double utility = getUtility(archiScenarios.get(archiScenarios.indexOf(this.architectureAnalysis.getScenario())));
	
		if (utility >= this.maxValue)
			return true;
		if (utility <= this.minValue)
			return false;
		
		return (utility >= ((this.maxValue - this.minValue) * this.threshold + this.minValue));
	}
	
	
	/**
	 * Evalua si una propuesta dada por otro agente es conveniente o no (comparando la utilidad de la misma con su propuesta actual).
	 * @param choice propuesta de un agente
	 * @return evaluacion de propuesta
	 */
	public boolean isBetterThanActual(NegotiationChoice choice){
		List<Scenario> archiScenarios = new ArrayList<Scenario>(choice.getArchitecture().getScenarios());
		Double choiceUtility = getUtility(archiScenarios.get(archiScenarios.indexOf(this.architectureAnalysis.getScenario())));
		archiScenarios = new ArrayList<Scenario>(getActualCandidate().getArchitecture().getScenarios());
		Double actualUtility = getUtility(archiScenarios.get(archiScenarios.indexOf(this.architectureAnalysis.getScenario())));
		return choiceUtility >= actualUtility;
	}
	
	
	/**
	 * Metodo utilizado para determinar el valor de zeuthen
	 * @param agents lista de agentes involucrados en la negociacion
	 * @return valor de zeuthen
	 */
	public double getZeuthenValue(List<DesignBotExecutor> agents){
		// si no es posible conceder, se retorna un valor de zeuthen infinito (esto exime al agente de la concesion)
		if ((getNextCandidate() != null)){
			this.indexCandidate--;
			return zeuthen.getZeuthenValue(this,agents);
		}
		else
			return INFINITE_VALUE;
	}
	
	
	public int getCountCandidates(){
		if (this.results == null)
			return 0;
		else
			return this.results.size();
	}
	
	
	/**
	 * Retorna el valor de utilidad asociada a un costo dado. Delega el calculo a la funcion asociada
	 * @param cost costo de una alternativa
	 * @return utilidad
	 */
	public double getUtility(double cost){
		double utility = this.functionUtility.getUtility(cost);
		if (utility < 0){
			utility = 0;
		}
		return utility;
	}


	public static IZeuthenFunction getZeuthenFunction() {
		return zeuthen;
	}


	public static void setZeuthenFunction(IZeuthenFunction zeuthen) {
		DBNegotiationExecutor.zeuthen = zeuthen;
	}
	
	/**
	 * Metodo agregado para obtener todas las opciones de negociación y loguerlas desde el NegotiationMediator
	 * @return
	 */
	public List<NegotiationChoice> getNegotiationChoices() {
		return results;
	}

	public synchronized boolean isWaiting() {
		return waiting;
	}

	public synchronized void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}
	

	
}
