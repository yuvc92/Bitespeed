package com.example.demo.DTO;

import java.util.List;

public class Contact {
    Long primaryContactId;
    List<String> emails; // first element being email of primary contact
    List<Integer>phoneNumbers; // first element being phoneNumber of primary contact
    List<Long>secondaryContactIds;


    public Long getPrimaryContactId() {
        return primaryContactId;
    }

    public List<String> getEmails() {
        return emails;
    }

    public List<Integer> getPhoneNumbers() {
        return phoneNumbers;
    }

    public List<Long> getSecondaryContactIds() {
        return secondaryContactIds;
    }

    public void setPrimaryContactId(Long primaryContactId) {
        this.primaryContactId = primaryContactId;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public void setPhoneNumbers(List<Integer> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void setSecondaryContactIds(List<Long> secondaryContactIds) {
        this.secondaryContactIds = secondaryContactIds;
    }
}
