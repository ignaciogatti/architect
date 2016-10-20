package architect.engine.architecture.algorithms.filters;

import java.util.ArrayList;
import java.util.List;

import architect.engine.architecture.statistics.ModuleStatistics;
import architect.engine.architecture.statistics.ScenarioStatistics;
import architect.model.Element;
import architect.model.Scenario;

public class GreaterCouplingModuleFilter implements Filter {

	@Override
	public List<Element> getFilteredElements(Element toAnalize) {
		ScenarioStatistics scenarioStats = new ScenarioStatistics((Scenario) toAnalize);
		ModuleStatistics moduleStats = scenarioStats.getGreaterCouplingModule();
		List<Element> moduleStatsList = new ArrayList<Element>();
		moduleStatsList.add(moduleStats.getModule());
		return moduleStatsList;
	}

}
