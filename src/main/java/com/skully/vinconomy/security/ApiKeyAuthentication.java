package com.skully.vinconomy.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.skully.vinconomy.model.TradeNetworkNode;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;
	
	private final String apiKey;

	private final TradeNetworkNode node;

    public ApiKeyAuthentication(String apiKey, TradeNetworkNode node, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        this.node = node;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }

	public TradeNetworkNode getNode() {
		return node;
	}

	public String getApiKey() {
		return apiKey;
	}
    
    
}