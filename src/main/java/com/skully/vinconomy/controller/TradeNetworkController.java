package com.skully.vinconomy.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.skully.vinconomy.enums.RequestStatus;
import com.skully.vinconomy.model.TradeNetwork;
import com.skully.vinconomy.model.TradeNetworkNode;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkDTO;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkJoinRequest;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkJoinResult;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkNodeRegistration;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkRegistration;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkRequestJoinStatus;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkRequestJoinStatuses;
import com.skully.vinconomy.security.ApiKeyAuthentication;
import com.skully.vinconomy.security.ApiUserDetails;
import com.skully.vinconomy.service.TradeNetworkService;

import jakarta.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("/api/network")
public class TradeNetworkController {
	
	@Autowired
	TradeNetworkService service;
	
	@PutMapping("/node")
	public TradeNetworkNode registerTradeNetworkNode(@RequestBody() TradeNetworkNodeRegistration reg, HttpServletRequest request) {
		
		if (StringUtils.isBlank(reg.getName())) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be blank");
		
		if (StringUtils.isBlank(reg.getGuid())) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GUID cannot be blank");

		
		reg.setHost(request.getRemoteHost());
		reg.setIp(request.getRemoteAddr());
		reg.setUsername( request.getRemoteUser());
		return service.registerTradeNetworkNode(reg);
	}
	
	@PatchMapping("/node/{nodeId}")
	public TradeNetworkNode updateTradeNetworkNode(@RequestBody() TradeNetworkNodeRegistration reg,@PathVariable("nodeId") Long nodeId,  HttpServletRequest request) {
		
		if (StringUtils.isBlank(reg.getName())) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be blank");
		
		if (StringUtils.isBlank(reg.getGuid())) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GUID cannot be blank");

		
		reg.setHost(request.getRemoteHost());
		reg.setIp(request.getRemoteAddr());
		reg.setUsername( request.getRemoteUser());
		return service.updateTradeNetworkNode(nodeId, reg);
	}
	
	@PatchMapping("/node")
	public TradeNetworkNode updateTradeNetworkNode(@RequestBody() TradeNetworkNodeRegistration reg, HttpServletRequest request) {
		
		if (StringUtils.isBlank(reg.getName())) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be blank");
		
		if (StringUtils.isBlank(reg.getGuid())) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GUID cannot be blank");

		
		reg.setHost(request.getRemoteHost());
		reg.setIp(request.getRemoteAddr());
		reg.setUsername( request.getRemoteUser());
		return service.updateTradeNetworkNode(reg.getGuid(), reg);
	}
	
	/**
	 * Attempts to join a given trade network. If auto-accept is enabled for the given network, they will receive access instantly.
	 * Otherwise, a new request should be created for the other to accept or reject.
	 * If there is already a pending request, it shall return the status of the request
	 * @param networkId
	 * @return
	 */
	@PreAuthorize("hasAuthority('GAME_API')")
	@PostMapping("/join")
	public TradeNetworkJoinResult joinTradeNetwork(@RequestBody() TradeNetworkJoinRequest reg, ApiKeyAuthentication auth) {
		return service.requestJoinTradeNetwork(reg, auth);
	}
	
	/**
	 * Attempts to leave the current trade network. Will also set the respective TradeNetworkRequest to REMOVED with a message saying they left 
	 * @return
	 */
	@PreAuthorize("hasAuthority('GAME_API')")
	@PostMapping("/leave")
	public TradeNetworkNode leaveradeNetwork(ApiKeyAuthentication auth) {
		return service.leaveTradeNetwork(auth);
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@PutMapping()
	public TradeNetwork registerTradeNetwork(@RequestBody() TradeNetworkRegistration reg, @AuthenticationPrincipal ApiUserDetails user) {		
		if (StringUtils.isBlank(reg.getName())) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be blank");
			
		return service.registerTradeNetwork(reg, user.getUser());
	}
		
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/{networkid}/requests")
	public TradeNetworkRequestJoinStatuses viewJoinRequests(@PathVariable("networkid") Long networkId, @RequestParam(required = false, name = "status") String status, @AuthenticationPrincipal ApiUserDetails user) {
		return service.viewJoinRequestsForNetwork(user, networkId, status);
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/{networkid}/requests/acceptall")
	public String acceptAllPendingJoinRequests(@PathVariable("networkid") String networkId, String requestId, @AuthenticationPrincipal ApiUserDetails user) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/{networkid}/requests/rejectall")
	public String rejectAllPendingJoinRequests(@PathVariable("networkid") String networkId, String requestId, @AuthenticationPrincipal ApiUserDetails user) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/{networkid}/requests/{nodeId}")
	public TradeNetworkRequestJoinStatus viewJoinRequest(@PathVariable("networkid") Long networkId, @PathVariable("nodeId") Long nodeId, @AuthenticationPrincipal ApiUserDetails user) {
		return service.viewJoinRequestForNetwork(user, networkId, nodeId);
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/{networkid}/requests/{nodeId}")
	public String transitionJoinRequest(@PathVariable("networkid") Long networkId, @PathVariable("nodeId") Long nodeId,
			@RequestParam("status") RequestStatus status,@RequestParam(required = false, name = "message") String message,
			@AuthenticationPrincipal ApiUserDetails user) {
		return service.transitionRequestToStatus(networkId, nodeId, user, status, message);
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/{networkid}/requests/{nodeId}/accept")
	public String acceptJoinRequest(@PathVariable("networkid") Long networkId, @PathVariable("nodeId") Long nodeId, @AuthenticationPrincipal ApiUserDetails user) {
		return service.transitionRequestToStatus(networkId, nodeId, user, RequestStatus.ACCEPTED, "Accepted");
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/{networkid}/requests/{nodeId}/reject")
	public String rejectJoinRequest(@PathVariable("networkid") Long networkId, @PathVariable("nodeId") Long nodeId, @AuthenticationPrincipal ApiUserDetails user) {
		return service.transitionRequestToStatus(networkId, nodeId, user, RequestStatus.REJECTED, "Rejected");
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@PatchMapping(path = "/{networkid}")
	public TradeNetworkDTO updateTradeNetwork(@PathVariable("networkid") Long networkId, @AuthenticationPrincipal ApiUserDetails user, @RequestBody() TradeNetworkRegistration reg) {
		return service.updateNetwork(networkId, user, reg);
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@DeleteMapping(path = "/{networkid}")
	public String deleteTradeNetwork(@PathVariable("networkid") Long networkId, @AuthenticationPrincipal ApiUserDetails user) {
		// Find all nodes belonging to network and set them to null
		// Do we need to clear out all shops on the node too? Probably not - they could just join another network and all the info is still intact
		// how would we handle trades? Cancel all of them? That would work, but what about FK issues arising where the network ID points to a network that doesnt exist anymore?
		// Should we outright delete the trade rows instead of cancel them? What downstream effects would that have on the different ASYNC modes?
		// If there is no Canceled status, how could we refund players money for the stricter ASYNC types?
		
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}
	
	// View Posted Trades
	// Add Posted Trade
	// Accept Posted Trade
	// Recall Posted Trade
}
