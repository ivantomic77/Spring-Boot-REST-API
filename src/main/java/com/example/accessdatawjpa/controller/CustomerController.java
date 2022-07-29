package com.example.accessdatawjpa.controller;

import java.util.List;

import com.example.accessdatawjpa.exception.CustomerNotFoundException;
import com.example.accessdatawjpa.model.Customer;
import com.example.accessdatawjpa.repository.CustomerRepo;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    private final CustomerRepo repository;

    CustomerController(CustomerRepo repository) {
        this.repository = repository;
    }

    @GetMapping("/customers/s")
    List<Customer> allActive(@RequestParam(name = "active") Boolean active) {
        return repository.findByActive(active);
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/customers")
    List<Customer> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/customersPost")
    Customer newEmployee(@RequestBody Customer newCustomer) {
        return repository.save(newCustomer);
    }

    // Single item

    @GetMapping("/customer/{id}")
    Customer one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @PutMapping("/customer/{id}")
    Customer replaceEmployee(@RequestBody Customer newCustomer, @PathVariable Long id) {

        return repository.findById(id)
                .map(customer -> {
                    customer.setFirstName(newCustomer.getFirstName());
                    customer.setLastName(newCustomer.getLastName());
                    customer.setActive(newCustomer.getActive());
                    return repository.save(customer);
                })
                .orElseGet(() -> {
                    newCustomer.setId(id);
                    return repository.save(newCustomer);
                });
    }

    @DeleteMapping("/customer/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}