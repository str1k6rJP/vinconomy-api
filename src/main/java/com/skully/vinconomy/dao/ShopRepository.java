package com.skully.vinconomy.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import com.skully.vinconomy.model.Shop;
import com.skully.vinconomy.model.ShopId;
import com.skully.vinconomy.model.dto.SearchResult;


public interface ShopRepository extends ListCrudRepository<Shop, ShopId> {

	@Query("select new com.skully.vinconomy.model.dto.SearchResult(node.guid, node.serverName, shop.id.shopId, shop.name, shop.owner, shop.description) FROM Shop as shop "
			+ "inner join ShopProduct as product on shop.id.networkNodeId = product.id.nodeId and shop.id.shopId = product.id.shopId "
			+ "inner join TradeNetworkNode as node on shop.id.networkNodeId = node.id "
			+ "where "
			+ "(:shopName is null OR shop.name LIKE %:shopName%)"
			+ " AND (:productName is null OR product.productName LIKE %:productName%)"
			+ " AND (:currencyName is null OR product.currencyName LIKE %:currencyName%)"
			+ " AND (:owner is null OR shop.owner LIKE %:owner%)"
			+ " AND node.id != :nodeId"
            + " AND node.network.id = :networkId"
			+ " GROUP BY shop.id.networkNodeId, shop.id.shopId"
			)
	List<SearchResult> searchShopsFor(@Param("networkId") long networkId,@Param("nodeId") long nodeId, @Param("owner") String owner, @Param("shopName") String shopName, @Param("productName") String productName, @Param("currencyName") String currencyName);

	@Query("FROM Shop shop join TradeNetworkNode as node on shop.id.networkNodeId = node.id where node.network.id = :networkId")
	List<Shop> findAllByNetwork(long networkId);

    @Query("SELECT COUNT(1) FROM TradeNetworkNode n WHERE n.id = :nodeId")
    int nodeExists(@Param("nodeId") long nodeId);

    @Query("SELECT COUNT(1) FROM Shop s WHERE s.id.networkNodeId = :nodeId AND s.id.shopId = :shopId")
    int shopExists(@Param("nodeId") long nodeId, @Param("shopId") long shopId);

}
