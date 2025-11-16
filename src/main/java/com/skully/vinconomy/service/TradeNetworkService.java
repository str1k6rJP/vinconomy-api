package com.skully.vinconomy.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.skully.vinconomy.dao.ApiUserRepository;
import com.skully.vinconomy.dao.TradeNetworkNodeRepository;
import com.skully.vinconomy.dao.TradeNetworkRepository;
import com.skully.vinconomy.dao.TradeNetworkRequestRepository;
import com.skully.vinconomy.enums.RequestStatus;
import com.skully.vinconomy.model.ApiUser;
import com.skully.vinconomy.model.TradeNetwork;
import com.skully.vinconomy.model.TradeNetworkNode;
import com.skully.vinconomy.model.TradeNetworkRequest;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkDTO;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkJoinRequest;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkJoinResult;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkNodeRegistration;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkRegistration;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkRequestJoinStatus;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkRequestJoinStatuses;
import com.skully.vinconomy.security.ApiKeyAuthentication;
import com.skully.vinconomy.security.ApiUserDetails;
import com.skully.vinconomy.util.GameUtils;
import com.skully.vinconomy.util.PasswordUtils;

@Service
public class TradeNetworkService {

	Logger logger = LoggerFactory.getLogger(TradeNetworkService.class);
	
	@Autowired
	TradeNetworkRepository tradeNetworkRepo;
	
	@Autowired
	TradeNetworkNodeRepository tradeNetworkNodeRepo;
	
	@Autowired
	TradeNetworkRequestRepository tradeNetworkRequestRepo;
	
