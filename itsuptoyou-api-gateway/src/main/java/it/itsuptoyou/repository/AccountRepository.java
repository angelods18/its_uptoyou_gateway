package it.itsuptoyou.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.itsuptoyou.collection.Account;

public interface AccountRepository extends MongoRepository<Account,String>{

	Account findByUsername(String username);
}
