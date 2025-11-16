package com.skully.vinconomy.security;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.skully.vinconomy.model.ApiUser;

public class ApiUserDetails extends User {

	private static final long serialVersionUID = 1L;
	private ApiUser user;
	private boolean authorizedByKey;

	public ApiUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, true, true, true, true, authorities);
	}

	
	public ApiUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password,enabled,accountNonExpired,credentialsNonExpired,accountNonLocked,authorities);
	}


	public ApiUser getUser() {
		return user;
	}


	public void setUser(ApiUser user) {
		this.user = user;
	}


	public boolean isAuthorizedByKey() {
		return authorizedByKey;
	}


	public void setAuthorizedByKey(boolean authorizedByKey) {
		this.authorizedByKey = authorizedByKey;
	}
	
	public boolean hasRole(String role) {
		Collection<GrantedAuthority> auths = this.getAuthorities();
		for (Iterator<GrantedAuthority> iterator = auths.iterator(); iterator.hasNext();) {
			GrantedAuthority type = iterator.next();
			if (type.getAuthority().equals(role)) {
				return true;
			}
			
		}
		return false;
	}
	
	

}
