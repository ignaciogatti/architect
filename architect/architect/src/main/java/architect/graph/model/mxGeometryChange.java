package architect.graph.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class mxGeometryChange extends mxChange {

	@XmlAttribute
	private Integer cell;
	
	@XmlElement(name="mxGeometry")
	private mxGeometry geometry;

	public mxGeometryChange() {
		
	}
	
	public mxGeometryChange(Integer cell) {
		this.cell = cell;
	}
	
	public Integer getCell() {
		return cell;
	}

	public void setCell(Integer cell) {
		this.cell = cell;
	}

	public mxGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(mxGeometry geometry) {
		this.geometry = geometry;
	}
	
	
}
