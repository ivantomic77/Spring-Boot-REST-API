package com.example.accessdatawjpa.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import com.example.accessdatawjpa.exception.CustomerNotFoundException;
import com.example.accessdatawjpa.model.Customer;
import com.example.accessdatawjpa.repository.CustomerRepo;
import com.example.accessdatawjpa.service.CustomerService;
import com.sun.xml.bind.api.impl.NameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CustomerController {

    private final CustomerRepo repository;

    @Autowired
    CustomerService service;

    CustomerController(CustomerRepo repository) {
        this.repository = repository;
    }

    @GetMapping("/customer/active")
    List<Customer> allActive(@RequestParam(name = "active") Boolean active) {
        return service.findAllActive(active);
    }

    @GetMapping("/customer")
    List<Customer> all() {
        return service.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/customer/new")
    public Customer newCustomer(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,@RequestParam("active") Boolean active, @RequestParam("file") MultipartFile file) throws Exception{
        return service.createCustomer(firstName,lastName,active,file);
    }

    @GetMapping("customer/getFile/{id}")
    public ResponseEntity downloadFileFromLocal(@PathVariable Long id){
        return service.downloadFile(id);
    }

    // Single item

    @GetMapping("/customer/get/{id}")
    Customer one(@PathVariable Long id) {
        return service.findByID(id);
    }

    @PutMapping("/customer/put/{id}")
    Customer replaceEmployee(@RequestBody Customer newCustomer, @PathVariable Long id) {return service.replaceCustomer(newCustomer,id);}

    @DeleteMapping("/customer/delete/{id}")
    void deleteEmployee(@PathVariable Long id) {
        service.deleteCustomer(id);
    }

}