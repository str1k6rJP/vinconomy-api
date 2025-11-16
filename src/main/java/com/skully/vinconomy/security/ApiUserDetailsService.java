package com.skully.vinconomy.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.skully.vinconomy.dao.ApiUserRepository;
import com.skully.vinconomy.model.ApiUser;

@Service
public class ApiUserDetailsService implements UserDetailsService {

	@Autowired
    private ApiUserRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApiUser user = customerRepository.findByUsername(username.toLowerCase());
        		
        if (user == null)
            throw new UsernameNotFoundException(username);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>(4);
        switch(user.getRole()) {
        	// YES, the fall-throughs are intentional...
	        case SUPERADMIN:
	        	authorities.add(new SimpleGrantedAuthority("SUPERADMIN"));
	        case ADMIN:
	        	authorities.add(new SimpleGrantedAuthority("ADMIN"));
	        case USER:
	        	authorities.add(new SimpleGrantedAuthority("USER"));
	        default:
	        	authorities.add(new SimpleGrantedAuthority("ANONYMOUS"));
        }
        
        //List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().toString()));
        //User details = new User(user.getUsername(), user.getPassword(), authorities);
        ApiUserDetails details = new ApiUserDetails(user.getUsername(), user.getPassword(), authorities);
        details.setUser(user);
        
        
        
        return details;
    }
    
    
}
