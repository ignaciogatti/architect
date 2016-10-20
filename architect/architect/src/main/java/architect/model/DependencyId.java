package architect.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class DependencyId implements Serializable {

	private static final long serialVersionUID = -2267467898083266804L;
	
	@XmlAttribute
	private Long parent;
	
	@XmlAttribute
	private Long child;

	public DependencyId() {
	}

	public DependencyId(Long parent, Long child) {
		this.parent = parent;
		this.child = child;
	}

	public Long getParent() {
		return this.parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Long getChild() {
		return this.child;
	}

	public void setChild(Long child) {
		this.child = child;
	}

	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof DependencyId) {
			Long child = ((DependencyId) o).getChild();
			Long parent = ((DependencyId) o).getParent();
			if (this.child!=null && this.parent!=null && child!=null && parent!=null && this.child.equals(child) && this.parent.equals(parent))
				return true;
			else 
				return false;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + (getParent() == null ? 0 : this.getParent().hashCode());
		result = 37 * result + (getChild() == null ? 0 : this.getChild().hashCode());
		return result;
	}
	
	@Override
	public DependencyId clone() {
		DependencyId newDependencyId = new DependencyId();
		if (child!=null) 
			newDependencyId.setChild(new Long(child));
		if (parent!=null) 
			newDependencyId.setParent(new Long(parent));
		return newDependencyId;
	}
	
	public String toString(){
		return "R" + parent + " " + "R" + child;
	}

}
