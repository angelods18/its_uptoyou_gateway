package it.itsuptoyou.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.itsuptoyou.collection.Authority;

public interface AuthorityRepository extends MongoRepository<Authority, String> {

}
