package architect.service;

import java.util.List;
import java.util.Set;

import architect.model.Architecture;
import architect.model.ElementChange;
import architect.model.Group;

public interface ArchitectureService {

	List<Architecture> listArchitectures();
	
	Architecture getArchitectureById(Long id);

	Architecture validateArchitecture(Long id_architecture, String username, boolean modificationAction) throws Exception;

	Group getArchitectureGroup(Long id_architecture);

	List<Architecture> listArchitecturesByGroups(Set<Group> userGroups);

	ElementChange deleteArchitecture(Long id_architecture);

	ElementChange updateArchitecture(Architecture architecture);

	ElementChange addArchitecture(Architecture newArchitecture);

	int blockArchitecture(Long architectureId, String blockReason);

	int unblockArchitecture(Long architectureId);
	
}
