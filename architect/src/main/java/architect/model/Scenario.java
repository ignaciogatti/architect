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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;

@Entity
@Table(name="scenario")
@XmlRootElement(name="scenario")
@XmlAccessorType(XmlAccessType.NONE)
public class Scenario implements Element, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Transient
	private Long period = this.getMeasure();
	
	
	
	public Long getPeriod() {
		if (period == null){
			period = this.getMeasure();
		}
		return period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}

	@XmlAttribute
	@Id
	@GenericGenerator(name="scenario_id_gen" , strategy="architect.model.IdIncrementGenerator")
	@GeneratedValue(generator="scenario_id_gen")
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
    @Column(name ="stimulus")
	private String stimulus;
	
	@XmlAttribute
	@Basic
    @Column(name ="source")
	private String source;
	
	@XmlAttribute
	@Basic
    @Column(name ="enviroment")
	private String enviroment;
	
	@XmlAttribute
	@Basic
    @Column(name ="artifact")
	private String artifact;
	
	@XmlAttribute
	@Basic
    @Column(name ="response")
	private Integer response;
	
	@XmlAttribute
	@Basic
    @Column(name ="measure")
	private Long measure;
	
	@XmlAttribute
	@Basic
    @Column(name ="priority")
	private Long priority;
	
	@XmlInverseReference(mappedBy="scenarios")
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_architecture", nullable=false)
	@Fetch(FetchMode.SELECT)
	private Architecture architecture;
	
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_quality_attribute", nullable=false)
	@Fetch(FetchMode.SELECT)
	private QualityAttribute qualityAttribute;
	
	@XmlInverseReference(mappedBy="scenarios")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "responsibility_scenario", 
		joinColumns = { @JoinColumn(name ="id_scenario") }, 
		inverseJoinColumns = { @JoinColumn(name = "id_responsibility") })
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id_responsibility")
	private Set<Responsibility> responsibilities = new LinkedHashSet<Responsibility>(0);

	@OneToMany(mappedBy = "scenario",fetch = FetchType.LAZY , cascade = {javax.persistence.CascadeType.ALL}, orphanRemoval=true) 
	@Cascade({org.hibernate.annotations.CascadeType.ALL}) 
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id")
	private Set<ArchitectureAnalysis> architectureAnalysis = new LinkedHashSet<ArchitectureAnalysis>(0);
	
	public Scenario() {
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

	public void setName(String n) {
		this.name = n;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String d) {
		this.description = d;
	}

	public String getStimulus() {
		return this.stimulus;
	}

	public void setStimulus(String s) {
		this.stimulus = s;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String s) {
		this.source = s;
	}

	public String getEnviroment() {
		return this.enviroment;
	}

	public void setEnviroment(String e) {
		this.enviroment = e;
	}

	public String getArtifact() {
		return this.artifact;
	}

	public void setArtifact(String a) {
		this.artifact = a;
	}

	public ResponseType getResponse() {
		return ResponseType.values()[this.response];
	}

	public void setResponse(ResponseType r) {
		this.response = r.ordinal();
	}

	public Long getMeasure() {
		return this.measure;
	}

	public void setMeasure(Long m) {
		this.measure = m;
	}

	public Long getPriority() {
		return this.priority;
	}

	public void setPriority(Long p) {
		this.priority = p;
	}

	public Architecture getArchitecture() {
		return this.architecture;
	}

	public void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}

	public Set<Responsibility> getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(Set<Responsibility> responsibilities) {
		this.responsibilities = responsibilities;
	}

	public QualityAttribute getQualityAttribute() {
		return qualityAttribute;
	}

	public void setQualityAttribute(QualityAttribute qualityAttribute) {
		this.qualityAttribute = qualityAttribute;
	}
	
	public Set<ArchitectureAnalysis> getArchitectureAnalysis() {
		return architectureAnalysis;
	}

	public void setArchitectureAnalysis(Set<ArchitectureAnalysis> architectureAnalysis) {
		this.architectureAnalysis = architectureAnalysis;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.id).toHashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof Scenario) {
			Long id = ((Scenario) o).getId();
			if (this.id!=null && id!=null && this.id.equals(id))
				return true;
			else 
				return false;
		} else
			return false;
	}
	
	@Override
	public Scenario clone() {
		Scenario newScenario = new Scenario();
		if (id!=null)
			newScenario.setId(new Long(id.longValue()));
		if (name!=null)
			newScenario.setName(new String(name));
		if (description!=null)
			newScenario.setDescription(new String(description));
		if (stimulus!=null)
			newScenario.setStimulus(new String(stimulus));
		if (source!=null)
			newScenario.setSource(new String(source));
		if (enviroment!=null)
			newScenario.setEnviroment(new String(enviroment));
		if (artifact!=null)
			newScenario.setArtifact(new String(artifact));
		if (response!=null)
			newScenario.setResponse(ResponseType.values()[response]);
		if (measure!=null)
			newScenario.setMeasure(new Long(measure));
		if (priority!=null)
			newScenario.setPriority(new Long(priority));
		if (qualityAttribute!=null)
			newScenario.setQualityAttribute(qualityAttribute.clone());
		if (period != null)
			newScenario.setPeriod(new Long(period));
		return newScenario;
	}
	
	public enum ResponseType {
		
		MILLISECONDS("Milliseconds"),	//0
		HOURS("Hours");					//1
		
		private String label;
		
		ResponseType(String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return this.label;
		}
		
	}

	public Set<Scenario> getSharedReponsibilitiesScenarios(
			Set<Scenario> scenariosList) {
		Set<Scenario> sharedReponsibilitiesScenarios = new LinkedHashSet<Scenario>();
		for (Responsibility responsibility : this.getResponsibilities()) {
			for (Scenario scenario : scenariosList) {
				if (scenario.getResponsibilities().contains(responsibility)) {
					if (!sharedReponsibilitiesScenarios.contains(scenario)) {
						sharedReponsibilitiesScenarios.add(scenario);
					}
				}
			}
		}
		return sharedReponsibilitiesScenarios;
	}

}
