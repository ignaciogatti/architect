package architect.graph.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

@XmlAccessorType(XmlAccessType.NONE)
public class Edit {
	
	@XmlElements({
	    @XmlElement(name = "mxChildChange", type = mxChildChange.class),
	    @XmlElement(name = "mxGeometryChange", type = mxGeometryChange.class)
	})
	private mxChange change;

	@XmlElement(name="mxTerminalChange")
	private List<mxTerminalChange> terminalChanges;
	
	public Edit() {
		
	}
	
	public mxChange getChange() {
		return change;
	}

	public void setChange(mxChange change) {
		this.change = change;
	}

	public List<mxTerminalChange> getTerminalChanges() {
		return terminalChanges;
	}

	public void setTerminalChanges(List<mxTerminalChange> terminalChanges) {
		this.terminalChanges = terminalChanges;
	}
	
}
