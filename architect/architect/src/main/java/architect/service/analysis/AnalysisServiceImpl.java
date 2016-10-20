package architect.service.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import architect.engine.DBNegotiationExecutor;
import architect.engine.DesignBotExecutor;
import architect.engine.architecture.AnalysisResults;
import architect.engine.architecture.algorithms.TrackAlgorithm;
import architect.engine.negotiation.NegotiationFeaturesFactory;
import architect.engine.negotiation.NegotiationMediator;
import architect.engine.negotiation.utility.IUtilityFunction;
import architect.engine.negotiation.zeuthen.IZeuthenFunction;
import architect.model.ArchitectureAnalysis;
import architect.model.NegotiationChoice;
import architect.service.ArchitectureAnalysisService;

@Service("analysisService")
public class AnalysisServiceImpl implements AnalysisService {

	@Autowired
    private ArchitectureAnalysisService architectureAnalysisService;
	
	private static List<DesignBotExecutor> runningDesignBots = new ArrayList<DesignBotExecutor>();
	private static HashMap<Long,AnalysisResults> analysisResults = new HashMap<Long, AnalysisResults>();
	
	// mediador. Gestiona el proceso de negociacion de agentes
	private static NegotiationMediator mediator = null;
	
	private static Logger log = Logger.getLogger("negotiation");
	
	private Properties properties = null;
	
	private static int DEFAULT_LEVEL_SEARCH = 3;
	
	
	@Override
	public String startFullAnalysis(Long architectureId, TrackAlgorithm trackAlgorithm, Boolean negotiation) {
		
		List<ArchitectureAnalysis> architectureAnalysis = architectureAnalysisService.listArchitectureAnalysisByArchitectureId(architectureId);
		this.deleteAllAnalysis();
		
		// carga de archivo de propiedades para negociacion
		properties = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("negotiationFeatures.properties");
		if (inputStream != null) {
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				log.error("no se pudo cargar el archivo de configuracion de propiedades");
			}
		} else {
			log.error("no se pudo cargar el archivo de configuracion de propiedades");
		}
		
		List<NegotiationChoice> choices = Collections.synchronizedList(new ArrayList<NegotiationChoice>());
		
		// SI (Negotiation == FALSE)
		// ejecutar el algoritmo de busqueda individual por cada dBot
		// SI NO
		// crear instancias de dBots para negociacion
		for (ArchitectureAnalysis architectureAnalysisAux : architectureAnalysis) {
			ArchitectureAnalysis architectureAnalysis1 = architectureAnalysisService.getArchitectureAnalysisEAGER(architectureAnalysisAux.getId());
			if (architectureAnalysis1.getEnable()){
				if (negotiation) {
					setupNegotiationDBot(architectureAnalysis1, trackAlgorithm, choices);
				}
				else {
					startAnalysis(architectureAnalysis1, trackAlgorithm);
				}
			}
		}
			
