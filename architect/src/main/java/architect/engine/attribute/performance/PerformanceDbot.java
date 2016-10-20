package architect.engine.attribute.performance;

import java.util.ArrayList;
import java.util.List;

import architect.engine.DesignBotExecutor;
import architect.engine.architecture.TacticChange;
import architect.engine.mast.MastSimConnector;
import architect.model.Architecture;
import architect.model.ArchitectureAnalysis;
import architect.model.Scenario;

public class PerformanceDbot extends DesignBotExecutor {
	
	public PerformanceDbot() {
	}
	
	public PerformanceDbot(ArchitectureAnalysis architectureAnalysis) {
		super(architectureAnalysis);
	}

	@Override
	public Double getScenarioCost(Scenario scenario) {
		Double performance = (new MastSimConnector()).execute(scenario);
		return performance;
	}
	
	@Override
	protected Architecture selectBestArchitecture(Architecture bestArchi,
			Architecture newArchi, Scenario scenario,
			List<TacticChange> bestArchiChanges, List<TacticChange> newChanges) {
		
		List<Scenario> bestArchiScenarios = new ArrayList<Scenario>(bestArchi.getScenarios());
		Double bestCost = getScenarioCost(bestArchiScenarios.get(bestArchiScenarios.indexOf(scenario)));
		
		List<Scenario> newArchiScenarios = new ArrayList<Scenario>(newArchi.getScenarios());
		Double newArchiCost = getScenarioCost(newArchiScenarios.get(newArchiScenarios.indexOf(scenario)));
		
		if (bestCost >= newArchiCost) {
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
