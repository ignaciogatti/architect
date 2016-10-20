package architect.graph.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class mxCell {
	
	@XmlAttribute
	private String style; 
	
	@XmlAttribute
	private Integer vertex; 
	
	@XmlAttribute
	private Integer connectable;
	
	@XmlAttribute
	private Integer edge;
	
	@XmlAttribute
	private String parent;
	
	@XmlAttribute
	private String source; 
			
	@XmlAttribute
	private String target;
	
	@XmlElement(name="mxGeometry")
	private mxGeometry geometry;
	
	public mxCell() {
		
	}
	
	public mxCell(String style, Integer verte, Integer connectable, String parent) {
		this.style = style;
		this.vertex = verte;
		this.connectable = connectable;
		this.parent = parent;
	}
	
	public mxCell(Integer edge, String parent, String source, String target) {
		this.edge = edge;
		this.source = source;
		this.target = target;
		this.parent = parent;
	}

	public mxGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(mxGeometry geometry) {
		this.geometry = geometry;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Integer getVertex() {
		return vertex;
	}

	public void setVertex(Integer vertex) {
		this.vertex = vertex;
	}

	public Integer getConnectable() {
		return connectable;
	}

	public void setConnectable(Integer connectable) {
		this.connectable = connectable;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Integer getEdge() {
		return edge;
	}

	public void setEdge(Integer edge) {
		this.edge = edge;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	

}
