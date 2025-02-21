package fimpa.co.fimpa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import fimpa.co.fimpa.model.Role;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);
}