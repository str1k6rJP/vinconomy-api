package com.skully.vinconomy.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.skully.vinconomy.dao.ShopProductRepository;
import com.skully.vinconomy.dao.ShopRepository;
import com.skully.vinconomy.dao.ShopTradeRepository;
import com.skully.vinconomy.dao.TradeNetworkNodeRepository;
import com.skully.vinconomy.enums.TradeStatus;
import com.skully.vinconomy.model.Shop;
import com.skully.vinconomy.model.ShopProduct;
import com.skully.vinconomy.model.ShopProductId;
import com.skully.vinconomy.model.ShopTrade;
import com.skully.vinconomy.model.TradeNetworkNode;
import com.skully.vinconomy.model.dto.SearchOptions;
import com.skully.vinconomy.model.dto.SearchResult;
import com.skully.vinconomy.model.dto.shop.ShopPurchaseUpdate;
import com.skully.vinconomy.model.dto.shop.ShopPurchaseUpdateResponse;
import com.skully.vinconomy.model.dto.shop.ShopTradeUpdate;
import com.skully.vinconomy.util.GameUtils;

@Service
public class MarketService {

	@Autowired
	ShopTradeRepository tradeDao;
	
	@Autowired
	ShopRepository shopDao;
	
	@Autowired
	ShopService shopService;
	
	@Autowired
	ShopProductRepository productDao;
	
	@Autowired
	TradeNetworkNodeRepository nodeDao;
	
	public List<ShopPurchaseUpdateResponse> purchaseItems(List<ShopPurchaseUpdate> updates, TradeNetworkNode node) {
		List<ShopPurchaseUpdateResponse> responses = new LinkedList<>();
		
		for (ShopPurchaseUpdate update : updates) {
			
			ShopPurchaseUpdateResponse response = new ShopPurchaseUpdateResponse(update);
			responses.add(response);
			
			TradeNetworkNode targetNode = nodeDao.findByGuid(update.getNodeGuid());
			if (targetNode == null) {
				response.setError("Could not find Trade Network Node by GUID " + update.getNodeGuid());
				continue;
			}
			
			Shop targetShop =shopService.getShopById(targetNode.getId(), update.getShopId());
			if (targetShop == null) {
				response.setError("Could not find Shop by Node ID " + targetNode.getId() + " and Shop ID " + update.getShopId());
				continue;
			}
			
			ShopProductId id = new ShopProductId(targetNode.getId(), update.getShopId(), update.getX(),update.getY(),update.getZ(),update.getStallSlot());
			ShopProduct product = GameUtils.getOptional(productDao.findById(id));
			if (product == null) {
				response.setError("Could not find Product " + id.toString());
				continue;
			}
			
			
			ShopTrade trade = new ShopTrade();
			trade.setStatus(TradeStatus.PENDING);
			trade.setRequestingNode(node);
			trade.setShop(targetShop);
			trade.setX(update.getX());
			trade.setY(update.getY());
			trade.setZ(update.getZ());
			trade.setAmount(update.getAmount());
			trade.setPlayerGuid(update.getPlayerGuid());
			trade.setName(update.getName());
			trade.setStallSlot(update.getStallSlot());
			trade.setOriginNode(targetNode);
			trade.setRequestingNode(node);
			Timestamp time = Timestamp.from(Instant.now());
			trade.setCreated(time);
			trade.setModified(time);
			trade = tradeDao.save(trade);
			response.updatePurchase(trade);
			
		}
		return responses;
	}

	public List<ShopTradeUpdate> getPurchasedItems(TradeNetworkNode node) {
		
		List<ShopTrade> trades = tradeDao.findAllByOriginNodeIdAndStatus(node.getId(), TradeStatus.PENDING);
		List<ShopTradeUpdate> updates = new LinkedList<>();
		for	(ShopTrade trade : trades) {
			ShopTradeUpdate update = new ShopTradeUpdate();
			update.setId(trade.getId());
			update.setStatus(trade.getStatus());
			update.setRequestingNode(node.getGuid());
			update.setShopId(trade.getShop().getId().getShopId());
			update.setX(trade.getX());
			update.setY(trade.getY());
			update.setZ(trade.getZ());
			update.setAmount(trade.getAmount());
			update.setPlayerGuid(trade.getPlayerGuid());
			update.setName(trade.getName());
			update.setStallSlot(trade.getStallSlot());
			update.setOriginNode(trade.getRequestingNode().getGuid());
			update.setCreated(trade.getCreated());
			update.setModified(trade.getModified());
			updates.add(update);
		}
		return updates;
	}

	public String updatePurchaseItems(List<ShopTradeUpdate> updates, TradeNetworkNode node, boolean isBuyer) {
		
		for (ShopTradeUpdate update : updates) {
			//Optional<ShopTrade> tradeOpt = tradeDao.findById(update.getId());
			//if (tradeOpt.isPresent()) {
				//ShopTrade trade = tradeOpt.get();		
				try {
					shopService.updatePendingTrade(update.getId(), update.getStatus(), node);
				} catch (Exception e) {
					// Log error
				}
				
			//}
		}
		
		return "SUCCESS";
	}

	public List<SearchResult> searchItems(SearchOptions searchOptions, TradeNetworkNode node) {
		if (node.getNetwork() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This node does not belong to a network");
		}
		return shopDao.searchShopsFor(node.getNetwork().getId(),node.getId(), searchOptions.getOwner(), searchOptions.getShopName(), searchOptions.getProductName(), searchOptions.getCurrencyName());
	}

}
