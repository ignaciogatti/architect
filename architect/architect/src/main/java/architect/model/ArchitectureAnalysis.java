package architect.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;

@Entity
@Table(name="architecture_analysis")
@XmlRootElement(name="architectureAnalysis")
@XmlAccessorType(XmlAccessType.NONE)
public class ArchitectureAnalysis implements Element, Serializable {

	private static final long serialVersionUID = -4365396060520022853L;
	
	@XmlAttribute
	@Id
	@GenericGenerator(name="architecture_analysis_id_gen" , strategy="architect.model.IdIncrementGenerator")
	@GeneratedValue(generator="architecture_analysis_id_gen")
	@Column(name = "id", unique = true, nullable = false, length = 20)
	private Long id;
	
	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    @JoinColumn(name="id_architecture", nullable=false)
	@Fetch(FetchMode.SELECT)
	private Architecture architecture;
	
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    @JoinColumn(name="id_scenario", nullable=false)
	@Fetch(FetchMode.SELECT)
	private Scenario scenario;
	
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    @JoinColumn(name="id_design_bot", nullable=false)
	@Fetch(FetchMode.SELECT)
	private DesignBot designBot;
	
	@XmlAttribute
	@Basic
    @Column(name ="enable")
	private Boolean enable;

	public ArchitectureAnalysis() {
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

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public DesignBot getDesignBot() {
		return designBot;
	}

	public void setDesignBot(DesignBot designBot) {
		this.designBot = designBot;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.id).toHashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof ArchitectureAnalysis) {
			Long id = ((ArchitectureAnalysis) o).getId();
			if (this.id!=null && id!=null && this.id.equals(id))
				return true;
			else 
				return false;
		} else
			return false;
	}
	
	@Override
	public ArchitectureAnalysis clone() {
		ArchitectureAnalysis architectureAnalysis = new ArchitectureAnalysis();
		if (id!=null) {
			architectureAnalysis.setId(new Long(id.longValue()));
		}
		if (enable!=null) {
			architectureAnalysis.setEnable(new Boolean(enable));
		}
		return architectureAnalysis;		
	}

}
