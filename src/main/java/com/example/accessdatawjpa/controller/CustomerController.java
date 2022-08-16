package com.example.accessdatawjpa.controller;

import com.example.accessdatawjpa.model.Customer;
import com.example.accessdatawjpa.repository.CustomerRepo;
import com.example.accessdatawjpa.service.CustomerService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
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

    @PostMapping("/customer/new")
    @ResponseBody
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
    Customer replaceEmployee(@PathVariable Long id, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,@RequestParam("active") Boolean active, @RequestParam(name="file", required=false) MultipartFile file) throws IOException {return service.replaceCustomer(id, firstName, lastName,active, file);}

    @DeleteMapping("/customer/delete/{id}")
    void deleteEmployee(@PathVariable Long id) {
        service.deleteCustomer(id);
    }

}
