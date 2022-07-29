package com.example.accessdatawjpa.repository;

import java.util.List;

import com.example.accessdatawjpa.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepo extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);
    List<Customer> findAll();
    List<Customer> findByActive(Boolean Active);
    Customer findById(long id);
}
