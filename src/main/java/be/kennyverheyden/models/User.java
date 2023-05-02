package be.kennyverheyden.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity(name="tblUser")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userID;
	@Column(name="email")
	private String eMail;
	@Column(name="name")
	private String name;
	@Column(name="firstName")
	private String firstName;
	@Column(name="secret")
	private String Secret;
	@Column(name="creation")
	private String creation;
	@OneToOne
	@JoinColumn(name="userRoleFK")
	UserRole userRole;
	@OneToOne
	@JoinColumn(name="currencyFK")
	Currency currency;
	
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecret() {
		return Secret;
	}
	public void setSecret(String secret) {
		Secret = secret;
	}
	public String getCreation() {
		return creation;
	}
	public void setCreation(String creation) {
		this.creation = creation;
	}
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
}
