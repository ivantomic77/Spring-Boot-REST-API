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
import org.springframework.web.bind.annotation.RequestParam;
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

    public List<Customer> findAllActive(boolean active) {
        return repo.findByActive(active);
    }

    public List<Customer> findAll() {
        return repo.findAll();
    }

    public ResponseEntity downloadFile(Long id) {
        Customer customer = findByID(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(customer.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + customer.getFileName() + "\"")
                .body(customer.getFile());
    }

    public Customer createCustomer(String firstName, String lastName, Boolean active, MultipartFile file) throws IOException {
        String pathDir = new ClassPathResource("static/files/").getFile().getAbsolutePath();
        Files.copy(file.getInputStream(), Paths.get(pathDir + File.separator + file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        Customer customer = new Customer();
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

    public Customer findByID(Long id) {
        return repo.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public Customer replaceCustomer(Long id, String firstName, String lastName,Boolean active, MultipartFile file) throws IOException {
        String pathDir = new ClassPathResource("static/files/").getFile().getAbsolutePath();
        Files.copy(file.getInputStream(), Paths.get(pathDir + File.separator + file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Customer newCustomer = new Customer();
        return repo.findById(id)
                .map(customer -> {
                    customer.setFirstName(firstName);
                    customer.setLastName(lastName);
                    customer.setActive(active);
                    try {
                        customer.setFile(file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    customer.setFileName(fileName);
                    customer.setFileType(file.getContentType());
                    customer.setFilePath(pathDir);
                    return repo.save(customer);
                })
                .orElseGet(() -> {
                    newCustomer.setId(id);
                    newCustomer.setFirstName(firstName);
                    newCustomer.setLastName(lastName);
                    newCustomer.setActive(active);
                    try {
                        newCustomer.setFile(file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    newCustomer.setFileName(fileName);
                    newCustomer.setFileType(file.getContentType());
                    newCustomer.setFilePath(pathDir);
                    return repo.save(newCustomer);
                });
    }

    public void deleteCustomer(Long id) {
        repo.deleteById(id);
    }
}
