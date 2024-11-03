package org.hexad.librarymanagementservice.controller;

import org.hexad.librarymanagementservice.model.Book;
import org.hexad.librarymanagementservice.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LibraryControllerTest {

    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private LibraryController libraryController;

    private Book book;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        book = new Book(1L, "The Alchemist", "Paulo Coelho", null, "Fiction", "English", "1st", "A book about following your dreams", 10, "", "");
    }

    @Test
    public void should_return_empty_list_when_no_books_present() {
        when(libraryService.viewBooks()).thenReturn(Collections.emptyList());

        List<Book> result = libraryController.viewBooks();
        assertEquals(0, result.size());
    }

    @Test
    public void should_return_list_of_books_when_books_present() {
        when(libraryService.viewBooks()).thenReturn(List.of(book));

        List<Book> result = libraryController.viewBooks();
        assertEquals(1, result.size());
        assertEquals("The Alchemist", result.get(0).getTitle());
    }

    @Test
    public void should_return_borrowed_books_by_user() {
        String userName = "John Doe";
        String phoneNumber = "1234567890";
        when(libraryService.viewBorrowedBooks(userName, phoneNumber)).thenReturn(List.of(book));

        List<Book> result = libraryController.viewBorrowedBooksByUser(userName, phoneNumber);
        assertEquals(1, result.size());
        assertEquals("The Alchemist", result.get(0).getTitle());
    }

    @Test
    public void should_borrow_book_successfully() {
        String userName = "John Doe";
        String phoneNumber = "1234567890";
        Long bookId = 1L;
        when(libraryService.borrowBook(userName, phoneNumber, bookId)).thenReturn("Book borrowed successfully");

        ResponseEntity<String> response = libraryController.borrowBook(userName, phoneNumber, bookId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book borrowed successfully", response.getBody());
    }

    @Test
    public void should_return_book_successfully() {
        String userName = "John Doe";
        String phoneNumber = "1234567890";
        Long bookId = 1L;
        when(libraryService.returnBook(userName, phoneNumber, bookId)).thenReturn("Book returned successfully");

        ResponseEntity<String> response = libraryController.returnBook(userName, phoneNumber, bookId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book returned successfully", response.getBody());
    }
}