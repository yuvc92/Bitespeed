package com.example.demo.controllers;

import com.example.demo.DTO.Contact;
import com.example.demo.DTO.ContactRequest;
import com.example.demo.DTO.ContactResponse;
import com.example.demo.model.CustomerContact;
import com.example.demo.repository.ContactRepository;
import com.example.demo.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/identify")
public class ContactController {
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    ContactService contactService;
    @PostMapping
    public ContactResponse identify(@RequestBody ContactRequest contactRequest) {
        ContactResponse response = new ContactResponse();
//        Contact contact = new Contact();
//            contact.setEmails(List.of(contactRequest.getEmail()));
//            contact.setPhoneNumbers(List.of(contactRequest.getPhoneNumber()));
//            return contact;
//            response.setContact(contact);
        List<CustomerContact> matchingEmail = contactRepository.findAllByEmail(contactRequest.getEmail());
        List<CustomerContact> matchingPhone = contactRepository.findAllByPhoneNumber(contactRequest.getPhoneNumber());
        if(matchingEmail.isEmpty() && matchingPhone.isEmpty()){
            Long newId = contactService.createNewContact(
                    contactRequest.getEmail(),
                    contactRequest.getPhoneNumber(),
                    "primary"
            );
            Contact contact = new Contact();
            contact.setPrimaryContactId(newId);
            contact.setEmails(List.of(contactRequest.getEmail()));
            contact.setPhoneNumbers(List.of(contactRequest.getPhoneNumber()));
            response.setContact(contact);
        }
        else if(matchingEmail.isEmpty()){
            Long primaryId = matchingPhone.get(0).getLinkedId();
            contactService.createNewContact(
                    contactRequest.getEmail(),
                    contactRequest.getPhoneNumber(),
                    primaryId,
                    "secondary"
            );
            List<CustomerContact>contactList = contactRepository.findAllByLinkedId(primaryId);
            List<Integer>phoneList = contactList.stream().map(CustomerContact::getPhoneNumber).distinct().toList();
            List<String>emailList = contactList.stream().map(CustomerContact::getEmail).distinct().toList();
            List<Long>secondaryContactList = contactList.stream().map(CustomerContact::getId).filter(id -> !id.equals(primaryId)).toList();
            Contact contact = new Contact();
            contact.setPrimaryContactId(primaryId);
            contact.setEmails(emailList);
            contact.setPhoneNumbers(phoneList);
            contact.setSecondaryContactIds(secondaryContactList);
            response.setContact(contact);
        }
        else if(matchingPhone.isEmpty()){
            Long primaryId = matchingEmail.get(0).getLinkedId();
            contactService.createNewContact(
                    contactRequest.getEmail(),
                    contactRequest.getPhoneNumber(),
                    primaryId,
                    "secondary"
            );
            List<CustomerContact>contactList = contactRepository.findAllByLinkedId(primaryId);
            List<String>emailList = contactList.stream().map(CustomerContact::getEmail).distinct().toList();
            List<Integer>phoneList = contactList.stream().map(CustomerContact::getPhoneNumber).distinct().toList();
            List<Long>secondaryContactList = contactList.stream().map(CustomerContact::getId).filter(id -> !id.equals(primaryId)).toList();
            Contact contact = new Contact();
            contact.setPrimaryContactId(primaryId);
            contact.setEmails(emailList);
//            contact.setPhoneNumbers(List.of(contactRequest.getPhoneNumber()));
            contact.setPhoneNumbers(phoneList);
            contact.setSecondaryContactIds(secondaryContactList);
            response.setContact(contact);
        }
        else{
            Long primaryId = matchingEmail.get(0).getLinkedId();
            Long secondaryId = matchingPhone.get(0).getLinkedId();
            if( !secondaryId.equals(  primaryId ) ){
                contactRepository.findAllByLinkedId(secondaryId).forEach(contact -> {
                    contact.setLinkedId(primaryId);
                    if(contact.getLinkPrecedence().equals("primary"))contact.setLinkPrecedence("secondary");
                    contactRepository.save(contact);
                });
            }
            List<CustomerContact>contactList = contactRepository.findAllByLinkedId(primaryId);
            List<String>emailList = contactList.stream().map(CustomerContact::getEmail).distinct().toList();
            List<Integer>phoneList = contactList.stream().map(CustomerContact::getPhoneNumber).distinct().toList();
            List<Long>secondaryContactList = contactList.stream().map(CustomerContact::getId).filter(id -> !id.equals(primaryId)).toList();
            Contact contact = new Contact();
            contact.setPrimaryContactId(primaryId);
            contact.setPhoneNumbers(phoneList);
            contact.setEmails(emailList);
            contact.setSecondaryContactIds(secondaryContactList);
            response.setContact(contact);
        }
        return response;
    }
}
