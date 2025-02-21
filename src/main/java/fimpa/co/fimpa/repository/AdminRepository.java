package fimpa.co.fimpa.repository;

import fimpa.co.fimpa.model.Admin;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByEmail(String email);
}
