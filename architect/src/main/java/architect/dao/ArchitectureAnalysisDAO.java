package architect.dao;

import java.util.List;

import architect.model.ArchitectureAnalysis;

public interface ArchitectureAnalysisDAO {
	
	List<ArchitectureAnalysis> listArchitectureAnalysisByArchietctureId(
			Long architectureId);

	ArchitectureAnalysis getArchitectureAnalysis(Long id_architectureAnalysis);
	
	ArchitectureAnalysis getArchitectureAnalysisEAGER(Long id_architectureAnalysis);

	Long add(ArchitectureAnalysis architectureAnalysis);

	void delete(ArchitectureAnalysis architectureAnalysis);

	void update(ArchitectureAnalysis architectureAnalysis);
	
}
