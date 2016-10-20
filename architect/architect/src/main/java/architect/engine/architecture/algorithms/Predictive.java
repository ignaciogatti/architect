package architect.engine.architecture.algorithms;

import java.util.ArrayList;
import java.util.List;

import architect.engine.TacticExecutor;
import architect.engine.architecture.TacticChange;
import architect.model.Architecture;
import architect.model.Element;
import architect.model.NegotiationChoice;
import architect.model.Scenario;

public class Predictive extends TrackAlgorithm {

	private static int appliedTacticsCount = 0;
	
	public Predictive() {
	}

	public Architecture _searchAndApply(Architecture architecture, Scenario scenario, TacticExecutor tactic, TacticChange tacticChange,List<NegotiationChoice> choices) {
		List<Element> candidates = tactic.getDefaultFilter().getFilteredElements(scenario);
		if (candidates!=null && candidates.size()>0) {
			tacticChange.setAppliedTo(candidates.get(0));
			tactic.applyTactic(architecture, scenario, tacticChange.getAppliedTo(), tacticChange.getAppliedChanges());
			appliedTacticsCount++;
			System.out.println("Predictive Applied Tactics : " + appliedTacticsCount);
			
			// si la lista de candidatos no es nula (se utiliza negociacion) se agregan alternativas
			if (choices != null){
				List<TacticChange> changes = new ArrayList<TacticChange>();
				changes.add(tacticChange);
				NegotiationChoice choice = new NegotiationChoice(architecture,changes,null);
				choices.add(choice);
			}
			return architecture;
		} else {
			return null;
		}
	}

}
