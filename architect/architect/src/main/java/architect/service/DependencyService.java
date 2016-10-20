package architect.service;

import java.util.List;

import architect.model.Dependency;
import architect.model.DependencyId;
import architect.model.ElementChange;

public interface DependencyService extends MessageProcessorService {

	List<Dependency> listDependencyByArchietctureId(Long architectureId);

	Dependency getDependencyById(DependencyId idDep);

	ElementChange add(Dependency dependency, boolean consistentState, boolean undo);
	
	ElementChange delete(DependencyId dependencyId, boolean consistentState, boolean undo);
	
	ElementChange update(Dependency oldDependency, Dependency newDependency, boolean consistentState, boolean undo);
	
}
