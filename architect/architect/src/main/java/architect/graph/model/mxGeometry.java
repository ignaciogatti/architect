package architect.graph.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class mxGeometry {

	@XmlAttribute
	private Integer x;
	
	@XmlAttribute
	private Integer y;
	
	@XmlAttribute
	private Integer width;
	
	@XmlAttribute
	private Integer height;
	
	@XmlAttribute
	private Integer relative;
	
	@XmlAttribute
	private String as;
	
	public mxGeometry() {
		
	}
	
	public mxGeometry(Integer x, Integer y, Integer width, Integer height, String as) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.as = as;
	}
	
	public mxGeometry(Integer relative, String as) {
		this.relative = relative;
		this.as = as;
	}
	
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public String getAs() {
		return as;
	}
	public void setAs(String as) {
		this.as = as;
	}

	public Integer getRelative() {
		return relative;
	}

	public void setRelative(Integer relative) {
		this.relative = relative;
	}
}
