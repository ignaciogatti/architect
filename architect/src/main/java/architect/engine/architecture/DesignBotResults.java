package architect.engine.architecture;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import architect.model.Architecture;
import architect.model.ArchitectureAnalysis;

@XmlRootElement(name="designBotResults")
@XmlAccessorType(XmlAccessType.NONE)
public class DesignBotResults {
	
	@XmlElement
	private ArchitectureAnalysis architectureAnalysis;
	
	@XmlElement
	private ArchitectureResults architectureResults;
	
	private Architecture architecture;
	
	private List<TacticChange> changes = new ArrayList<TacticChange>();

	public List<TacticChange> getChanges() {
		return changes;
	}

	public void setChanges(List<TacticChange> changes) {
		this.changes = changes;
	}

	public ArchitectureAnalysis getArchitectureAnalysis() {
		return architectureAnalysis;
	}

	public void setArchitectureAnalysis(ArchitectureAnalysis architectureAnalysis) {
		this.architectureAnalysis = architectureAnalysis;
	}

	public Architecture getArchitecture() {
		return architecture;
	}

	public void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}

	public ArchitectureResults getArchitectureResults() {
		return architectureResults;
	}

	public void setArchitectureResults(ArchitectureResults architectureResults) {
		this.architectureResults = architectureResults;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof DesignBotResults) {
			Long id = ((DesignBotResults) o).getArchitectureAnalysis().getId();
			if (this.getArchitectureAnalysis()!=null && this.getArchitectureAnalysis().getId()!=null && id!=null && this.getArchitectureAnalysis().getId().equals(id))
				return true;
			else 
				return false;
		} else
			return false;
	}

}
