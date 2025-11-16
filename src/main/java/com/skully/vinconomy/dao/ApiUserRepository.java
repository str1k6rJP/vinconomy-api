package com.skully.vinconomy.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.skully.vinconomy.model.ApiUser;

@Repository
public interface ApiUserRepository extends CrudRepository<ApiUser, Long> {

	ApiUser findByUsername(String username);
	
	@Query("FROM ApiUser WHERE username = :username OR uuid = :uuid")
	List<ApiUser> findByUsernameOrUUID(String username, String uuid);

}
