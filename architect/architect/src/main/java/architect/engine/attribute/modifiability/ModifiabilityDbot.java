package architect.engine.attribute.modifiability;

import java.util.ArrayList;
import java.util.List;

import architect.engine.DesignBotExecutor;
import architect.engine.architecture.TacticChange;
import architect.engine.architecture.statistics.ScenarioStatistics;
import architect.model.Architecture;
import architect.model.ArchitectureAnalysis;
import architect.model.Scenario;

public class ModifiabilityDbot extends DesignBotExecutor {
	
	public ModifiabilityDbot() {
	}
	
	public ModifiabilityDbot(ArchitectureAnalysis architectureAnalysis) {
		super(architectureAnalysis);
	}

	@Override
	public Double getScenarioCost(Scenario scenario) {
		ScenarioStatistics scenarioStats = new ScenarioStatistics(scenario);
		return scenarioStats.getScenarioModifiabilityCost()*scenario.getMeasure();
	}
	
	@Override
	protected Architecture selectBestArchitecture(Architecture bestArchi,
			Architecture newArchi, Scenario scenario,
			List<TacticChange> bestArchiChanges, List<TacticChange> newChanges) {
		
		List<Scenario> bestArchiScenarios = new ArrayList<Scenario>(bestArchi.getScenarios());
		Double bestCost = getScenarioCost(bestArchiScenarios.get(bestArchiScenarios.indexOf(scenario)));
		
		List<Scenario> newArchiScenarios = new ArrayList<Scenario>(newArchi.getScenarios());
		Double newArchiCost = getScenarioCost(newArchiScenarios.get(newArchiScenarios.indexOf(scenario)));
		System.out.println("Scenario " + scenario.getName() + " Cost " + newArchiCost);
		if (newArchiCost.compareTo(bestCost) < 0) {
			bestArchiChanges.clear();
			bestArchiChanges.addAll(newChanges);
			return newArchi;
		}
		return bestArchi;
		
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

}
