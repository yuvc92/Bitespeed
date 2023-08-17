package com.example.demo.service;

import com.example.demo.DTO.Contact;
import com.example.demo.DTO.ContactRequest;
import com.example.demo.DTO.ContactResponse;
import com.example.demo.model.CustomerContact;
import com.example.demo.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

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


    public ContactResponse identify(@RequestBody ContactRequest contactRequest) {
        ContactResponse response = new ContactResponse();
        List<CustomerContact> matchingEmail = contactRepository.findAllByEmail(contactRequest.getEmail());
        List<CustomerContact> matchingPhone = contactRepository.findAllByPhoneNumber(contactRequest.getPhoneNumber());
        if(matchingEmail.isEmpty() && matchingPhone.isEmpty()){
            Long newId = this.createNewContact(
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
            this.createNewContact(
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
            this.createNewContact(
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
