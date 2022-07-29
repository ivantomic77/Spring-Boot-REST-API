package com.example.accessdatawjpa.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean active;

    protected Customer() {}

    public Customer(String firstName, String lastName, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
    }
}
