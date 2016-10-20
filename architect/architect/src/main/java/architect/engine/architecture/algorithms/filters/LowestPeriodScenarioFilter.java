package architect.engine.architecture.algorithms.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import architect.model.Element;
import architect.model.Scenario;

public class LowestPeriodScenarioFilter implements Filter {

	@Override
	public List<Element> getFilteredElements(Element toAnalize) {
		Scenario scenario = (Scenario) toAnalize;
		List<Element> scenarios = new ArrayList<Element>(scenario.getSharedReponsibilitiesScenarios(scenario.getArchitecture().getPerformanceScenarios()));
		scenarios.remove((Scenario) scenario);
		
		Comparator<Element> comparatorByPeriod = new Comparator<Element>() {
		    public int compare(Element s1, Element s2) {
		        return ((Scenario)s1).getMeasure().compareTo(((Scenario)s2).getMeasure()) * (-1);
		    }
		};
		Collections.sort(scenarios, comparatorByPeriod);
		return scenarios;
	}

}
