package com.example.demo.controllers;

import com.example.demo.DTO.ContactRequest;
import com.example.demo.service.ContactService;
import com.example.demo.service.EmailVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/identify")
public class ContactController {

    @Autowired
    ContactService contactService;
    @PostMapping
    public ResponseEntity identify(@RequestBody ContactRequest contactRequest){

        if(!EmailVerifier.validate(contactRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please enter a valid email");
        }
        return ResponseEntity.status(HttpStatus.OK ).body(contactService.identify(contactRequest));
    }
}
