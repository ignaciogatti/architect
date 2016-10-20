package architect.engine.attribute.performance;

import java.util.ArrayList;
import java.util.List;

import architect.engine.TacticExecutor;
import architect.engine.architecture.ChangeType;
import architect.engine.architecture.ElementType;
import architect.engine.architecture.algorithms.filters.Filter;
import architect.engine.architecture.algorithms.filters.LowestPeriodScenarioFilter;
import architect.engine.mast.MastSimConnector;
import architect.model.Architecture;
import architect.model.Element;
import architect.model.ElementChange;
import architect.model.Scenario;

public class IncreaseScenarioPeriod implements TacticExecutor, Cloneable {
	
	@Override
	public Filter getDefaultFilter() {
		return new LowestPeriodScenarioFilter();
	}
	
	@Override
	public List<Element> getElementsToIterateTactic(Architecture architecture, Scenario scenario) {
		List<Element> scenarios = new ArrayList<Element>(scenario.getSharedReponsibilitiesScenarios(architecture.getPerformanceScenarios()));
		scenarios.remove((Scenario) scenario);
		return scenarios;
	}
	
	@Override
	public Element getElementToApplyTactic(Architecture architecture, Element element) {
		List<Scenario> scenarios = new ArrayList<Scenario>(architecture.getScenarios());
		return scenarios.get(scenarios.indexOf((Scenario) element));
	}
	
	@Override
	public int compareScenariosCosts(Scenario scenario1, Scenario scenario2) {
		Double scenario1Cost = (new MastSimConnector()).execute(scenario1);
		Double scenario2Cost = (new MastSimConnector()).execute(scenario2);
		return scenario1Cost.compareTo(scenario2Cost);
	}
	
	@Override
	public void applyTactic(Architecture architecture, Scenario scenario, Element element, List<ElementChange> changes) {
		
		//Update Scenario Period
		Scenario scenarioToUpdate = (Scenario) element;
		//scenarioToUpdate.setMeasure((long) (scenarioToUpdate.getMeasure() * 2));
		scenarioToUpdate.setPeriod((long) (scenarioToUpdate.getPeriod() * 2));
		
		//scenario.setMeasure((long) (scenario.getMeasure() * 2));
		scenario.setPeriod((long) (scenario.getPeriod() * 2));
		
		changes.add(new ElementChange(null, ElementType.SCENARIO
				.toString(), ChangeType.UPDATE.toString(), null,
				scenarioToUpdate, true, false));
		
	}

}
