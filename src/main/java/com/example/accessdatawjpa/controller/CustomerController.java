package com.example.accessdatawjpa.controller;

import java.util.List;

import com.example.accessdatawjpa.exception.CustomerNotFoundException;
import com.example.accessdatawjpa.model.Customer;
import com.example.accessdatawjpa.repository.CustomerRepo;
import com.example.accessdatawjpa.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    private final CustomerRepo repository;

    @Autowired
    CustomerService service;

    CustomerController(CustomerRepo repository) {
        this.repository = repository;
    }

    @GetMapping("/customers/s")
    List<Customer> allActive(@RequestParam(name = "active") Boolean active) {
        return service.findAllActive(active);
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/customers")
    List<Customer> all() {
        return service.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/customersPost")
    Customer newCustomer(@RequestBody Customer newCustomer) {
        return service.createCustomer(newCustomer);
    }

    // Single item

    @GetMapping("/customer/{id}")
    Customer one(@PathVariable Long id) {
        return service.findByID(id);
    }

    @PutMapping("/customer/{id}")
    Customer replaceEmployee(@RequestBody Customer newCustomer, @PathVariable Long id) {
        return service.replaceCustomer(newCustomer,id);
    }

    @DeleteMapping("/customer/{id}")
    void deleteEmployee(@PathVariable Long id) {
        service.deleteCustomer(id);
    }
}