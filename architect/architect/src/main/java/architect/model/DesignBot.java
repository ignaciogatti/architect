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

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;

@Entity
@Table(name="design_bot")
@XmlRootElement(name="designBot")
@XmlAccessorType(XmlAccessType.NONE)
public class DesignBot implements Element, Serializable {

	private static final long serialVersionUID = -8483750761897873915L;
	
	@XmlAttribute
	@Id
	@GenericGenerator(name="designbot_id_gen" , strategy="architect.model.IdIncrementGenerator")
	@GeneratedValue(generator="designbot_id_gen")
	@Column(name = "id", unique = true, nullable = false, length = 20)
	private Long id;
	
	@XmlAttribute
	@Basic
    @Column(name ="name")
	private String name;
	
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_quality_attribute", nullable=false)
	@Fetch(FetchMode.SELECT)
	private QualityAttribute qualityAttribute;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_architecture", nullable=false)
	@Fetch(FetchMode.SELECT)
	private Architecture architecture;
	
	@XmlElement
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tactic_designbot", 
		joinColumns = { @JoinColumn(name ="id_design_bot") }, 
		inverseJoinColumns = { @JoinColumn(name = "id_tactic") })
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id_tactic")
	private Set<Tactic> tactics = new LinkedHashSet<Tactic>(0);

	@OneToMany(mappedBy = "designBot",fetch = FetchType.LAZY , cascade = {javax.persistence.CascadeType.ALL}, orphanRemoval=true) 
	@Cascade({org.hibernate.annotations.CascadeType.ALL}) 
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id")
	private Set<ArchitectureAnalysis> architectureAnalysis = new LinkedHashSet<ArchitectureAnalysis>(0);
	
	public DesignBot() {
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

	public QualityAttribute getQualityAttribute() {
		return qualityAttribute;
	}

	public void setQualityAttribute(QualityAttribute qualityAttribute) {
		this.qualityAttribute = qualityAttribute;
	}

	public Architecture getArchitecture() {
		return architecture;
	}

	public void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}

	public Set<Tactic> getTactics() {
		return tactics;
	}

	public void setTactics(Set<Tactic> tactics) {
		this.tactics = tactics;
	}
	
	public Set<ArchitectureAnalysis> getArchitectureAnalysis() {
		return architectureAnalysis;
	}

	public void setArchitectureAnalysis(Set<ArchitectureAnalysis> architectureAnalysis) {
		this.architectureAnalysis = architectureAnalysis;
	}

	public boolean containsTactic(Tactic t) {
		for(Tactic tactic : tactics) {
			if(tactic.getId().equals(t.getId()))
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.id).toHashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof DesignBot) {
			Long id = ((DesignBot) o).getId();
			if (this.id!=null && id!=null && this.id.equals(id))
				return true;
			else 
				return false;
		} else
			return false;
	}
	
	@Override
	public DesignBot clone() {
		DesignBot designBot = new DesignBot();
		if (id!=null) {
			designBot.setId(new Long(id.longValue()));
		}
		if (name!=null) {
			designBot.setName(new String(name));
		}
		if (qualityAttribute!=null) {
			designBot.setQualityAttribute(qualityAttribute.clone());
		}
		return designBot;		
	}
	
}
