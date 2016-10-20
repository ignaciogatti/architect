package architect.engine.architecture;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import architect.model.ElementChange;

@XmlRootElement(name="elementChangeList")
@XmlAccessorType(XmlAccessType.NONE)
public class ElementChangeList {

	@XmlElement(name="change")
	private List<ElementChange> changes;

	public List<ElementChange> getChanges() {
		return changes;
	}

	public void setChanges(List<ElementChange> changes) {
		this.changes = changes;
	}
	
	
}
