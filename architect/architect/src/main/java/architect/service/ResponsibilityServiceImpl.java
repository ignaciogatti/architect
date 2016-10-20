package architect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import architect.dao.ResponsibilityDAO;
import architect.engine.architecture.ChangeType;
import architect.engine.architecture.ElementType;
import architect.model.ElementChange;
import architect.model.Responsibility;
import architect.service.diagram.DiagramResponsibilityService;

@Service("responsibilityService")
public class ResponsibilityServiceImpl extends MessageProcessorServiceImpl implements ResponsibilityService {

	@Autowired
    private ResponsibilityDAO responsibilityDAO;
	
	@Autowired
    private DiagramResponsibilityService diagramResponsibilityService;

	@Override
	public List<Responsibility> listResponsibilitiesByArchietctureId(
			Long architectureId) {
		return responsibilityDAO.listResponsibilitiesByArchietctureId(architectureId);
	}
	
	@Override
	public Responsibility getResponsibility(Long responsibilityId) {
		return responsibilityDAO.getResponsibility(responsibilityId);
	}

	@Override
	public ElementChange add(Responsibility newResponsibility, boolean consistentState, boolean undo) {
		Long addedResponsibilityId = responsibilityDAO.add(newResponsibility);
		Responsibility addedResponsibility = (Responsibility) this.getResponsibility(addedResponsibilityId);
		ElementChange change = null;
		if (addedResponsibility != null) {
			change = new ElementChange(newResponsibility.getArchitecture().getId(), ElementType.RESPONSIBILITY.toString(),
					ChangeType.INSERT.toString(), null, addedResponsibility, new Boolean(consistentState), new Boolean(undo));
			postChange(change,responsibilityQueueTemplate);
			diagramResponsibilityService.add(addedResponsibility);
		}
		return change;
	}

	@Override
	public ElementChange delete(Long responsibilityId, boolean consistentState, boolean undo) {
		Responsibility respToDelete = (Responsibility) responsibilityDAO.getResponsibility(responsibilityId);
		respToDelete.getArchitecture().getResponsibilities().remove(respToDelete);
		responsibilityDAO.delete(respToDelete);
		ElementChange change = new ElementChange(respToDelete.getArchitecture().getId(), ElementType.RESPONSIBILITY.toString(),
				ChangeType.DELETE.toString(), respToDelete, null, new Boolean(consistentState), new Boolean(undo));
		postChange(change,responsibilityQueueTemplate);
		diagramResponsibilityService.delete(respToDelete);
		return change;
	}

	@Override
	public ElementChange update(Responsibility oldResponsibility, Responsibility newResponsibility, boolean consistentState, boolean undo) {
		ElementChange change = new ElementChange(newResponsibility.getArchitecture().getId(), ElementType.RESPONSIBILITY.toString(),
				ChangeType.UPDATE.toString(), oldResponsibility, newResponsibility, new Boolean(consistentState), new Boolean(undo));
		responsibilityDAO.update(newResponsibility);
		postChange(change,responsibilityQueueTemplate);
		return change;
	}
	
	/**
	 * Responsibilities sync properties and methods
	 */

	@Autowired
	private JmsTemplate responsibilityQueueTemplate;

	@Autowired
	private JmsTemplate responsibilityTopicTemplate;

	@Override
	public void doProcessing(final String msg) {
		this.doProcessing(msg, responsibilityTopicTemplate);
	}
	
}
