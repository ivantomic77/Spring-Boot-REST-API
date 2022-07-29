package com.example.accessdatawjpa;

import com.example.accessdatawjpa.model.Customer;
import com.example.accessdatawjpa.repository.CustomerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccessDataWjpaApplication {
    private static final Logger log = LoggerFactory.getLogger(AccessDataWjpaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AccessDataWjpaApplication.class);
    }

    @Bean
    public CommandLineRunner demo(CustomerRepo repository) {
        return (args) -> {
            // save a few customers
            repository.save(new Customer("Jack", "Bauer",true));
            repository.save(new Customer("Chloe", "O'Brian",false));
            repository.save(new Customer("Kim", "Bauer",true));
            repository.save(new Customer("David", "Palmer",false));
            repository.save(new Customer("Michelle", "Dessler",true));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Customer customer : repository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            Customer customer = repository.findById(1L);
            log.info("Customer found with findById(1L):");
            log.info("--------------------------------");
            log.info(customer.toString());
            log.info("");

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findByLastName("Bauer").forEach(bauer -> {
                log.info(bauer.toString());
            });
            // for (Customer bauer : repository.findByLastName("Bauer")) {
            //  log.info(bauer.toString());
            // }
            log.info("");
        };
    }

}
