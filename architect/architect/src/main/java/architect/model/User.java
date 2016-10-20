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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;

@Entity
@Table(name="user")
@XmlAccessorType(XmlAccessType.NONE)
public class User implements Serializable {

	private static final long serialVersionUID = 3082383995925954745L;
	
	@XmlAttribute
	@Id
	@GenericGenerator(name="user_id_gen" , strategy="architect.model.IdIncrementGenerator")
	@GeneratedValue(generator="user_id_gen")
	@Column(name = "id", unique = true, nullable = false, length = 20)
	private Long id;
	
	@XmlAttribute
	@Basic
    @Column(name ="username")
	private String username;
	
	@XmlAttribute
	@Basic
    @Column(name ="password")
	private String password;
	
	@XmlAttribute
	@Basic
    @Column(name ="enabled")
	private Boolean enabled;
	
	@XmlAttribute
	@Basic
    @Column(name ="account_expired")
	private Boolean account_expired;
	
	@XmlAttribute
	@Basic
    @Column(name ="credentials_expired")
	private Boolean credentials_expired;
	
	@XmlAttribute
	@Basic
    @Column(name ="account_locked")
	private Boolean account_locked;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "group_user", 
		joinColumns = { @JoinColumn(name ="id_user") }, 
		inverseJoinColumns = { @JoinColumn(name = "id_group") })
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "id_group")
	private Set<Group> groups = new LinkedHashSet<Group>(0);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	
	public Boolean getAccount_expired() {
		return account_expired;
	}

	public void setAccount_expired(Boolean account_expired) {
		this.account_expired = account_expired;
	}

	public Boolean getCredentials_expired() {
		return credentials_expired;
	}

	public void setCredentials_expired(Boolean credentials_expired) {
		this.credentials_expired = credentials_expired;
	}

	public Boolean getAccount_locked() {
		return account_locked;
	}

	public void setAccount_locked(Boolean account_locked) {
		this.account_locked = account_locked;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

}
