package architect.service;

import java.util.List;

import architect.model.ElementChange;
import architect.model.Scenario;

public interface ScenarioService extends MessageProcessorService {

	Scenario getScenario(Long id);
	
	List<Scenario> listScenariosByArchitectureId(Long id);
	
	ElementChange add(Scenario scenario, boolean consistentState, boolean undo);
	
	ElementChange delete(Long scenarioId, boolean consistentState, boolean undo);
	
	ElementChange update(Scenario oldScenario, Scenario newScenario, boolean consistentState, boolean undo);
		
}
