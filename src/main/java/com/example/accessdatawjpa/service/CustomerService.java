package com.example.accessdatawjpa.service;

import com.example.accessdatawjpa.exception.CustomerNotFoundException;
import com.example.accessdatawjpa.model.Customer;
import com.example.accessdatawjpa.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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

    public ResponseEntity downloadFile(Long id){
        Customer customer = findByID(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(customer.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + customer.getFileName() + "\"")
                .body(customer.getFile());
    }

    public Customer createCustomer(String firstName,String lastName,Boolean active, MultipartFile file) throws IOException {
        String pathDir = new ClassPathResource("static/files/").getFile().getAbsolutePath();
        Files.copy(file.getInputStream(), Paths.get(pathDir+ File.separator+file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        Customer customer =  new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setFilePath(pathDir);
        customer.setActive(active);
        customer.setFileName(fileName);
        customer.setFileType(file.getContentType());

        try {
            customer.setFile(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return repo.save(customer);
    }

    public Customer findByID(Long id){return repo.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));}

    public Customer replaceCustomer(Customer newCustomer, Long id){
        return repo.findById(id)
                .map(customer -> {
                    customer.setFirstName(newCustomer.getFirstName());
                    customer.setLastName(newCustomer.getLastName());
                    customer.setActive(newCustomer.getActive());
                    customer.setFile(newCustomer.getFile());
                    customer.setFileName(newCustomer.getFileName());
                    customer.setFileType(newCustomer.getFileType());
                    customer.setFilePath(newCustomer.getFilePath());
                    return repo.save(customer);
                })
                .orElseGet(() -> {
                    newCustomer.setId(id);
                    return repo.save(newCustomer);
                });
    }

    public void deleteCustomer(Long id){repo.deleteById(id);}
}
