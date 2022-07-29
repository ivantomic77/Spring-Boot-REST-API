package com.example.accessdatawjpa.service;

import com.example.accessdatawjpa.exception.CustomerNotFoundException;
import com.example.accessdatawjpa.model.Customer;
import com.example.accessdatawjpa.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CustomerRepo repo;

    public List<Customer> findAllActive(boolean active){
        return repo.findByActive(active);
    }

    public List<Customer> findAll() {return repo.findAll();}

    public Customer createCustomer(Customer newCustomer){return repo.save(newCustomer);}

    public Customer findByID(Long id){return repo.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));}

    public Customer replaceCustomer(Customer newCustomer, Long id){
        return repo.findById(id)
                .map(customer -> {
                    customer.setFirstName(newCustomer.getFirstName());
                    customer.setLastName(newCustomer.getLastName());
                    customer.setActive(newCustomer.getActive());
                    return repo.save(customer);
                })
                .orElseGet(() -> {
                    newCustomer.setId(id);
                    return repo.save(newCustomer);
                });
    }

    public void deleteCustomer(Long id){repo.deleteById(id);}
}
