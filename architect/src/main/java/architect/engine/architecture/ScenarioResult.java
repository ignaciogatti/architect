package architect.engine.architecture;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import architect.model.Scenario;

@XmlRootElement(name="architectureResults")
@XmlAccessorType(XmlAccessType.NONE)
public class ScenarioResult {
	
	@XmlElement
	private Scenario scenario;
	
	@XmlAttribute
	private Double scenarioCost;
	
	//@XmlAttribute
	//private int costComparedWithOriginal;

	public ScenarioResult() {
		
	}
	
	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public Double getScenarioCost() {
		return scenarioCost;
	}

	public void setScenarioCost(Double scenarioCost) {
		this.scenarioCost = roundTwoDecimals(scenarioCost);
		//this.costComparedWithOriginal = this.scenarioCost.compareTo(originalScenarioCost);
	}
	
	private double roundTwoDecimals(Double d) {
		BigDecimal bd = new BigDecimal(d.doubleValue());
	    BigDecimal rounded = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
	    return rounded.doubleValue();
	}

}
