package architect.engine.negotiation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import architect.engine.DBNegotiationExecutor;
import architect.engine.DesignBotExecutor;
import architect.engine.architecture.ArchitectureResults;
import architect.engine.architecture.DesignBotResults;
import architect.engine.architecture.ScenarioResult;
import architect.engine.negotiation.zeuthen.IZeuthenFunction;
import architect.engine.negotiation.zeuthen.WillignessRiskConflictFunction;
import architect.model.ArchitectureAnalysis;
import architect.model.NegotiationChoice;


/**
 * 
 * Esta clase representa al mediador del proceso de negociacion.<br>
 * Entre sus responsabilidades se encuentran la determinacion del agente que debe conceder, lanzamiento de cada ronda y finalizacion del proceso.<br>
 *
 */
public class NegotiationMediator{
	
	private static final long NEGOTIATION_ID = 9999;
	private static final double INFINITE_VALUE = 999999e9;
	
	private static Logger log = Logger.getLogger("negotiation");
	
	private List<DesignBotExecutor> runningDBots;
	private NegotiationChoice result;
	private DBNegotiationExecutor agent;
	
	
	public NegotiationMediator(List<DesignBotExecutor> runningDBots, IZeuthenFunction negotiationStrategy){
		// seteo de funcion a utilizar para el calculo del valor de zeuthen en el proceso de negociacion.
		DBNegotiationExecutor.setZeuthenFunction(negotiationStrategy);
		this.runningDBots = runningDBots;
		this.result = null;
		this.agent = null;
	}
	
	
	/**
	 * Ejecuta el proceso de negociacion
	 */
	public void initNegotiation(){
		
		if ((runningDBots != null)){
			// primera ronda de negociacion (todos los agentes proponen)
			boolean negotiation = firstRound();
			boolean conflict = false;
			NegotiationChoice proposal = null;
			int roundNumber = 1;
			
			// mientras no haya acuerdo ni conflicto (continuacion de la negociacion), se ejecuta una nueva ronda
			while ((!negotiation) && (!conflict)){
				roundNumber++;
				log.info("** ROUND " + roundNumber + " **");
				
				// seleccion de agente para realizar concesion
				DBNegotiationExecutor agent = selectAgentForConcession();
				
				if (agent == null){ // conflicto
					log.info("NO PROPOSALS");
					conflict = true;
				}
				else{
					this.agent = agent;
					// concesion: nueva propuesta candidata
					proposal = agent.getNextCandidate();
					log.info("\nAnalysis-" + this.agent.getArchitectureAnalysis().getId() + " proposal:" );
					List<ScenarioResult> scenarioResults = new ArchitectureResults(proposal.getArchitecture(),this.agent.getAnalysisResults().getActualArchitectureResult()).getScenarioResults();
					for (ScenarioResult scenarioResult : scenarioResults){
						log.info(scenarioResult.getScenario().getName() + " - " + scenarioResult.getScenarioCost());
					}
					this.result = proposal;
					
					// evaluacion de propuesta
					negotiation = evaluateProposal(proposal);
					if (!negotiation)
						log.info("\nNO DEAL\n");
				}
			}
			if (negotiation){
				log.info("### DEAL ###");
			}
			else
				log.info("### CONFLICT ###");
			
			// se muestran los resultados
			if (this.result != null){
				showResults();
			}
		}
	}
	
	
	/**
	 * Primera ronda de negociacion. En este punto, todos los agentes proponen y se evaluan dichas propuestas.
	 * @return resultado de la ronda de negociacion
	 */
	private boolean firstRound(){
		//logNegotiationChoices();
		log.info("** ROUND 1 **");
		boolean negotiation = false;
		int index = 0;
		NegotiationChoice proposal = null;
		while ((!negotiation) && (index < runningDBots.size())) {
			proposal = ((DBNegotiationExecutor) runningDBots.get(index)).getNextCandidate();
			if (proposal != null){
				this.agent = (DBNegotiationExecutor) runningDBots.get(index);
				log.info("\nAnalysis-" + this.agent.getArchitectureAnalysis().getId() + " proposal:" );
				List<ScenarioResult> scenarioResults = new ArchitectureResults(proposal.getArchitecture(),this.agent.getAnalysisResults().getActualArchitectureResult()).getScenarioResults();
				for (ScenarioResult scenarioResult : scenarioResults){
					log.info(scenarioResult.getScenario().getName() + " - " + scenarioResult.getScenarioCost());
				}
				this.result = proposal;
				negotiation = evaluateProposal(proposal);
			}
			index++;
		}
		if (!negotiation)
			log.info("\nNO DEAL\n");
		return negotiation;
	}
	
	
	/**
	 * Evaluacion conjunta de una propuesta.
	 * @param proposal propuesta a evaluar
	 * @return evaluacion
	 */
	private boolean evaluateProposal(NegotiationChoice proposal){
		boolean eval = true;
		for (DesignBotExecutor agent : runningDBots){
			if (!(((DBNegotiationExecutor) agent).isBetterThanActual(proposal))){
				eval = false;
			}
		}
		return eval;
	}
	
	
	/**
	 * Este metodo permite seleccionar un agente para realizar una concesion, en base al calculo del valor de zeuthen para cada agente
	 * @return agente con valor de zeuthen minimo
	 */
	DBNegotiationExecutor selectAgentForConcession(){
		Map<Long,NegotiationChoice> candidates = new HashMap<Long,NegotiationChoice>();
		for (DesignBotExecutor agent : runningDBots){
			candidates.put(agent.getId(),((DBNegotiationExecutor)agent).getActualCandidate());
		}
		double min = INFINITE_VALUE;
		DBNegotiationExecutor selected = null;
		for (DesignBotExecutor agent : runningDBots){
			double value = ((DBNegotiationExecutor) agent).getZeuthenValue(runningDBots);
			log.info("\nAnalysis-" + agent.getArchitectureAnalysis().getId() + " Zeuthen Value: " + value );
			if (value < min){
				min = value;
				selected = (DBNegotiationExecutor) agent;
			}
		}
		
		return selected;
	}

	
	/**
	 * Metodo utilizado para encolar los resultados del proceso de negociacion para ser mostrados
	 */
	public void showResults(){
		if (this.agent.getAnalysisResults()!=null) {
			ArchitectureResults architectureResults = new ArchitectureResults(this.result.getArchitecture(),this.agent.getAnalysisResults().getActualArchitectureResult());
			
			DesignBotResults designBotResults = new DesignBotResults();
			ArchitectureAnalysis a = this.agent.getArchitectureAnalysis().clone();
			a.setId(new Long(NEGOTIATION_ID));
			designBotResults.setArchitectureAnalysis(a);
			designBotResults.setChanges(this.result.getChanges());
			designBotResults.setArchitecture(this.result.getArchitecture());
			designBotResults.setArchitectureResults(architectureResults);
		
			this.agent.getAnalysisResults().addDesignBotResult(designBotResults);	
		}
		
	}
	