		if (negotiation){
			searchAndNegotiation(choices);
		}
		else{
			choices = null;
		}
		return "Full Analisys running.";
	}
	
	
	@Override
	public String startIndividualArchitectureAnalysis(ArchitectureAnalysis architectureAnalysis, TrackAlgorithm trackAlgorithm) {
		return startAnalysis(architectureAnalysis,trackAlgorithm);
	}
	
	
	private String startAnalysis(ArchitectureAnalysis architectureAnalysis, TrackAlgorithm trackAlgorithm) {
		DesignBotExecutor designBotExecutor = architectureAnalysis.getDesignBot().getQualityAttribute().getDesignBotExecutor();
		designBotExecutor.setArchitectureAnalysis(architectureAnalysis);
		
		int running = runningDesignBots.indexOf(designBotExecutor);
		if (running != -1) {
			if (runningDesignBots.get(running).isAlive())
				return "Analisys " + designBotExecutor.getArchitectureAnalysis().getId() + " already running." ;
			else
				runningDesignBots.remove(running);
		}
		
		AnalysisResults analysis = analysisResults.get(architectureAnalysis.getArchitecture().getId());
		if (analysis == null) {
			analysis = new AnalysisResults(architectureAnalysis.getArchitecture());
			analysisResults.put(architectureAnalysis.getArchitecture().getId(), analysis);
		}
		designBotExecutor.setAnalysisResults(analysis);
		designBotExecutor.setTrackAlgorithm(trackAlgorithm);
		designBotExecutor.start();
		runningDesignBots.add(designBotExecutor);
		return "Analisys " + designBotExecutor.getArchitectureAnalysis().getId() + " running.";
	}

	
	@Override
	public String stopIndividualArchitectureAnalysis(ArchitectureAnalysis architectureAnalysis) {
		DesignBotExecutor designBotExecutor = architectureAnalysis.getDesignBot().getQualityAttribute().getDesignBotExecutor();
		designBotExecutor.setArchitectureAnalysis(architectureAnalysis);
		
		int running = runningDesignBots.indexOf(designBotExecutor);
		if (running == -1) {
			return "Analisys " + designBotExecutor.getArchitectureAnalysis().getId() + " already stopped." ;
		} else {
			if (runningDesignBots.get(running).isAlive()) {
				runningDesignBots.get(running).stopRun();
			} else {
				return "Analisys " + designBotExecutor.getArchitectureAnalysis().getId() + " already stopped." ;
			}
			runningDesignBots.remove(running);	
		}	
		return "Analisys " + designBotExecutor.getArchitectureAnalysis().getId() + "stopped.";
	}

	
	@Override
	public AnalysisResults getAnalysisResultsByArchitectureId(Long id_architecture) {
		return analysisResults.get(id_architecture);
	}

	
	@Override
	public void deleteAllAnalysis() {
		runningDesignBots.clear();
		analysisResults.clear();
	}
	
	
	/**
	 * El DBot involucrado en este analisis se incluye en la lista de runningBots. <br><br>
	 * Actualmente no hay una interfaz para seleccionar el valor umbral y funcion de utilidad asociada al DBot. Estos valores son fijos
	 * 
	 * @param architectureAnalysis configuracion de analisis asociada a un DBot
	 * @param trackAlgorithm Algoritmo de busqueda a utilizar
	 * 
	 */
	private String setupNegotiationDBot(ArchitectureAnalysis architectureAnalysis, TrackAlgorithm trackAlgorithm, List<NegotiationChoice> choices){
		DBNegotiationExecutor negotiationBotExecutor = null;
		
		AnalysisResults analysis = analysisResults.get(architectureAnalysis.getArchitecture().getId());
		if (analysis == null) {
			analysis = new AnalysisResults(architectureAnalysis.getArchitecture());
			analysisResults.put(architectureAnalysis.getArchitecture().getId(), analysis);
		}
		
		//Utilidad
		IUtilityFunction utilityFunction = NegotiationFeaturesFactory.generateUtility(architectureAnalysis.getScenario(),analysis.getActualArchitectureResult(),properties.getProperty("analysis"+architectureAnalysis.getId()+".utility"));
		
		negotiationBotExecutor = new DBNegotiationExecutor(architectureAnalysis.getDesignBot().getQualityAttribute().getDesignBotExecutor(),utilityFunction);
		negotiationBotExecutor.setArchitectureAnalysis(architectureAnalysis);
		negotiationBotExecutor.setTrackAlgorithm(trackAlgorithm);
		
		
		double threshold = NegotiationFeaturesFactory.generateThreshold(negotiationBotExecutor, properties.getProperty("analysis"+architectureAnalysis.getId()+".threshold"));
		negotiationBotExecutor.setThreshold(threshold);
		
		negotiationBotExecutor.setListChoices(choices);
		
		negotiationBotExecutor.setAnalysisResults(analysis);
		negotiationBotExecutor.start();
		
		runningDesignBots.add(negotiationBotExecutor);
		
		return "Analisys " + negotiationBotExecutor.getArchitectureAnalysis().getId() + " running.";
	}
	
	
	public void esperar(){
		for (DesignBotExecutor dbot : runningDesignBots){
			while ((!((DBNegotiationExecutor) dbot).isWaiting()) && (dbot.isAlive())){}
		}
	}
	
	
	public void searchAndNegotiation(List<NegotiationChoice> choices){
		String propLevel = properties.getProperty("search.maxlevel");
		int maxLevelSearch = ((propLevel != null) && (StringUtils.isNumeric(propLevel))) ? Integer.parseInt(properties.getProperty("search.maxlevel")) : DEFAULT_LEVEL_SEARCH;
		
		esperar();
		// mientras no se hayan completado la cantidad de alternativas de busqueda deseadas, se notifica a los dBots y se espera al siguiente turno.
		int index = 0;
		
		int level = 0;
		int levelSize = choices.size();
		while (level < maxLevelSearch){
			for (DesignBotExecutor dbot : runningDesignBots){
				((DBNegotiationExecutor) dbot).notificar();
			}
			esperar();
			// al llegar al ultimo elemento del nivel actual, se obtiene el tamanio del proximo y se aumenta el contador de nivel (esto asegura que la busqueda se realice por niveles completos)
			if (index == levelSize){
				level++;
				levelSize = choices.size();
			}
			index++;
		}
		
		// una vez que se completo el arbol de busqueda, se detiene el procesamiento de los dbots, finalizando estos con el ordenamiento del arbol de acuerdo a sus prioridades.
		for (DesignBotExecutor dbot : runningDesignBots){
			dbot.stopRun();
			((DBNegotiationExecutor) dbot).notificar();
		}
		esperar();
				
		log.info("====== Starting Negotiation - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()) + " ======\n");
		for (DesignBotExecutor dbot : runningDesignBots){
			log.info("Analysis-" + dbot.getArchitectureAnalysis().getId() + " (Scenario " + dbot.getArchitectureAnalysis().getScenario().getName() + "): " + ((DBNegotiationExecutor)dbot).getCountCandidates() + " candidates");
		}
		
		//Funcion de negociacion
		IZeuthenFunction negotiationFunction = NegotiationFeaturesFactory.generateZeuthen(properties.getProperty("mediator.negotiationStrategy"));
		
		mediator = new NegotiationMediator(runningDesignBots,negotiationFunction);
		mediator.initNegotiation();
		
		log.info("====== End Negotiation ======\n");
	}
	
	
	
}
