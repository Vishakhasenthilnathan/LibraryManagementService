package org.hexad.librarymanagementservice.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.hexad.librarymanagementservice.model.Book;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Repository
@Component
public class BookRepository  {

    private List<Book> books = new ArrayList<>();

    @PostConstruct
    public void loadJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Book>> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/Books.json");

        try {
            books = mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON data", e);
        }
    }

    public List<Book> findAll() {
        return books;
    }

    public Book findById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(Book book) {
        books.add(book);
    }

    public void updateBookQuantity(Long bookId,int bookQuantity) {
        books.stream().filter(book -> Objects.equals(book.getId(), bookId)).forEach(book -> book.setCopiesAvailable(bookQuantity));
    }

    public void deleteById(Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }

}