	/**
	 * Guarda en el log todas las alternativas de negociación (propuestas) de cada agente 
	 */
	private void logNegotiationChoices() {
		log.info("#### Negotiation choices ###");
		for(DesignBotExecutor bot: runningDBots) {
		//while ((!negotiation) && (index < runningDBots.size())) {
			DBNegotiationExecutor agent = (DBNegotiationExecutor)bot;
			log.info("\nAnalysis-" + agent.getArchitectureAnalysis().getId() + ":" );
			
			List<NegotiationChoice> choices = agent.getNegotiationChoices();
			int i = 1;
			for(NegotiationChoice choice: choices) {
				//proposal = ((DBNegotiationExecutor) runningDBots.get(index)).getNextCandidate();
				log.info(" - Negotiation choice #" + i);
				if (choice != null) {
					List<ScenarioResult> scenarioResults = new ArchitectureResults(choice.getArchitecture(), agent.getAnalysisResults().getActualArchitectureResult()).getScenarioResults();
					for (ScenarioResult scenarioResult : scenarioResults){
						log.info("  " + scenarioResult.getScenario().getName() + ", " + scenarioResult.getScenarioCost() + ", " + agent.getUtility(scenarioResult.getScenarioCost()));
					}
				}
				i++;
			}	
		}
		log.info("#### END negotiation choices ###");
	}
}
