package architect.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.hibernate.annotations.GenericGenerator;

import architect.engine.DesignBotExecutor;

@Entity
@Table(name="quality_attribute")
@XmlAccessorType(XmlAccessType.NONE)
public class QualityAttribute implements Serializable {

	private static final long serialVersionUID = 8511855854862382746L;
	
	@XmlAttribute
	@Id
	@GenericGenerator(name="qa_id_gen" , strategy="architect.model.IdIncrementGenerator")
	@GeneratedValue(generator="qa_id_gen")
	@Column(name = "id", unique = true, nullable = false, length = 20)
	private Long id;
	
	@XmlAttribute
	@Basic
    @Column(name ="name")
	private String name;

	public QualityAttribute() {
	}

	public Long getId() {
		return this.id;
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
	
	public DesignBotExecutor getDesignBotExecutor() {
		try {
			String className = this.getName() + "Dbot";
			Class<?> theClass = Class.forName("architect.engine.attribute." + this.getName().toLowerCase() + "." + className);
			DesignBotExecutor designBotExecutor = (DesignBotExecutor) theClass.newInstance();
			return designBotExecutor;
		} catch (ClassNotFoundException ex) {
			System.err.println(ex);
		} catch (InstantiationException ex) {
			System.err.println(ex);
		} catch (IllegalAccessException ex) {
			System.err.println(ex);
		}
		return null;
	}
	
	@Override
	public QualityAttribute clone() {
		QualityAttribute newQualityAttribute = new QualityAttribute();
		if (id!=null) {
			newQualityAttribute.setId(new Long(id.longValue()));
		}
		if (name!=null)
			newQualityAttribute.setName(new String(name));
		return newQualityAttribute;
	}

}
