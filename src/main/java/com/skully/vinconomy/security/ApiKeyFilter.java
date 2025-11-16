package com.skully.vinconomy.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.skully.vinconomy.dao.TradeNetworkNodeRepository;
import com.skully.vinconomy.model.TradeNetworkNode;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

	private static final String API_KEY_HEADER = "X-API-KEY";

	@Autowired
	TradeNetworkNodeRepository nodeRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String apiKey = request.getHeader(API_KEY_HEADER);

		if (apiKey != null) {
			TradeNetworkNode node = nodeRepo.findByApiKey(apiKey);
			if (node != null) {
				Authentication authentication = new ApiKeyAuthentication(apiKey, node, List.of(new SimpleGrantedAuthority("GAME_API")));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);

	}

}
