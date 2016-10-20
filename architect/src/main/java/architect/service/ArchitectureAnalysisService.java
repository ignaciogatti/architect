package architect.service;

import java.util.List;

import architect.model.ArchitectureAnalysis;
import architect.model.ElementChange;

public interface ArchitectureAnalysisService extends MessageProcessorService {

	List<ArchitectureAnalysis> listArchitectureAnalysisByArchitectureId(Long architectureId);
	
	ArchitectureAnalysis getArchitectureAnalysis(Long id_architectureAnalysis);
	
	ArchitectureAnalysis getArchitectureAnalysisEAGER(Long id_architectureAnalysis);

	ElementChange add(ArchitectureAnalysis architectureAnalysis, boolean undo);
	
	ElementChange delete(Long architectureAnalysisId, boolean undo);
	
	ElementChange update(ArchitectureAnalysis oldArchitectureAnalysis, ArchitectureAnalysis newArchitectureAnalysis, boolean undo);
	
}
