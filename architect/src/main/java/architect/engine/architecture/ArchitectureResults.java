package architect.engine.architecture;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import architect.model.Architecture;
import architect.model.Scenario;

@XmlRootElement(name="architectureResults")
@XmlAccessorType(XmlAccessType.NONE)
public class ArchitectureResults {

	@XmlElement
	private List<ScenarioResult> ScenarioResults = new ArrayList<ScenarioResult>();

	public ArchitectureResults() {
	}
	
	public ArchitectureResults(Architecture architecture, ArchitectureResults originalArchitectureResults) {
		if (architecture!=null) {
			for (Scenario scenario : architecture.getScenarios()) {
				ScenarioResult scenarioResult = new ScenarioResult();
				scenarioResult.setScenario(scenario);
				scenarioResult.setScenarioCost(scenario.getQualityAttribute().getDesignBotExecutor().getScenarioCost(scenario));
				ScenarioResults.add(scenarioResult);
			}
		}
	}
	
	public Double getScenarioCost(Scenario scenario) {
		for (ScenarioResult scenarioResult : ScenarioResults) {
			if (scenarioResult.getScenario().equals(scenario)) {
				return scenarioResult.getScenarioCost();
			}
		}
		return 0D;
	}

	public List<ScenarioResult> getScenarioResults() {
		return ScenarioResults;
	}

	public void setScenarioResults(List<ScenarioResult> scenarioResults) {
		ScenarioResults = scenarioResults;
	}
	
}
