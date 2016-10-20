package architect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import architect.dao.DependencyDAO;
import architect.engine.architecture.ChangeType;
import architect.engine.architecture.ElementType;
import architect.model.Dependency;
import architect.model.DependencyId;
import architect.model.ElementChange;
import architect.service.diagram.DiagramDependencyService;

@Service("dependencyService")
public class DependencyServiceImpl extends MessageProcessorServiceImpl implements DependencyService {

	@Autowired
    private DependencyDAO dependencyDAO;
	
	@Autowired
	private DiagramDependencyService diagramDependencyService;

	@Override
	public List<Dependency> listDependencyByArchietctureId(Long architectureId) {
		return dependencyDAO.listDependenciesByArchietctureId(architectureId);
	}
	
	@Override
	public Dependency getDependencyById(DependencyId idDep) {
		return dependencyDAO.getDependencyById(idDep);
	}
	
	@Override
	public ElementChange add(Dependency dependency, boolean consistentState, boolean undo) {
		DependencyId addedDependencyId = dependencyDAO.add(dependency);
		Dependency addedDependency = (Dependency) this.getDependencyById(addedDependencyId);
		ElementChange change = null;
		if (addedDependency != null) {
			change = new ElementChange(dependency.getArchitecture().getId(),
					ElementType.DEPENDENCY.toString(),
					ChangeType.INSERT.toString(), null, addedDependency,
					new Boolean(consistentState), new Boolean(undo));
			postChange(change,dependencyQueueTemplate);
			diagramDependencyService.add(addedDependency);
		}
		return change;
	}
	
	@Override
	public ElementChange delete(DependencyId dependencyId, boolean consistentState, boolean undo) {
		Dependency depToDelete = (Dependency) dependencyDAO.getDependencyById(dependencyId);
		depToDelete.getArchitecture().getDependencies().remove(depToDelete);
		depToDelete.getChildResponsibility().getDependenciesAsChild().remove(depToDelete);
		depToDelete.getParentResponsibility().getDependenciesAsParent().remove(depToDelete);
		dependencyDAO.delete(depToDelete);
		ElementChange change = new ElementChange(depToDelete.getArchitecture()
				.getId(), ElementType.DEPENDENCY.toString(),
				ChangeType.DELETE.toString(), depToDelete, null, new Boolean(
						consistentState), new Boolean(undo));
		postChange(change,dependencyQueueTemplate);
		diagramDependencyService.delete(depToDelete);
		return change;
	}
	
	@Override
	public ElementChange update(Dependency oldDependency, Dependency newDependency, boolean consistentState, boolean undo) {
		ElementChange change = new ElementChange(newDependency
				.getArchitecture().getId(), ElementType.DEPENDENCY.toString(),
				ChangeType.UPDATE.toString(), oldDependency, newDependency,
				new Boolean(consistentState), new Boolean(undo));
		dependencyDAO.update(newDependency);
		postChange(change,dependencyQueueTemplate);
		return change;
	}

	/**
	 * Dependencies sync properties and methods
	 */

	@Autowired
	private JmsTemplate dependencyQueueTemplate;

	@Autowired
	private JmsTemplate dependencyTopicTemplate;

	@Override
	public void doProcessing(final String msg) {
		this.doProcessing(msg, dependencyTopicTemplate);
	}


}
