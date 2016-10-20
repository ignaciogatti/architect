package architect.engine;

import java.util.List;

import architect.engine.architecture.algorithms.filters.Filter;
import architect.model.Architecture;
import architect.model.Element;
import architect.model.ElementChange;
import architect.model.Scenario;

public interface TacticExecutor {
		
	Filter getDefaultFilter();
	
	List<Element> getElementsToIterateTactic(Architecture architecture, Scenario scenario);
	
	Element getElementToApplyTactic(Architecture architecture, Element element);
	
	void applyTactic(Architecture architecture, Scenario scenario, Element element, List<ElementChange> changes);
		
	int compareScenariosCosts(Scenario scenario1, Scenario scenario2);
}
