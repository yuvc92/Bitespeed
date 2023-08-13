package com.example.demo.repository;

import com.example.demo.model.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<CustomerContact, Long> {
    List<CustomerContact> findAllByEmail(String email);
    List<CustomerContact> findAllByPhoneNumber(int phoneNumber);
    List<CustomerContact> findAllByLinkedId(long linkedId);

}
