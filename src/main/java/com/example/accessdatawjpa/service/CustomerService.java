package com.example.accessdatawjpa.service;

import com.example.accessdatawjpa.exception.CustomerNotFoundException;
import com.example.accessdatawjpa.model.Customer;
import com.example.accessdatawjpa.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CustomerRepo repo;

    public List<Customer> findAllActive(boolean active){
        return repo.findByActive(active);
    }

    public List<Customer> findAll() {return repo.findAll();}

    public Customer createCustomer(String firstName,String lastName,Boolean active, MultipartFile file) throws IOException {
        String pathDir = new ClassPathResource("static/files/").getFile().getAbsolutePath();
        Files.copy(file.getInputStream(), Paths.get(pathDir+ File.separator+file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(fileName + " " + firstName);
        Customer customer =  new Customer(firstName,lastName,active,pathDir,fileName, file.getContentType());
        return repo.save(customer);
    }

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
