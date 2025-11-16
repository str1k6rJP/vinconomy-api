package com.skully.vinconomy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skully.vinconomy.model.dto.SearchOptions;
import com.skully.vinconomy.model.dto.SearchResult;
import com.skully.vinconomy.model.dto.shop.ShopPurchaseUpdate;
import com.skully.vinconomy.model.dto.shop.ShopPurchaseUpdateResponse;
import com.skully.vinconomy.model.dto.shop.ShopTradeUpdate;
import com.skully.vinconomy.security.ApiKeyAuthentication;
import com.skully.vinconomy.service.MarketService;

@RestController()
@RequestMapping("/api/market")
public class MarketController {

	@Autowired
	MarketService marketService;
	
	@PreAuthorize("hasAuthority('GAME_API')")
	@PostMapping("/search")
	public List<SearchResult> searchItems(@RequestBody SearchOptions searchOptions, ApiKeyAuthentication auth) {
		return marketService.searchItems(searchOptions, auth.getNode());
	}
	
	@PreAuthorize("hasAuthority('GAME_API')")
	@PostMapping("/purchase")
	public List<ShopPurchaseUpdateResponse> purchaseItems(@RequestBody List<ShopPurchaseUpdate> updates, ApiKeyAuthentication auth) {
		return marketService.purchaseItems(updates, auth.getNode());
	}
	
	
	@PreAuthorize("hasAuthority('GAME_API')")
	@PostMapping("/purchases/pending")
	public String updatePurchasedItems(@RequestBody List<ShopTradeUpdate> updates, ApiKeyAuthentication auth) {
		return marketService.updatePurchaseItems(updates, auth.getNode(), false);
	}
	
	/**
	 * 
	 * Gets the Pending item purchases for items sold on this node by another node
	 * Should transition all Pending trades to Accepted or Rejected
	 * 
	 * @param updates
	 * @param auth
	 * @return
	 */
	@PreAuthorize("hasAuthority('GAME_API')")
	@GetMapping("/purchases/pending")
	public List<ShopTradeUpdate> getPurchasedItems(ApiKeyAuthentication auth) {
		return marketService.getPurchasedItems(auth.getNode());
	}
	
	/**
	 * Gets the Accepted/Rejected item purchases by this node for other networked nodes
	 * Should transition all Accepted trades to Completed
	 * 
	 * Only needed for full lifecycle checking (ensures items exist on both sides, not arcade-like)
	 * 
	 * @param updates
	 * @param auth
	 * @return
	 */
	@PreAuthorize("hasAuthority('GAME_API')")
	@GetMapping("/purchases/completed")
	public List<ShopPurchaseUpdateResponse> getAcceptedPurchases(@RequestBody List<ShopPurchaseUpdate> updates, ApiKeyAuthentication auth) {
		return marketService.purchaseItems(updates, auth.getNode());
	}
	
	
	// View Posted Trades
	// Add Posted Trade
	// Accept Posted Trade
	// Recall Posted Trade
}
