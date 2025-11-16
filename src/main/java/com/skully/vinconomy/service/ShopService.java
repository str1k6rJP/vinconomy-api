package com.skully.vinconomy.service;

import com.skully.vinconomy.dao.ShopProductRepository;
import com.skully.vinconomy.dao.ShopRepository;
import com.skully.vinconomy.dao.ShopTradeRepository;
import com.skully.vinconomy.dao.TradeNetworkNodeRepository;
import com.skully.vinconomy.enums.TradeStatus;
import com.skully.vinconomy.model.Shop;
import com.skully.vinconomy.model.ShopId;
import com.skully.vinconomy.model.ShopProduct;
import com.skully.vinconomy.model.ShopProductId;
import com.skully.vinconomy.model.ShopRegistration;
import com.skully.vinconomy.model.ShopTrade;
import com.skully.vinconomy.model.TradeNetworkNode;
import com.skully.vinconomy.model.builder.ShopProductBuilder;
import com.skully.vinconomy.model.dto.Product;
import com.skully.vinconomy.model.dto.shop.ShopStall;
import com.skully.vinconomy.model.dto.shop.ShopTradeRequest;
import com.skully.vinconomy.model.dto.shop.ShopUpdate;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkShop;
import com.skully.vinconomy.security.ApiKeyAuthentication;
import com.skully.vinconomy.util.GameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShopService {

	Logger logger = LoggerFactory.getLogger(ShopService.class);
	
	private final List<TradeStatus> OWNER_PENDING_TRANSITIONS = List.of(TradeStatus.FAILED, TradeStatus.PROCESSED, TradeStatus.LACKS_ITEMS);
	
	@Autowired
	ShopRepository shopDao;
	
	@Autowired
	ShopProductRepository productDao;
	
	@Autowired
	ShopTradeRepository tradeRequestDao;
	
	@Autowired
	TradeNetworkNodeRepository tradeNetworkDao;
	
	public Shop registerShop(ShopRegistration reg, TradeNetworkNode node) {
		
		if (reg.getId() <= 0) {
			throw new IllegalArgumentException("ID cannot be less than 1");
		}
		
		ShopId id = new ShopId(node.getId(), reg.getId());
		Shop shop;
		Optional<Shop> existing = shopDao.findById(id);
		if (existing.isPresent()) {
			shop = existing.get();

		} else {
			shop = new Shop(id);
		}
		shop.setName(reg.getName());
		shop.setOwner(reg.getOwner());
		shop.setDescription(reg.getDescription());
		shop = shopDao.save(shop);
		return shop;
	}
	
	public String updateProducts(ShopUpdate update, TradeNetworkNode node) {
		long serverId = node.getId();
		int shopId = update.getId();

		if (shopId <= 0) {
			throw new IllegalArgumentException("ID cannot be less than 1");
		}
		
		ShopId id = new ShopId(node.getId(), shopId);
		Shop shop = GameUtils.getOptional(shopDao.findById(id));
		if (shop == null) {
			logger.trace("Shop doesnt exist for products in update. Creating a blank placeholder.");
			shop = new Shop(id);
		}
		shop.setDescription(update.description);
		shop.setName(update.name);
		shop.setOwner(update.owner);
		shop = shopDao.save(shop);
		
		if (update.isRemoveAll()) {
			productDao.deleteByShopId(serverId, shopId);
		}
		
		List<ShopStall> stallList = update.getStalls();
		
		//TODO: It is currently clearing ALL stall slots that werent updated.
		// We want to remove old entries if/when we change Shops for the stall
		// but we dont want to remove the older entries - update the shop id instead.
		
		// Do we need to query for the X/Y/Z and go that route?
		
		for (ShopStall stall : stallList) {
			if (stall.isRemoveAll()) {
				productDao.deleteByStall(serverId, stall.getX(), stall.getY(), stall.getZ());
			} else {
				HashMap<Integer, ShopProduct> productMap = new HashMap<>();
				List<ShopProduct> removalList = new LinkedList<>();
				List<ShopProduct> existingProducts = productDao.getProductsForStall(serverId,  stall.getX(), stall.getY(), stall.getZ());
				for (ShopProduct product : existingProducts) {
					productMap.put(product.getId().getStallSlot(), product);
				}
				
				List<Product> productUpdates = stall.getProducts();
				for (Product productUpdate : productUpdates) {
					ShopProduct prod = productMap.get(productUpdate.getStallSlot());
					if (prod == null) {
                        logger.debug("No product found for slot {} at {} {} {} on server {} - Skipping!", productUpdate.getStallSlot(), stall.getX(), stall.getY(), stall.getZ(),
                                serverId);
                        continue;
					} else if (prod.getId().getShopId() != shopId) {
						logger.info("Product for different shop {} for slot {} at {} {} {} on server {} - Updating to {}!", prod.getId().getShopId(), productUpdate.getStallSlot(), stall.getX(), stall.getY(), stall.getZ(), serverId, shopId );
						removalList.add(prod);
						ShopProductId productId = new ShopProductId(serverId, shopId, stall.getX(), stall.getY(), stall.getZ(), productUpdate.getStallSlot());
						prod = new ShopProduct();
						prod.setId(productId);
						productMap.put(productUpdate.getStallSlot(), prod);
						
					} else {
						logger.info("Updating product for slot {} at {} {} {} on server {}",productUpdate.getStallSlot(), stall.getX(), stall.getY(), stall.getZ(), serverId );
						
					}
					
					prod.setProductName(productUpdate.getProductName());
					prod.setProductCode(productUpdate.getProductCode());
					prod.setProductAttributes(productUpdate.getProductAttributes());
					prod.setProductQuantity(productUpdate.getProductQuantity());
					
					prod.setCurrencyName(productUpdate.getCurrencyName());
					prod.setCurrencyCode(productUpdate.getCurrencyCode());
					prod.setCurrencyAttributes(productUpdate.getCurrencyAttributes());
					prod.setCurrencyQuantity(productUpdate.getCurrencyQuantity());
					
					prod.setTotalStock(productUpdate.getTotalStock());
				}
				productDao.deleteAll(removalList);
				productDao.saveAll(productMap.values());
			}
		}
		
		//TODO: I had anticipated needing to return some sort of return value, but now I dont remember what this was for months later. Remove?
		return "Updated!";
		
	}
	
	
	public String deleteShop(TradeNetworkNode node, long shopId) {
		productDao.deleteByShopId(node.getId(), shopId);
		ShopId id = new ShopId(node.getId(), shopId);
		shopDao.deleteById(id);
		return null;
	}

	public List<ShopTrade> getPendingTrades(TradeNetworkNode node, int shopId) {
		return tradeRequestDao.findAllByShopIdAndStatus(new ShopId(node.getId(), shopId), TradeStatus.PENDING);
	}

	public ShopTrade addPendingTrade(String networkId, long shopId, ShopTradeRequest req, TradeNetworkNode node) {

		TradeNetworkNode targetNode = tradeNetworkDao.findByGuid(networkId);
		if (targetNode == null) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target Network Node not found");
		
		Shop shop = GameUtils.getOptional(shopDao.findById(new ShopId(targetNode.getId(), shopId)));
		if (shop == null) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop ID not found");
		
		ShopTrade trade = new ShopTrade();
		trade.setRequestingNode(node);
		
		trade.setShop(shop);
		trade.setX(req.getX());
		trade.setY(req.getY());
		trade.setZ(req.getZ());
		trade.setStallSlot(req.getStallSlot());
		
		//trade.setProductCode(req.getProductCode());
		//trade.setProductAttributes(req.getProductAttributes());
		//trade.setProductQuantity(req.getProductQuantity());
		
		//trade.setCurrencyCode(req.getCurrencyCode());
		//trade.setCurrencyAttributes(req.getCurrencyAttributes());
		//trade.setCurrencyQuantity(req.getCurrencyQuantity());
		
		Timestamp instant = Timestamp.from(Instant.now());
		trade.setCreated(instant);
		trade.setModified(instant);
		
		trade.setStatus(TradeStatus.PENDING);
		
		return tradeRequestDao.save(trade);
	}

	/**
	 * Updates a given request into the next transition status
	 * 
	 * Customer Node makes request with addPendingTrade -> PENDING
	 * Customer Node can Cancel the request before it gets Processed: PENDING -> CANCELED
	 * Owner Node must update PENDING respectively:
	 * 		if the stall doesnt have enough items, PENDING -> LACKS_ITEMS
	 * 		if the stall succesfully removed items and created currency: PENDING -> PROCESSED
	 * 		if the stall could not be found or was removed: PENDING -> FAILED
	 * Customer Node must then confirm the status for each processed trade: PROCESSED -> COMPLETED
	 * 
	 * @param tradeId
	 * @param req
	 * @param node
	 */
	public void updatePendingTrade(long tradeId, TradeStatus status, TradeNetworkNode node) {
		ShopTrade trade = GameUtils.getOptional(tradeRequestDao.findById(tradeId));
		if (trade == null) {
			throw new IllegalArgumentException("Trade ID not found");
		}
		
		
		boolean isOwner = trade.getShop().getId().getNetworkNodeId() == node.getId();
		boolean isRequester = trade.getRequestingNode().getGuid() == node.getGuid();
		
		if (isOwner) {
			// Must be Pending for us to ACK it
			if (trade.getStatus() != TradeStatus.PENDING) {
				throw new IllegalStateException("Trade not in PENDING status");
			}
			
			if (!OWNER_PENDING_TRANSITIONS.contains(status)) {
				throw new IllegalStateException("Target status transition is not allowed in the current state");
			}
			
		} else if (isRequester) {
			
			if ((trade.getStatus() == TradeStatus.PENDING && status != TradeStatus.CANCELED) 
					|| (trade.getStatus() == TradeStatus.PROCESSED && status != TradeStatus.COMPLETED)) {
				throw new IllegalStateException("Target status transition is not allowed in the current state");
			}

		} else {
			throw new AccessDeniedException("Access Denied");
		}

		
		trade.setStatus(status);
		trade.setModified(Timestamp.from(Calendar.getInstance().toInstant()));
		tradeRequestDao.save(trade);
		
	}

    /**
     * Returns the shop inventory
     * Boolean flag is used to exclude empty trades (where both product and currency are empty,
     * as they clutter visual space and don't carry anu useful load
     *
     * @param networkGuid        guid
     * @param shopId             shop id inside network
     * @param includeEmptyTrades if to include empty trades
     * @return the shop object with specified trades
     */
    public TradeNetworkShop getShopInventory(String networkGuid, int shopId, boolean includeEmptyTrades) {

		TradeNetworkNode node = tradeNetworkDao.findByGuid(networkGuid);
		if (node == null) 
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trade Network does not exist");
		}
		ShopId shopIdentifier = new ShopId(node.getId(), shopId);
		Shop networkShop = GameUtils.getOptional(shopDao.findById(shopIdentifier));
		if (networkShop == null) 
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trade Network Shop does not exist");
		}
		
		TradeNetworkShop shop = new TradeNetworkShop();
		shop.id = shopId;
		shop.nodeId = networkGuid;
		shop.serverName = node.getServerName();
		shop.name = networkShop.getName();
		shop.owner = networkShop.getOwner();
        shop.products = includeEmptyTrades
                        ? productDao.findByNodeIdAndShopId(node.getId(), shopId)
                        : productDao.findByNodeIdAndShopIdExcludeEmptyTrades(node.getId(), shopId);
		shop.lastUpdatedTimestamp = node.getLastAccessed().getTime();
		
		return shop;
	}

    /**
     * Returns registered trade for the specified shop.
     *
     * @param nodeId
     * @param x
     * @param y
     * @param z
     * @param includeEmptyTrades
     * @return list of found trades for given stall; if no rows, will be empty
     */
    public List<ShopProduct> getStallInventory(long nodeId, long shopId, int x, int y, int z, boolean includeEmptyTrades) {
        return includeEmptyTrades
               ? productDao.findAllByStallId(nodeId, shopId, x, y, z)
               : productDao.findAllByStallIdExcludeEmptyTrades(nodeId, shopId, x, y, z);
    }

    public List<Shop> getAllShops(ApiKeyAuthentication auth) {
        return shopDao.findAllByNetwork(auth.getNode().getNetwork().getId());
    }

	public Shop getShopById(long id, long shopId) {
		return GameUtils.getOptional(shopDao.findById(new ShopId(id, shopId)));

    }

    protected boolean shopExists(long nodeId, long shopId) {
        return shopDao.shopExists(nodeId, shopId) == 1;
    }

    public List<ShopProduct> authoritativeShopStallUpdate(long shopId, ShopStall stall, ApiKeyAuthentication auth) {
        var node = auth.getNode();
        var nodeId = node.getId();
        if (!shopExists(nodeId, shopId)) {
            logger.info("No Shop with id {} exists for Node id:{} name:{}", shopId, nodeId, node.getServerName());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such Shop exists for the Node");
        }
        var rows = getStallInventory(nodeId, shopId, stall.getX(), stall.getY(), stall.getZ(), true);

        if (stall.getProducts().isEmpty()) {
            productDao.deleteByStall(nodeId, stall.getX(), stall.getY(), stall.getZ());
        } else {
            var newSlots = stall.getProducts().stream().map(Product::getStallSlot).collect(Collectors.toUnmodifiableSet());
            rows.stream().filter(p -> !newSlots.contains(p.getId().getStallSlot())).forEach(productDao::delete);
            stall.getProducts().stream().map(p -> new ShopProductBuilder()
                            .withId(new ShopProductId(nodeId, shopId, stall.getX(), stall.getY(), stall.getZ(), p.getStallSlot()))
                            .withCurrency(p.getCurrencyCode(), p.getCurrencyName(), p.getCurrencyQuantity())
                            .withCurrencyAttributes(p.getCurrencyAttributes())
                            .withProduct(p.getProductCode(), p.getProductName(), p.getProductQuantity())
                            .withProductAttributes(p.getProductAttributes())
                            .withTotalStock(p.getTotalStock())
                            .build())
                    .forEach(productDao::save);
        }

        return getStallInventory(nodeId, shopId, stall.getX(), stall.getY(), stall.getZ(), false);
    }

}
