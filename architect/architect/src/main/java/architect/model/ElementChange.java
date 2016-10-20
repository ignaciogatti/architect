package architect.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;

import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.sql.rowset.serial.SerialBlob;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="elementChange")
@XmlSeeAlso({Scenario.class,
	Responsibility.class,
	Module.class,
	Dependency.class,
	DesignBot.class,
	ArchitectureAnalysis.class}) 
@XmlAccessorType(XmlAccessType.NONE)
public class ElementChange {

	@XmlElement
	private Long id;
	
	@XmlElement
	private Long id_architecture;
	
	@XmlElement
	private String element_type;
	
	@XmlElement
	private Long change_number;
	
	@XmlElement
	private String change_type;
	
	@Lob
	private Blob oldElementblob;
	
	@Lob
	private Blob newElementblob;
	
	@XmlElement
	private Element oldElement;
	
	@XmlElement
	private Element newElement;
	
	@XmlElement
	private Boolean consistent;
	
	@XmlElement
	private Boolean undoChange;


	public ElementChange(Long id_architecture, String element_type,
			String change_type, Element oldElement, Element newElement,
			Boolean consistentState, Boolean undoChange) {
		this.id_architecture = id_architecture;
		setElement_type(element_type);
		setChange_type(change_type);
		setOldElement(oldElement);
		setNewElement(newElement);
		this.consistent = consistentState;
		this.undoChange = undoChange;
	}
	
	public ElementChange() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId_architecture() {
		return id_architecture;
	}

	public void setId_architecture(Long id_architecture) {
		this.id_architecture = id_architecture;
	}

	public String getElement_type() {
		return element_type;
	}

	public void setElement_type(String element_type) {
		this.element_type = element_type;
	}
	
	public Long getChange_number() {
		return change_number;
	}

	public void setChange_number(Long change_number) {
		this.change_number = change_number;
	}

	public String getChange_type() {
		return change_type;
	}

	public void setChange_type(String change_type) {
		this.change_type = change_type;
	}
	
	@XmlTransient
	public Blob getOldElementblob() {
		return this.oldElementblob;
	}
	
	public void setOldElementblob(Blob changed) {
		this.oldElementblob = changed;
		this.oldElement = deserialize(changed);
	} 

	@Transient
	public Element getOldElement() {
		return oldElement;
	} 

	public void setOldElement(Element changed) {
		this.oldElement = changed;
		this.oldElementblob = serialize(changed);
	}
	
	@XmlTransient
	public Blob getNewElementblob() {
		return this.newElementblob;
	}
	
	public void setNewElementblob(Blob changed) {
		this.newElementblob = changed;
		this.newElement = deserialize(changed);
	} 

	@Transient
	public Element getNewElement() {
		return newElement;
	} 

	public void setNewElement(Element changed) {
		this.newElement = changed;
		this.newElementblob = serialize(changed);
	}
	
	private SerialBlob serialize(Element element) {
		ByteArrayOutputStream baos;
		ObjectOutputStream out;
		baos = new ByteArrayOutputStream();
		try {
			out = new ObjectOutputStream(baos);
			out.writeObject(element);
			out.close();
			return new SerialBlob(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Element deserialize(Blob blob) {
		try {
			byte[] bytes = blob.getBytes(1, (int) blob.length());
		    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		    ObjectInputStream is = new ObjectInputStream(in);
		    return (Element) is.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Boolean getConsistent() {
		return consistent;
	}

	public void setConsistent(Boolean consistent) {
		this.consistent = consistent;
	}

	public Boolean getUndoChange() {
		return undoChange;
	}

	public void setUndoChange(Boolean undoChange) {
		this.undoChange = undoChange;
	}

}