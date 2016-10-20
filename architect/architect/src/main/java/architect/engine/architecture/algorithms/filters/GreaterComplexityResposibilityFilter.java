package architect.engine.architecture.algorithms.filters;

import java.util.ArrayList;
import java.util.List;

import architect.engine.architecture.statistics.ScenarioStatistics;
import architect.model.Element;
import architect.model.Responsibility;
import architect.model.Scenario;

public class GreaterComplexityResposibilityFilter implements Filter {

	@Override
	public List<Element> getFilteredElements(Element toAnalize) {
		ScenarioStatistics scenarioStats = new ScenarioStatistics((Scenario) toAnalize);
		Responsibility responsibilityToSplit = scenarioStats.getGreaterComplexityResposibility();
		List<Element> responsibilityList = new ArrayList<Element>();
		responsibilityList.add(responsibilityToSplit);
		return responsibilityList;
	}

}
