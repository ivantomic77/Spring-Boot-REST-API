package com.example.accessdatawjpa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    @JsonProperty("active")
    private Boolean active;
    private String fileName;
    private String filePath;
    private String fileType;
    @Lob
    @JsonIgnore
    private byte[] file;


    public Customer() {}

    public Customer(String firstName, String lastName, Boolean active, String fileName, String filePath, byte[] file, String fileType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
        this.fileName = fileName;
        this.filePath = filePath;
        this.file = file;
        this.fileType = fileType;
    }
}
