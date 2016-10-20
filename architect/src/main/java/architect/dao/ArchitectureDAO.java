package architect.dao;

import java.util.List;

import architect.model.Architecture;

public interface ArchitectureDAO {

	List<Architecture> listArchitectures();
	
	Architecture getArchitectureById(Long id);

	List<Architecture> getArchitectureByGroupId(Long id);

	void deleteArchitecture(Architecture architecture);

	void updateArchitecture(Architecture architecture);

	Long addArchitecture(Architecture newArchitecture);

	int blockArchitecture(Long architectureId, String blockReason);

	int unblockArchitecture(Long architectureId);
		
}
