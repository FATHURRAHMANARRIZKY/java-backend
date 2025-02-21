package fimpa.co.fimpa.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import fimpa.co.fimpa.model.Users;

public interface UsersRepository extends MongoRepository<Users, String> {

    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
}
