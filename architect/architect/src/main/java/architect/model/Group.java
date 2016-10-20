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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;

@Entity
@Table(name="grupo")
@XmlAccessorType(XmlAccessType.NONE)
public class Group implements Serializable {

	private static final long serialVersionUID = 3978303394656045916L;
	
	@XmlAttribute
	@Id
	@GenericGenerator(name="group_id_gen" , strategy="architect.model.IdIncrementGenerator")
	@GeneratedValue(generator="group_id_gen")
	@Column(name = "id", unique = true, nullable = false, length = 20)
	private Long id;
	
	@XmlAttribute
	@Basic
    @Column(name ="groupname")
	private String groupname;
	
	@XmlElement(name="user")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "group_user", 
		joinColumns = { @JoinColumn(name ="id_group") }, 
		inverseJoinColumns = { @JoinColumn(name = "id_user") })
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id_user")
	private Set<User> users = new LinkedHashSet<User>(0);
	
	@XmlElement(name="architecture")
	@OneToMany(mappedBy = "group",fetch = FetchType.LAZY , cascade = {javax.persistence.CascadeType.ALL}, orphanRemoval=true) 
	@Cascade({org.hibernate.annotations.CascadeType.ALL}) 
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id")
	private Set<Architecture> architectures = new LinkedHashSet<Architecture>(0);

	public Group() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public Set<Architecture> getArchitectures() {
		return architectures;
	}

	public void setArchitectures(Set<Architecture> architectures) {
		this.architectures = architectures;
	}

}
