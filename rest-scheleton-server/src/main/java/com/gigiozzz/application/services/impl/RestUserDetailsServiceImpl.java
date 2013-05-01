package com.gigiozzz.application.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gigiozzz.application.domain.RestRole;
import com.gigiozzz.application.domain.RestUser;
import com.gigiozzz.application.repositories.RestUserRepository;
import com.gigiozzz.framework.security.domain.SecurityUser;

/**
 * A custom {@link UserDetailsService} where user information is retrieved from
 * a JPA repository
 */
@Service
@Transactional(readOnly = true)
public class RestUserDetailsServiceImpl implements UserDetailsService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RestUserRepository userRepository;

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	/**
	 * Returns a populated {@link UserDetails} object. The username is first
	 * retrieved from the database and then mapped to a {@link UserDetails}
	 * object.
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		RestUser domainUser =null;

		try {
			domainUser = userRepository.findByUsername(username);
		} catch(Exception ex){
			logger.error("Error UserRepository",ex);
			throw new RuntimeException(ex);
		}
		
		if (domainUser == null) {
			throw new UsernameNotFoundException(messages.getMessage(
					"JdbcDaoImpl.notFound", new Object[] { username },
					"Username {0} not found"));
		}
		
		Collection<GrantedAuthority> dbAuths = getGrantedAuthoritiesFromRoles(domainUser.getRoles());
		if(dbAuths.size()==0){
			throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.noAuthority",
                    new Object[] { username }, "User {0} has no GrantedAuthority"));
		}
		
		return createUserDetails(domainUser,dbAuths);
	}

	
	private UserDetails createUserDetails(RestUser domainUser,Collection<GrantedAuthority> grantedAuthorities){
		// gestiamo solo utente disabilitato attualmente
		SecurityUser user = new SecurityUser(domainUser.getUsername(), domainUser.getPassword(), domainUser.isEnabled(), grantedAuthorities);
		return user;
	}

	private List<GrantedAuthority> getGrantedAuthoritiesFromRoles(List<RestRole> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (RestRole role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		return authorities;
	}
}