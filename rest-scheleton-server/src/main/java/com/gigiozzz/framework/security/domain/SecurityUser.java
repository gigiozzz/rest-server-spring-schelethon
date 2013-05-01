package com.gigiozzz.framework.security.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;



public class SecurityUser extends User {

	private static final long serialVersionUID = 1426628170932266826L;

	//da usare per aggiungere valori che si vuole recuperare dal security context
	
	public SecurityUser(String username, String password,Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
    public SecurityUser(String username, String password, boolean enabled, Collection<GrantedAuthority> authorities) throws IllegalArgumentException {
    	super(username, password, enabled, true, true, true, authorities);
    }


}