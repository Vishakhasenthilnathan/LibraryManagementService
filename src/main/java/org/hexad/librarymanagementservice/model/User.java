package org.hexad.librarymanagementservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "\"User\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String email;
    private String phoneNumber;
    private String address;
//
//    @OneToMany
//    private List<Book> borrowedBooks = new ArrayList<>();

    private final int borrowLimit = 2;

}
