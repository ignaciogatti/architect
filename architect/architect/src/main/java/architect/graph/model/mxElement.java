package architect.graph.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class mxElement {
	
	@XmlAttribute
	private String label;
	
	@XmlAttribute
	private String href;
	
	@XmlAttribute
	private String id;

	@XmlElement(name="mxCell")
	private mxCell cell;
	
	public mxElement() {
		
	}
	
	public mxElement(String id, String label, String href) {
		this.id = id;
		this.label = label;
		this.href = href;
	}

	public mxCell getCell() {
		return cell;
	}

	public void setCell(mxCell cell) {
		this.cell = cell;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}
