package com.skully.vinconomy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.skully.vinconomy.enums.ApiRole;
import com.skully.vinconomy.enums.AsyncType;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkRegistration;

@Service
public class VinconomyDefaultsService {

	Logger logger = LoggerFactory.getLogger(VinconomyDefaultsService.class);
	
	@Autowired
	TradeNetworkService tradeNetworkService;
	
	@Autowired
	UserService userService;
	
    @EventListener(ApplicationReadyEvent.class)
    public void createGlobalTradeNetwork() {
    	if (userService.getApiUser("admin") == null) {
    		logger.warn("No Administrator account found. Creating default admin account");
    		userService.registerAPIUser( "admin", "Admin1234", ApiRole.SUPERADMIN, "Administrator", null);
    	}
    	    	
    	if (tradeNetworkService.findByNetworkAccessKey("GLOBAL") == null) {
    		logger.warn("No Global Trade Network found. Creating default owned by admin account");
    		TradeNetworkRegistration reg = new TradeNetworkRegistration();
    		reg.setName("Global Network");
    		reg.setDescription("A Publicly available network available for everyone to join");
    		reg.setAutoAcceptRequests(true);
    		reg.setVisible(true);
    		reg.setModdedItemsAllowed(false);
    		reg.setAsyncType(AsyncType.ASYNC);
    		reg.setCustomAccessKey("GLOBAL");

    		tradeNetworkService.registerTradeNetwork(reg, userService.getApiUser("admin"));
    		
    	}
    }
    
    
}
