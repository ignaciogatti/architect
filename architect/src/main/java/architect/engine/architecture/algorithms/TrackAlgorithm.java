package architect.engine.architecture.algorithms;

import java.util.ArrayList;
import java.util.List;

import architect.engine.TacticExecutor;
import architect.engine.architecture.TacticChange;
import architect.model.Architecture;
import architect.model.NegotiationChoice;
import architect.model.Scenario;

public abstract class TrackAlgorithm {
	
	
	/**
	 * Efectua la busqueda del mejor elemento de un escenario donde aplicar la tactica indicada (no son busquedas exhaustivas sino heuristicas)
	 * 
	 * @param architecture
	 * @param scenario
	 * @param tactic
	 * @param tacticChange
	 * @param choices
	 * @return
	 */
	public abstract Architecture _searchAndApply(Architecture architecture, Scenario scenario, TacticExecutor tactic, TacticChange tacticChange, List<NegotiationChoice> choices);

	public Architecture searchAndApply(TacticExecutor tactic, Scenario scenario, TacticChange tacticChange, List<NegotiationChoice> choices) {
		Architecture architecture = scenario.getArchitecture().clone();
		List<Scenario> clonedScenarios = new ArrayList<Scenario>(architecture.getScenarios());
		Scenario clonedScenario = clonedScenarios.get(clonedScenarios.indexOf(scenario));
		return _searchAndApply(architecture, clonedScenario, tactic, tacticChange, choices);
	}

}
