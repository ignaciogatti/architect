package architect.graph.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class mxChildChange extends mxChange {

	@XmlAttribute
	private String parent; 
			
	@XmlAttribute
	private Integer index;
	
	@XmlAttribute
	private String previous;
	
	@XmlAttribute
	private String child;
	
	@XmlElements({
	    @XmlElement(name = "Container", type = Container.class),
	    @XmlElement(name = "Shape", type = Shape.class)
	})
	private mxElement element;
	
	public mxChildChange () {
		
	}
	
	public mxChildChange (String parent, Integer index) {
		this.parent = parent;
		this.index = index;
	}

	public mxElement getElement() {
		return element;
	}

	public void setElement(mxElement element) {
		this.element = element;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = child;
	}
	
	
	
}
