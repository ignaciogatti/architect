package architect.service.analysis;

import architect.engine.architecture.AnalysisResults;
import architect.engine.architecture.algorithms.TrackAlgorithm;
import architect.model.ArchitectureAnalysis;

public interface AnalysisService {
	
	/**
	 * Metodo utilizado para ejecutar el proceso de evaluacion completa de una arquitectura
	 * 
	 * @param architectureId
	 * @param trackAlgorithm algoritmo de busqueda a utilizar
	 * @param negotiation flag que indica el uso de negociacion o no
	 */
	String startFullAnalysis(Long architectureId, TrackAlgorithm trackAlgorithm, Boolean negotiation);

	String startIndividualArchitectureAnalysis(ArchitectureAnalysis architectureAnalysis, TrackAlgorithm trackAlgorithm);
	
	String stopIndividualArchitectureAnalysis(ArchitectureAnalysis architectureAnalysis);
	
	AnalysisResults getAnalysisResultsByArchitectureId(Long id_architecture);

	void deleteAllAnalysis();

}
