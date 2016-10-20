package architect.engine.architecture.algorithms;

import java.util.ArrayList;
import java.util.List;

import architect.engine.TacticExecutor;
import architect.engine.architecture.TacticChange;
import architect.model.Architecture;
import architect.model.Element;
import architect.model.ElementChange;
import architect.model.NegotiationChoice;
import architect.model.Scenario;

public class Backtracking extends TrackAlgorithm {
	
	private static int appliedTacticsCount = 0;
	public Backtracking() {
	}

	public Architecture _searchAndApply(Architecture architecture, Scenario scenario, TacticExecutor tactic, TacticChange tacticChange, List<NegotiationChoice> choices) {
		try {
			Architecture bestArchitecture = architecture;
			Scenario bestScenario = scenario;
			Element bestAppliedElement = null;
			List<ElementChange> bestAppliedChanges = new ArrayList<ElementChange>();
		
			for (Element element : tactic.getElementsToIterateTactic(architecture, scenario)) {
				Architecture newArchitecture = architecture.clone();

				Element elementToApplyTactic = tactic.getElementToApplyTactic(newArchitecture, element);
			
				List<Scenario> newScenarios = new ArrayList<Scenario>(newArchitecture.getScenarios());
				List<ElementChange> newChanges = new ArrayList<ElementChange>();
				Scenario actualScenario = newScenarios.get(newScenarios.indexOf(scenario));
			
				tactic.applyTactic(newArchitecture, actualScenario, elementToApplyTactic, newChanges);
				appliedTacticsCount++;
				
				// si la lista de candidatos no es nula (se utiliza negociacion) se agrega cada alternativa encontrada
				if (choices != null){
					List<TacticChange> changes = new ArrayList<TacticChange>();
					TacticChange change = new TacticChange();
					change.setId(tacticChange.getId());
					change.setTacticName(tacticChange.getTacticName());
					change.setAppliedTo(elementToApplyTactic);
					change.setAppliedChanges(newChanges);
					changes.add(change);
					NegotiationChoice choice = new NegotiationChoice(newArchitecture,changes,null);
					choices.add(choice);
				}
				
				System.out.println("Backtracking Applied Tactics : " + appliedTacticsCount);
				if (tactic.compareScenariosCosts(actualScenario, bestScenario) < 0 ) {
					bestScenario = actualScenario;
					bestArchitecture = newArchitecture;
					bestAppliedElement = elementToApplyTactic;
					bestAppliedChanges = newChanges;
				}
			}
		
			tacticChange.setAppliedTo(bestAppliedElement);
			tacticChange.setAppliedChanges(bestAppliedChanges);
		
			if (tacticChange.getAppliedTo()!=null)
				return bestArchitecture;
			else
				return null;
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

}
