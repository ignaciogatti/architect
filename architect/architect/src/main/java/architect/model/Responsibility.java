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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OrderBy;

@Entity
@Table(name="responsibility")
@XmlRootElement(name="responsibility")
@XmlAccessorType(XmlAccessType.NONE)
public class Responsibility implements Element, Serializable {

	public final static double DEFAULT_COMPLEXITY_COST = 0.5;
	public final static long DEFAULT_EXECUTION_TIME = 10;
	
	private final static long serialVersionUID = 2560792558119704824L;
	
	@XmlAttribute
	@Id
	@GenericGenerator(name="responsibility_id_gen" , strategy="architect.model.IdIncrementGenerator")
	@GeneratedValue(generator="responsibility_id_gen")
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
	
	@XmlAttribute
	@Basic
    @Column(name ="complexity", nullable=false)
	private Double complexity;
	
	@XmlAttribute
	@Basic
    @Column(name ="executionTime", nullable=false)
	private Long executionTime;
	
	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    @JoinColumn(name="id_module")
	@Fetch(FetchMode.SELECT)
	private Module module;
	
	@XmlInverseReference(mappedBy="responsibilities")
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_architecture", nullable=false)
	@Fetch(FetchMode.SELECT)
	private Architecture architecture;
	
	@OneToMany(mappedBy = "parentResponsibility",fetch = FetchType.LAZY , cascade = {javax.persistence.CascadeType.ALL}, orphanRemoval=true) 
	@Cascade({org.hibernate.annotations.CascadeType.ALL}) 
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id")
	private Set<Dependency> dependenciesAsParent = new LinkedHashSet<Dependency>(0);
	
	@OneToMany(mappedBy = "childResponsibility",fetch = FetchType.LAZY , cascade = {javax.persistence.CascadeType.ALL}, orphanRemoval=true) 
	@Cascade({org.hibernate.annotations.CascadeType.ALL}) 
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id")
	private Set<Dependency> dependenciesAsChild = new LinkedHashSet<Dependency>(0);
	
	@XmlElement(name="scenario")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "responsibility_scenario", 
		joinColumns = { @JoinColumn(name ="id_responsibility") }, 
		inverseJoinColumns = { @JoinColumn(name = "id_scenario") })
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id_scenario")
	private Set<Scenario> scenarios = new LinkedHashSet<Scenario>(0);

	public Responsibility() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Architecture getArchitecture() {
		return this.architecture;
	}

	public void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
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

	public Set<Dependency> getDependenciesAsParent() {
		return this.dependenciesAsParent;
	}

	public void setDependenciesAsParent(Set<Dependency> dependenciesAsParent) {
		this.dependenciesAsParent = dependenciesAsParent;
	}

	public Set<Dependency> getDependenciesAsChild() {
		return this.dependenciesAsChild;
	}

	public void setDependenciesAsChild(Set<Dependency> dependenciesAsChild) {
		this.dependenciesAsChild = dependenciesAsChild;
	}

	public Set<Scenario> getScenarios() {
		return scenarios;
	}

	public void setScenarios(Set<Scenario> scenarios) {
		this.scenarios = scenarios;
	}
	
	public boolean containsScenario(Scenario s) {
		for(Scenario scenario : scenarios) {
			if(scenario.getId().equals(s.getId()))
				return true;
		}
		return false;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Double getComplexity() {
		return complexity;
	}

	public void setComplexity(Double complexity) {
		this.complexity = complexity;
	}
	
	public Long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof Responsibility) {
			Long id = ((Responsibility) o).getId();
			if (this.id!=null && id!=null && this.id.equals(id))
				return true;
			else 
				return false;
		} else
			return false;
	}
	
	@Override
	public Responsibility clone() {
		Responsibility newResponsibility = new Responsibility();
		if (id!=null) {
			newResponsibility.setId(new Long(id.longValue()));
		}
		if (name!=null) {
			newResponsibility.setName(new String(name));
		}
		if (description!=null) {
			newResponsibility.setDescription(new String(description));
		}
		if (complexity!=null) {
			newResponsibility.setComplexity(new Double(complexity));
		}
		if (executionTime!=null) {
			newResponsibility.setExecutionTime(new Long(executionTime));
		}
		return newResponsibility;		
	}
	
	public Responsibility cloneEmptyId() {
		Responsibility newResponsibility = new Responsibility();
		if (name!=null) {
			newResponsibility.setName(new String(name));
		}
		if (description!=null) {
			newResponsibility.setDescription(new String(description));
		}
		if (complexity!=null) {
			newResponsibility.setComplexity(new Double(complexity));
		}
		if (executionTime!=null) {
			newResponsibility.setExecutionTime(new Long(executionTime));
		}
		newResponsibility.setModule(module);
		newResponsibility.setArchitecture(architecture);
		newResponsibility.setScenarios(scenarios);
		return newResponsibility;		
	}

}
