package com.skully.vinconomy.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.skully.vinconomy.enums.RequestStatus;
import com.skully.vinconomy.model.TradeNetworkRequest;
import com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkRequestJoinStatus;

@Repository
public interface TradeNetworkRequestRepository extends CrudRepository<TradeNetworkRequest, Long> {

	@Query("SELECT t FROM TradeNetworkRequest t WHERE t.network.id = ?1 AND t.node.id = ?2")
	TradeNetworkRequest findByNetworkAndNode(long network, long node);

	@Query("select new com.skully.vinconomy.model.dto.tradenetwork.TradeNetworkRequestJoinStatus(n.serverName, n.guid, n.hostname, n.ip, n.owner, t.status, t.message, t.createdAt, t.lastModified, n.lastAccessed)"
			+ " FROM TradeNetworkRequest t INNER JOIN TradeNetworkNode n ON t.node.id = n.id WHERE t.network.id = ?1  AND (?2 is null OR t.status = ?2)")
	List<TradeNetworkRequestJoinStatus> findByNetworkIdAndStatus(Long networkId, RequestStatus status);
	
}
