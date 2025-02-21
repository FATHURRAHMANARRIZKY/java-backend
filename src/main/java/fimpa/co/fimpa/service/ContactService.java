package fimpa.co.fimpa.service;

import fimpa.co.fimpa.model.Contact;
import fimpa.co.fimpa.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact getContact() {
        // Assuming there is only one contact object in the database
        return contactRepository.findAll().stream().findFirst().orElse(null);
    }
}