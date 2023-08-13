package com.example.demo.model;

import jakarta.persistence.*;

import javax.xml.crypto.Data;
import java.util.Date;

@Entity
@Table(name = "Customers")
public class CustomerContact {
    @Column(name = "phoneNumber")
    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    @Column(name = "linkedId")
    public Long getLinkedId() {
        return linkedId;
    }

    @Column(name = "linkPrecedence")
    public String getLinkPrecedence() {
        return linkPrecedence;
    }

    @Column(name = "createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    @Column(name = "updateAt")
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Column(name = "deletedAt")
    public Date getDeletedAt() {
        return deletedAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    private Integer phoneNumber;


    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setLinkedId(long linkedId) {
        this.linkedId = linkedId;
    }

    public void setLinkPrecedence(String linkPrecedence) {
        this.linkPrecedence = linkPrecedence;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    private String email;
    private Long linkedId;

    public void setId(Long id) {
        this.id = id;
    }

    public void setLinkedId(Long linkedId) {
        this.linkedId = linkedId;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    private String linkPrecedence;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    private Long id;
}
