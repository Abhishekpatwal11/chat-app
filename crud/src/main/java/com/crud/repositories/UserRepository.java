package com.crud.repositories;

import com.crud.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
//    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String userName);
}
