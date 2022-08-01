package com.example.accessdatawjpa.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean active;
    private String path;
    private String fileName;
    private String fileType;

    public Customer() {}

    public Customer(String firstName, String lastName, Boolean active, String path, String fileName, String fileType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
        this.path = path;
        this.fileName = fileName;
        this.fileType = fileType;
    }
}
