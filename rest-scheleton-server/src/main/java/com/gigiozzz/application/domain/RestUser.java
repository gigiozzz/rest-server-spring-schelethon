package com.gigiozzz.application.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Entity(name="restuser")
public class RestUser {
	
	@Id
	private Long id;
	
	private String firstName;
	private String lastName;

	private boolean enabled;
	
	@Column(unique=true)
	private String username;
	private String password;
	
//	@OneToOne(mappedBy="user", cascade={CascadeType.ALL})
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(
		      name="USER_ROLE",
		      joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
		      inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")})
	private List<RestRole> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<RestRole> getRoles() {
		return roles;
	}

	public void setRoles(List<RestRole> roles) {
		this.roles = roles;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}




}