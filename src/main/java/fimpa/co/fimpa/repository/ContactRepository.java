package fimpa.co.fimpa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import fimpa.co.fimpa.model.Contact;

public interface ContactRepository extends MongoRepository<Contact, String> {

}
