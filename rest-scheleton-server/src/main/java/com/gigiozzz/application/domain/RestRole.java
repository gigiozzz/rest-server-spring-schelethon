package com.gigiozzz.application.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity(name="restrole")
public class RestRole {
	
	@Id
	private Long id;
	
	@ManyToMany(mappedBy="roles")
	private List<RestUser> users;
	private String role;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<RestUser> getUsers() {
		return users;
	}
	public void setUsers(List<RestUser> users) {
		this.users = users;
	}
	

}