	@Autowired
	ApiUserRepository userRepo;
	
	
	public TradeNetworkNode registerTradeNetworkNode(TradeNetworkNodeRegistration reg) {
		
		if (GameUtils.isInvalidGUID(reg.getGuid()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GUID is invalid");
		
		TradeNetworkNode existingNode = tradeNetworkNodeRepo.findByGuid(reg.getGuid());
		if (existingNode != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GUID already exists");
		}
		
		TradeNetworkNode node = new TradeNetworkNode();
		node.setServerName(reg.getName());
		node.setGuid(reg.getGuid());
		node.setLastAccessed(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
		node.setHostname(reg.getHost());
		node.setIp(reg.getIp());
		node.setUdpPort(reg.getUdpListenPort());
		if (StringUtils.isBlank(reg.getUsername())) {
			node.setOwner("ANONYMOUS");
		} else {
			node.setOwner(reg.getUsername());
		}
		node.setApiKey(PasswordUtils.generatePassword());
		
		//0509d2e4-88a3-4c0e-8fbb-be998f4af4b6
		return tradeNetworkNodeRepo.save(node);
	}
	
	public TradeNetworkNode updateTradeNetworkNode(Long nodeId, TradeNetworkNodeRegistration reg) {
		TradeNetworkNode node = GameUtils.getOptional(tradeNetworkNodeRepo.findById(nodeId));
		if (node == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Network Node ID '"+nodeId+"' does not exist");
		}
		return updateTradeNetworkNode(node, reg);
	}
	
	public TradeNetworkNode updateTradeNetworkNode(String guid, TradeNetworkNodeRegistration reg) {
		TradeNetworkNode node = tradeNetworkNodeRepo.findByGuid(guid);
		if (node == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Network Node GUID '"+guid+"' does not exist");
		}
		return updateTradeNetworkNode(node, reg);
	}
	
	public TradeNetworkNode updateTradeNetworkNode(TradeNetworkNode node, TradeNetworkNodeRegistration reg) {
		
		//TODO: Check Ownership of node? Just doing the same basic check as the join request
		if (node != null && node.getOwner() != null && !node.getOwner().equals("ANONYMOUS") && !node.getOwner().equals(reg.getUsername()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "GUID is claimed by a different account");
		
		node.setServerName(reg.getName());
		
		//TODO: I dont think they should be able to update the GUID, especially since there is a Unique constraint on it.
		// If that needs to change, maybe just register an entirely new node?
		//node.setGuid(reg.getGuid());
		
		node.setLastAccessed(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
		node.setHostname(reg.getHost());
		node.setIp(reg.getIp());
		node.setUdpPort(reg.getUdpListenPort());
		
		/**
		//TODO: Make sure we own it before we update the user.
		if (StringUtils.isBlank(reg.getUsername())) {
			node.setOwner("ANONYMOUS");
		} else {
			node.setOwner(reg.getUsername());
		}
		*/
		
		return tradeNetworkNodeRepo.save(node);
	}
	
	
	
	public TradeNetwork registerTradeNetwork(TradeNetworkRegistration reg, ApiUser user) {
		
		if (DoesAccessKeyExist(reg)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Network Access Key '"+reg.getCustomAccessKey()+"' already in use");
		}
		
		TradeNetwork network = new TradeNetwork();
		network.setName(reg.getName());
		network.setOwner(user);
		network.setDescription(reg.getDescription());
		network.setAutoAcceptRequests(reg.isAutoAcceptRequests());
		network.setVisible(reg.isVisible());
		network.setAsyncType(reg.getAsyncType());
		if (StringUtils.isBlank(reg.getCustomAccessKey()))
			network.setNetworkAccessKey(PasswordUtils.generatePassword());
		else {
			network.setNetworkAccessKey(reg.getCustomAccessKey());
		}
		
		return tradeNetworkRepo.save(network);
	}

	public TradeNetworkJoinResult requestJoinTradeNetwork(TradeNetworkJoinRequest reg, ApiKeyAuthentication auth) {
		TradeNetwork network = null;
		TradeNetworkNode node = auth.getNode();
		TradeNetworkRequest request = null;
		
		// Validation
		//if (GameUtils.isInvalidGUID(reg.getGuid()))
		//	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GUID is invalid");
		
		// Check if the node already exists, and if so, if it is owned by a different user
		//node = tradeNetworkNodeRepo.findByGuid(reg.getGuid());
		if (node != null && node.getOwner() != null && !node.getOwner().equals("ANONYMOUS") && !node.getOwner().equals(reg.getUsername()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "GUID is claimed by a different account");
		
		// Check if network they are trying to join is valid
		
		if (StringUtils.isNotBlank(reg.getNetworkAccessKey())) {
			network = tradeNetworkRepo.findByNetworkAccessKey(reg.getNetworkAccessKey());
			if (network == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Network access key " + reg.getNetworkAccessKey() +" is not valid ");
			}
		} else if (reg.getNetworkId() != null){
			network = GameUtils.getOptional(tradeNetworkRepo.findById(reg.getNetworkId()));
			if (network == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Network ID '"+reg.getNetworkId()+"' does not exist");
			}
		}
		

			
		Timestamp instant = Timestamp.from(Calendar.getInstance().toInstant());
		// Handle creation and updates of the node itself
		if (node == null) {
			node = new TradeNetworkNode();
			node.setGuid(reg.getGuid());
			node.setServerName(reg.getServerName());
			if (StringUtils.isBlank(reg.getUsername())) {
				node.setOwner("ANONYMOUS");
			} else {
				node.setOwner(reg.getUsername());
			}
			
			String apiKey = PasswordUtils.generatePassword();
			node.setApiKey(apiKey);
			node.setLastAccessed(instant);
			node = tradeNetworkNodeRepo.save(node);
		}
		
		if (node.getNetwork() != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Network Node already belongs to a network. Please leave the current network before joining another");
		}
		
		// Lookup any existing Network Join requests for this node. If one exists, make sure the guid was not banned
		// If it was rejected or removed (due to inactivity) then re-request access
		request = tradeNetworkRequestRepo.findByNetworkAndNode(network.getId(), node.getId());
		if (request == null) { 
			request = new TradeNetworkRequest();
			request.setNetwork(network);
			request.setNode(node);
			request.setCreatedAt(instant);
			request.setStatus(RequestStatus.PENDING);
		} 
		
		switch(request.getStatus()) {
			//case null: // If it wasn't set for some reason (E.g. a new request);
			case REJECTED: // If our application was previously rejected
			case REMOVED: // If we joined and were removed at some point
			case PENDING: // If its already pending, see if auto-join was turned on after we originally requested
				if (network.isAutoAcceptRequests()) {
					node.setNetwork(network);
					request.setStatus(RequestStatus.ACCEPTED);
					request.setMessage("Accepted - Automatic");
					node = tradeNetworkNodeRepo.save(node);
				}
				request.setLastModified(instant);
				request = tradeNetworkRequestRepo.save(request);
				break;
			case BANNED: // Banned users cannot re-request access;
			case ACCEPTED: // Don't revert Accepted back to Pending
			default:
				//Do nothing. Either we shouldn't go back to PENDING status, or can't (Banned).
				break;
		}
				
		
		TradeNetworkJoinResult result = new TradeNetworkJoinResult();
		result.setApiKey(node.getApiKey());
		result.setStatus(request.getStatus());
		result.setMessage(request.getMessage());
		return result;
	}
	

	public TradeNetwork findByNetworkAccessKey(String key) {
		return tradeNetworkRepo.findByNetworkAccessKey(key);
	}

	public TradeNetworkRequestJoinStatuses viewJoinRequestsForNetwork(ApiUserDetails user, Long networkId, String status) {
		RequestStatus statusEnum = null;
		if (StringUtils.isNotBlank(status)) {
			try {
				statusEnum = RequestStatus.valueOf(status);
			}
			catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Join Status '"+status+"' does not exist");
			}
		}

		
		Optional<TradeNetwork> network = tradeNetworkRepo.findById(networkId);
		if (network.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Network ID '"+networkId+"' does not exist");
		}
		
		if (!HasOwnershipOrAdmin(user, network.get())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Network ID owned by another user");
		}
		
		List<TradeNetworkRequestJoinStatus> requests = tradeNetworkRequestRepo.findByNetworkIdAndStatus(networkId, statusEnum);
		
		TradeNetworkRequestJoinStatuses req = new TradeNetworkRequestJoinStatuses();
		req.setNetwork( new TradeNetworkDTO(network.get()));
		req.setRequests(requests);

		
		return req;
	}
	
	public TradeNetworkRequestJoinStatus viewJoinRequestForNetwork(ApiUserDetails user, Long networkId, Long nodeId) {
		
		Optional<TradeNetwork> network = tradeNetworkRepo.findById(networkId);
		if (network.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Network ID '"+networkId+"' does not exist");
		}
		
		if (!HasOwnershipOrAdmin(user, network.get())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Network ID owned by another user");
		}
		
		TradeNetworkRequest request = tradeNetworkRequestRepo.findByNetworkAndNode(networkId, nodeId);
		if (request != null)
			return new TradeNetworkRequestJoinStatus(request);
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Network '"+network.get().getName()+"' does not have a join request for Node " + nodeId);
	}

	public String transitionRequestToStatus(Long networkId, Long nodeId, ApiUserDetails user, RequestStatus targetStatus, String message) {

		TradeNetwork network = null;
		Optional<TradeNetwork> optNetwork = tradeNetworkRepo.findById(networkId);
		if (optNetwork.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Network ID '"+networkId+"' does not exist");
		}
		network = optNetwork.get();
		
		if (!HasOwnershipOrAdmin(user, network)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Network ID owned by another user");
		}
		
		TradeNetworkNode node = null;
		Optional<TradeNetworkNode> optNode = tradeNetworkNodeRepo.findById(nodeId);
		if (optNode.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Node ID '"+networkId+"' does not exist");
		}
		node = optNode.get();
		
		TradeNetworkRequest request = tradeNetworkRequestRepo.findByNetworkAndNode(networkId, nodeId);
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Join Request for Network ID '"+networkId+"' and Node ID '"+nodeId+"' does not exist");
		}
		
		Timestamp instant = Timestamp.from(Calendar.getInstance().toInstant());
		switch(targetStatus) {

			// In the case of moving any state to Rejected, Removed, or Banned, we could also come from ACCEPTED state
			// Therefore, we must assume they are currently on a network, which will boot them off.
			case REJECTED:
			case REMOVED:
			case BANNED:
				// Clear the network from the node
				node.setNetwork(null);
				
				// Set the request to target status
				request.setStatus(targetStatus);
				request.setLastModified(instant);
				break;
				
			case ACCEPTED: // Don't revert Accepted back to Pending
				node.setNetwork(network);
				node = tradeNetworkNodeRepo.save(node);
				
				request.setStatus(RequestStatus.ACCEPTED);
				request.setLastModified(instant);
				break;
			case PENDING: // If its already pending, see if auto-join was turned on after we originally requested
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported Transition from " + request.getStatus().toString() + " to " + targetStatus.toString() );
		}
		
		request.setMessage(message);
		
		//Update the node and request with any changes
		node = tradeNetworkNodeRepo.save(node);
		request = tradeNetworkRequestRepo.save(request);
		
		return null;
	}
	


	public TradeNetworkDTO updateNetwork(Long networkId, ApiUserDetails user, TradeNetworkRegistration reg) {
		TradeNetwork network = null;
		Optional<TradeNetwork> optNetwork = tradeNetworkRepo.findById(networkId);
		if (optNetwork.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Network ID '"+networkId+"' does not exist");
		}
		network = optNetwork.get();
		
		// Check if we can update the custom access key
		if (reg.getCustomAccessKey() != null) {
			TradeNetwork existingNetwork = tradeNetworkRepo.findByNetworkAccessKey(reg.getCustomAccessKey());
			if (existingNetwork != null && !network.equals(existingNetwork)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Network Access Key '"+reg.getCustomAccessKey()+"' already in use");
			}
		}
		
		ApiUser owner = network.getOwner();
		// Check if there is a new owner, if it exists, and we are SUPERADMIN
		if (StringUtils.isNotBlank(reg.getOwner())) {
			ApiUser newUser = userRepo.findByUsername(reg.getOwner());
			if (newUser == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user '"+reg.getOwner()+"' to inherit Trade Network");
			}
			
			if (!HasOwnershipOrSuperadmin(user, network)) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You do not have permission to reassign ownership of this Trade Network");
			}
			owner = newUser;
		}
		
		if (!HasOwnershipOrAdmin(user, network)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Network ID owned by another user");
		}

		network.setName(reg.getName());
		network.setDescription(reg.getDescription());
		network.setAsyncType(reg.getAsyncType());
		network.setAutoAcceptRequests(reg.isAutoAcceptRequests());
		network.setModdedItemsAllowed(reg.isModdedItemsAllowed());
		network.setVisible(reg.isVisible());
		
		if (reg.getCustomAccessKey() != null) {
			network.setNetworkAccessKey(reg.getCustomAccessKey());
		}
		
		if (StringUtils.isNotBlank(reg.getOwner())) {
			network.setOwner(owner);
		}
		
		network = tradeNetworkRepo.save(network);
		return new TradeNetworkDTO(network);
	}
	
	
	public boolean DoesAccessKeyExist(TradeNetworkRegistration reg) {
		return DoesAccessKeyExist(reg, null);
	}
	
	public boolean DoesAccessKeyExist(TradeNetworkRegistration reg, TradeNetwork curNetwork) {
		// Check if we can update the custom access key
		if (reg.getCustomAccessKey() != null) {
			TradeNetwork existingNetwork = tradeNetworkRepo.findByNetworkAccessKey(reg.getCustomAccessKey());
			if (existingNetwork != null && !existingNetwork.equals(curNetwork)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean HasOwnershipOrAdmin(ApiUserDetails user, TradeNetwork network) {
		boolean isOwner = network.getOwner().equals(user.getUser());
		return  isOwner || user.hasRole("ADMIN");
	}
	
	public static boolean HasOwnershipOrSuperadmin(ApiUserDetails user, TradeNetwork network) {
		return network.getOwner().equals(user.getUser()) || user.hasRole("SUPERADMIN");
	}


	public TradeNetworkNode leaveTradeNetwork(ApiKeyAuthentication auth) {
		TradeNetworkNode node = auth.getNode();

		TradeNetworkRequest request = tradeNetworkRequestRepo.findByNetworkAndNode(node.getNetwork().getId(), node.getId());
		request.setStatus(RequestStatus.REMOVED);
		request.setMessage("User Left Trade Network");
		
		node.setNetwork(null);
		
		tradeNetworkNodeRepo.save(node);
		tradeNetworkRequestRepo.save(request);
		
		return node;
	}



	
}
