package architect.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;

@Entity
@Table(name=" dependency")
@XmlRootElement(name="dependency")
@XmlAccessorType(XmlAccessType.NONE)
public class Dependency implements Element, Serializable {
	
	public final static double DEFAULT_COUPLING_COST = 0.5;

	private static final long serialVersionUID = 7768592875805583418L;
	
	@XmlElement
	@Id
	private DependencyId id;
	
	@XmlAttribute
	@Basic
    @Column(name ="couplingcost", nullable=false)
	private Double couplingcost;
	
	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
	@JoinColumn(name = "parent", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	private Responsibility parentResponsibility;
	
	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
	@JoinColumn(name = "child", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	private Responsibility childResponsibility;
	
	@XmlInverseReference(mappedBy="dependencies")
	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    @JoinColumn(name="id_architecture", nullable=false)
	@Fetch(FetchMode.SELECT)
	private Architecture architecture;

	public Dependency() {
	}

	public Dependency(DependencyId id, Responsibility parentResponsibility,
			Responsibility childResponsibility) {
		this.id = id;
		this.parentResponsibility = parentResponsibility;
		this.childResponsibility = childResponsibility;
	}

	public Dependency(DependencyId id, Responsibility parentResponsibility,
			Responsibility childResponsibility, Double couplingcost) {
		this.id = id;
		this.parentResponsibility = parentResponsibility;
		this.childResponsibility = childResponsibility;
		this.couplingcost = couplingcost;
	}

	public Dependency(DependencyId dependencyId) {
		this.id = dependencyId;
	}

	public DependencyId getId() {
		return this.id;
	}

	public void setId(DependencyId id) {
		this.id = id;
	}

	public Responsibility getParentResponsibility() {
		return this.parentResponsibility;
	}

	public void setParentResponsibility(Responsibility parentResponsibility) {
		this.parentResponsibility = parentResponsibility;
	}

	public Responsibility getChildResponsibility() {
		return this.childResponsibility;
	}

	public void setChildResponsibility(Responsibility childResponsibility) {
		this.childResponsibility = childResponsibility;
	}

	public Double getCouplingcost() {
		return this.couplingcost;
	}

	public void setCouplingcost(Double couplingcost) {
		this.couplingcost = couplingcost;
	}
	
	public Architecture getArchitecture() {
		return architecture;
	}

	public void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.id).toHashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof Dependency) {
			DependencyId id = ((Dependency) o).getId();
			if (this.id!=null && id!=null && this.id.equals(id))
				return true;
			else 
				return false;
		} else
			return false;
	}

	@Override
	public Dependency clone() {
		Dependency newDependency = new Dependency();
		if (id!=null)
			newDependency.setId(id.clone());
		if (this.couplingcost!=null)
			newDependency.setCouplingcost(new Double(this.couplingcost));
		return newDependency;
	}
	
}
