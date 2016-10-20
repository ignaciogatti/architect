package architect.engine.architecture;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import architect.model.Architecture;

@XmlRootElement(name="analysisResults")
@XmlAccessorType(XmlAccessType.NONE)
public class AnalysisResults {

	@XmlElement(name="actualArchitectureResult")
	private ArchitectureResults actualArchitectureResult;

	@XmlElement(name="designBotResult")
	private List<DesignBotResults> designBotResults = new ArrayList<DesignBotResults>();
    
	public List<DesignBotResults> getDesignBotResults() {
		return designBotResults;
	}
	
	public AnalysisResults() {
	}
	
	public AnalysisResults(Architecture architecture) {
		this.actualArchitectureResult = new ArchitectureResults(architecture,null);
	}
	
	public DesignBotResults getDesignBotResultsById(Long idArchitectureAnalysis){
		for (DesignBotResults designBotResult: designBotResults) {
			if (designBotResult.getArchitectureAnalysis().getId().equals(idArchitectureAnalysis))
				return designBotResult;
		}
		return null;
	}

	public void setDesignBotResults(List<DesignBotResults> designBotResults) {
		this.designBotResults = designBotResults;
	}
	
	public void addDesignBotResult(DesignBotResults designBotResult) {
		if (designBotResults.contains(designBotResult)) {
			designBotResults.remove(designBotResult);
		}
		designBotResults.add(designBotResult);
	}
	
	public ArchitectureResults getActualArchitectureResult() {
		return actualArchitectureResult;
	}

	public void setActualArchitectureResult(ArchitectureResults actualArchitectureResult) {
		this.actualArchitectureResult = actualArchitectureResult;
	}
	
}
