package architect.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;

@Entity
@Table(name="architecture")
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Architecture implements Element, Serializable {

	private static final long serialVersionUID = 1736559376456399060L;
	
	@XmlAttribute
	@Id
	@GenericGenerator(name="arch_id_gen" , strategy="architect.model.IdIncrementGenerator")
	@GeneratedValue(generator="arch_id_gen")
	@Column(name = "id", unique = true, nullable = false, length = 20)
	private Long id;
	
	@XmlAttribute
	@Basic
    @Column(name ="name", nullable=false)
	private String name;
	
	@XmlInverseReference(mappedBy="architectures")
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_group", nullable=false)
	@Fetch(FetchMode.SELECT)
	private Group group;
	
	@Basic
    @Column(name ="blocked", nullable=false)
	private Boolean blocked;
	
	@Basic
    @Column(name ="block_reason", nullable=true)
	private String blockReason;
	
	@XmlElement(name="responsibility")
	@OneToMany(mappedBy = "architecture",fetch = FetchType.LAZY , cascade = {javax.persistence.CascadeType.ALL}, orphanRemoval=true) 
	@Cascade({org.hibernate.annotations.CascadeType.ALL}) 
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id")
	private Set<Responsibility> responsibilities = new LinkedHashSet<Responsibility>(0);
	
	@XmlElement(name="module")
	@OneToMany(mappedBy = "architecture",fetch = FetchType.LAZY , cascade = {javax.persistence.CascadeType.ALL}, orphanRemoval=true) 
	@Cascade({org.hibernate.annotations.CascadeType.ALL}) 
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id")
	private Set<Module> modules = new LinkedHashSet<Module>(0);
	
	@XmlElement(name="scenario")
	@OneToMany(mappedBy = "architecture",fetch = FetchType.LAZY , cascade = {javax.persistence.CascadeType.ALL}, orphanRemoval=true) 
	@Cascade({org.hibernate.annotations.CascadeType.ALL}) 
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id")
	private Set<Scenario> scenarios = new LinkedHashSet<Scenario>(0);
	
	@XmlElement(name="dependency")
	@OneToMany(mappedBy = "architecture",fetch = FetchType.LAZY , cascade = {javax.persistence.CascadeType.ALL}, orphanRemoval=true) 
	@Cascade({org.hibernate.annotations.CascadeType.ALL}) 
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id")
	private Set<Dependency> dependencies = new LinkedHashSet<Dependency>(0);

	@OneToMany(mappedBy = "architecture",fetch = FetchType.LAZY , cascade = {javax.persistence.CascadeType.ALL}, orphanRemoval=true) 
	@Cascade({org.hibernate.annotations.CascadeType.ALL}) 
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id")
	private Set<DesignBot> designBots = new LinkedHashSet<DesignBot>(0);
	
	public Architecture() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	public String getBlockReason() {
		return blockReason;
	}

	public void setBlockReason(String blockReason) {
		this.blockReason = blockReason;
	}

	public Set<Responsibility> getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(Set<Responsibility> responsibilities) {
		this.responsibilities = responsibilities;
	}

	public Set<Module> getModules() {
		return modules;
	}

	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}

	public Set<Scenario> getScenarios() {
		return scenarios;
	}
	
	public Set<Scenario> getPerformanceScenarios() {
		Set<Scenario> performanceScenarios = new LinkedHashSet<Scenario>();
		for (Scenario scenario : scenarios) {
			if (scenario.getQualityAttribute().getName().equals("Performance")) {
				performanceScenarios.add(scenario);
			}
		}
		return performanceScenarios;
	}

	public void setScenarios(Set<Scenario> scenarios) {
		this.scenarios = scenarios;
	}
	
	public Set<Dependency> getDependencies() {
		return this.dependencies;
	}
	
	public void setDependencies(Set<Dependency> dependencies) {
		this.dependencies = dependencies;
	}
	
	public boolean isBlocked() {
		return this.blocked;
	}

	public Set<DesignBot> getDesignBots() {
		return designBots;
	}

	public void setDesignBots(Set<DesignBot> designBots) {
		this.designBots = designBots;
	}
	
	@Override
	public Architecture clone() {
		Architecture newArchitecture = new Architecture();

		//Clone Modules
		Set<Module> newModules = new LinkedHashSet<Module>();		
		for(Module oldModule : modules) {
			Module newModule = oldModule.clone();
			newModule.setArchitecture(newArchitecture);
			newModules.add(newModule);
		}
		
		//Clone Responsibilities
		Set<Responsibility> newResponsibilities = new LinkedHashSet<Responsibility>();
		for(Responsibility oldResponsibility : responsibilities) {
			Responsibility newResponsibility = oldResponsibility.clone();
			newResponsibility.setArchitecture(newArchitecture);
			Module oldModule = oldResponsibility.getModule();
			if (oldModule!=null) {
				List<Module> modules = new ArrayList<Module>(newModules);
				Module newModule = modules.get(modules.indexOf(oldModule));
				newResponsibility.setModule(newModule);
				newModule.getResponsibilities().add(newResponsibility);
			}
			newResponsibilities.add(newResponsibility);
		}
		List<Responsibility> newResponsibilitiesList = new ArrayList<Responsibility>(newResponsibilities);

		
		//Clone Scenarios
		Set<Scenario> newScenarios = new LinkedHashSet<Scenario>();
		for (Scenario oldScenario : this.scenarios) {
			Scenario newScenario = oldScenario.clone();
			Set<Responsibility> newScenarioResponsibilities = new LinkedHashSet<Responsibility>(0);
			for (Responsibility resp : oldScenario.getResponsibilities()) {
				Responsibility newResponsibility;
				if (newResponsibilitiesList.indexOf(resp) != -1) {
					newResponsibility = newResponsibilitiesList.get(newResponsibilitiesList.indexOf(resp));
					newResponsibility.getScenarios().add(newScenario);
					newScenarioResponsibilities.add(newResponsibility);
				} else {
					System.out.println("no esta");
				}
			}
			newScenario.setResponsibilities(newScenarioResponsibilities);
			newScenario.setArchitecture(newArchitecture);
			newScenarios.add(newScenario);
		}
		
		//Clone DependeciesList
		Set<Dependency> dependencies = new LinkedHashSet<Dependency>(0);
		for(Responsibility oldResponsibility : responsibilities) {
//			Responsibility newResponsibility = newResponsibilitiesList.get(newResponsibilitiesList.indexOf(oldResponsibility));
			for(Dependency oldDep : oldResponsibility.getDependenciesAsChild()) {
				Dependency newDependency = oldDep.clone();
				newDependency.setArchitecture(newArchitecture);
				dependencies.add(newDependency);
				//newResponsibility.getDependenciesAsChild().add(newDependency);
				if (oldDep.getChildResponsibility()!=null) {
					Responsibility childResponsibility = newResponsibilitiesList.get(newResponsibilitiesList.indexOf(oldDep.getChildResponsibility()));
					childResponsibility.getDependenciesAsChild().add(newDependency);
					newDependency.setChildResponsibility(childResponsibility);
				}
				if (oldDep.getParentResponsibility()!=null) {
					Responsibility parentResponsibility = newResponsibilitiesList.get(newResponsibilitiesList.indexOf(oldDep.getParentResponsibility()));
					parentResponsibility.getDependenciesAsParent().add(newDependency);
					newDependency.setParentResponsibility(parentResponsibility);
				}
			}	
		}
				
		if (id!=null)
			newArchitecture.setId(id);
		if (name!=null)
			newArchitecture.setName(name);
		if (group!=null) {
			newArchitecture.setGroup(group);
		}
		newArchitecture.setModules(newModules);
		newArchitecture.setResponsibilities(newResponsibilities);
		newArchitecture.setScenarios(newScenarios);
		newArchitecture.setDependencies(dependencies);
		return newArchitecture;
	}

}
