package com.example.demo.service;

import com.example.demo.model.CustomerContact;
import com.example.demo.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository;
    public Long createNewContact(String email, Integer phoneNumber,  String precedence){
        CustomerContact newContact = new CustomerContact();
        newContact.setEmail(email);
        newContact.setPhoneNumber(phoneNumber);
        newContact.setCreatedAt(new Date());
        newContact.setLinkPrecedence(precedence);
        contactRepository.save(newContact);
        newContact.setLinkedId(newContact.getId());
        contactRepository.save(newContact);
        return newContact.getId();
    }
    public void createNewContact(String email, Integer phoneNumber, Long linkedId, String precedence){
        CustomerContact newContact = new CustomerContact();
        newContact.setEmail(email);
        newContact.setPhoneNumber(phoneNumber);
        newContact.setCreatedAt(new Date());
        newContact.setLinkPrecedence(precedence);
        newContact.setLinkedId(linkedId);
        contactRepository.save(newContact);
    }
}
