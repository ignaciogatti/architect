package architect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import architect.dao.ScenarioDAO;
import architect.engine.architecture.ChangeType;
import architect.engine.architecture.ElementType;
import architect.model.ElementChange;
import architect.model.Scenario;

@Service("scenarioService")
public class ScenarioServiceImpl extends MessageProcessorServiceImpl implements ScenarioService {

	@Autowired
	private ScenarioDAO scenarioDAO;

	@Autowired
	private ElementChangeService elementChangeService;

	@Override
	public Scenario getScenario(Long id) {
		return scenarioDAO.getScenario(id);
	}

	@Override
	public List<Scenario> listScenariosByArchitectureId(Long id) {
		return scenarioDAO.listScenariosByArchitectureId(id);
	}

	@Override
	public ElementChange add(Scenario scenario, boolean consistentState, boolean undo) {
		Long addedScenarioId = scenarioDAO.add(scenario);
		Scenario addedScenario = (Scenario) this.getScenario(addedScenarioId);
		ElementChange change = null;
		if (addedScenario != null) {
			change = new ElementChange(scenario.getArchitecture().getId(),
					ElementType.SCENARIO.toString(),
					ChangeType.INSERT.toString(), null, addedScenario,
					new Boolean(consistentState), new Boolean(undo));
			postChange(change,scenarioQueueTemplate);
		}
		return change;
	}

	@Override
	public ElementChange delete(Long scenarioId, boolean consistentState, boolean undo) {
		Scenario scenarioToDelete = (Scenario) scenarioDAO.getScenario(scenarioId);
		scenarioToDelete.getArchitecture().getScenarios().remove(scenarioToDelete);
		scenarioDAO.delete(scenarioToDelete);
		ElementChange change = new ElementChange(scenarioToDelete
				.getArchitecture().getId(), ElementType.SCENARIO.toString(),
				ChangeType.DELETE.toString(), scenarioToDelete, null,
				new Boolean(consistentState), new Boolean(undo));
		postChange(change,scenarioQueueTemplate);
		return change;
	}

	@Override
	public ElementChange update(Scenario oldScenario, Scenario newScenario, boolean consistentState, boolean undo) {
		ElementChange change = new ElementChange(newScenario.getArchitecture()
				.getId(), ElementType.SCENARIO.toString(),
				ChangeType.UPDATE.toString(), oldScenario, newScenario,
				new Boolean(consistentState), new Boolean(undo));
		scenarioDAO.update(newScenario);
		postChange(change,scenarioQueueTemplate);
		return change;
	}

	/**
	 * Scenarios sync properties and methods
	 */

	@Autowired
	private JmsTemplate scenarioQueueTemplate;

	@Autowired
	private JmsTemplate scenarioTopicTemplate;

	@Override
	public void doProcessing(final String msg) {
		this.doProcessing(msg, scenarioTopicTemplate);
	}

}
