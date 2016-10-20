package architect.dao;

import java.util.List;

import architect.model.Dependency;
import architect.model.DependencyId;

public interface DependencyDAO {
	
	List<Dependency> listDependenciesByArchietctureId( Long architectureId);

	Dependency getDependencyById(DependencyId idDep);

	DependencyId add(Dependency dependency);

	void delete(Dependency dependency);

	void update(Dependency dependency);

	
}
