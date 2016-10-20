package architect.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;

@Entity
@Table(name="module")
@XmlRootElement(name="module")
@XmlAccessorType(XmlAccessType.NONE)
public class Module implements Element, Serializable {

	private static final long serialVersionUID = -8512866853139268500L;
	
	@XmlAttribute
	@Id
	@GenericGenerator(name="module_id_gen" , strategy="architect.model.IdIncrementGenerator")
	@GeneratedValue(generator="module_id_gen")
	@Column(name = "id", unique = true, nullable = false, length = 20)
	private Long id;
	
	@XmlAttribute
	@Basic
    @Column(name ="name")
	private String name;
	
	@XmlAttribute
	@Basic
    @Column(name ="description")
	private String description;
	
	@XmlInverseReference(mappedBy="modules")
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_architecture", nullable=false)
	@Fetch(FetchMode.SELECT)
	private Architecture architecture;
	
	@OneToMany(mappedBy="module",fetch = FetchType.LAZY, cascade = {javax.persistence.CascadeType.ALL}, orphanRemoval=true) 
	@Cascade({org.hibernate.annotations.CascadeType.ALL}) 
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id")
	private Set<Responsibility> responsibilities = new LinkedHashSet<Responsibility>(0);

	public Module() {
	}

	public Module(String name, Set<Responsibility> responsibilities) {
		this.name = name;
		this.responsibilities = responsibilities;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Responsibility> getResponsibilities() {
		return this.responsibilities;
	}

	public void setResponsibilities(Set<Responsibility> responsibilities) {
		this.responsibilities = responsibilities;
	}

	public Architecture getArchitecture() {
		return architecture;
	}

	public void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof Module) {
			Long id = ((Module) o).getId();
			if (this.id!=null && id!=null && this.id.equals(id))
				return true;
			else 
				return false;
		} else
			return false;
	}
	
	@Override
	public Module clone() {
		Module newModule = new Module();
		if (id!=null) {
			newModule.setId(new Long(id.longValue()));
		}
		if (name!=null) {
			newModule.setName( new String(name.toString()));
		}
		if (description!=null) {
			newModule.setDescription( new String(description.toString()));
		}
		return newModule;
	}
	
	public Module cloneEmptyId() {
		Module newModule = new Module();
		if (name!=null) {
			newModule.setName(new String(name.toString()));
		}
		if (description!=null) {
			newModule.setDescription( new String(description.toString()));
		}
		newModule.setArchitecture(architecture);
		return newModule;
	}


}
