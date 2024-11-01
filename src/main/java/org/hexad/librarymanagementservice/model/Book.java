package org.hexad.librarymanagementservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    private String title;
    private String author;
    private String isbn;
    private String publicationDate;
    private String genre;
    private String language;
    private String publisher;
    private String edition;
    private String numberOfPages;
    private String description;
    private String status;

}
