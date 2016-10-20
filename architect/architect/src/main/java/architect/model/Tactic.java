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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;

import architect.engine.TacticExecutor;

@Entity
@Table(name="tactic")
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Tactic implements Serializable {

	private static final long serialVersionUID = -1130344587681877178L;
	
	@XmlAttribute
	@Id
	@GenericGenerator(name="tactic_id_gen" , strategy="architect.model.IdIncrementGenerator")
	@GeneratedValue(generator="tactic_id_gen")
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
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tactic_designbot", 
		joinColumns = { @JoinColumn(name ="id_tactic") }, 
		inverseJoinColumns = { @JoinColumn(name = "id_design_bot") })
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id_design_bot")
	private Set<DesignBot> designBots = new LinkedHashSet<DesignBot>(0);

	public Tactic() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public QualityAttribute getQualityAttribute() {
		return qualityAttribute;
	}

	public void setQualityAttribute(QualityAttribute qualityAttribute) {
		this.qualityAttribute = qualityAttribute;
	}

	public Set<DesignBot> getDesignBots() {
		return designBots;
	}

	public void setDesignBots(Set<DesignBot> designBots) {
		this.designBots = designBots;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof Tactic) {
			Long id = ((Tactic) o).getId();
			if (this.id!=null && id!=null && this.id.equals(id))
				return true;
			else 
				return false;
		} else
			return false;
	}
	
	public TacticExecutor getTacticExecutor() {
		try {
			String className = this.name.replaceAll(" ", "");
			Class<?> theClass = Class.forName("architect.engine.attribute." + this.qualityAttribute.getName().toLowerCase() + "." +className);
			TacticExecutor tacticExecutor = (TacticExecutor) theClass
					.newInstance();
			return tacticExecutor;
		} catch (ClassNotFoundException ex) {
			System.err.println(ex);
		} catch (InstantiationException ex) {
			System.err.println(ex);
		} catch (IllegalAccessException ex) {
			System.err.println(ex);
		}
		return null;
	}

}
