package org.hexad.librarymanagementservice.service;

import org.hexad.librarymanagementservice.model.Book;
import org.hexad.librarymanagementservice.model.BorrowRecord;
import org.hexad.librarymanagementservice.model.User;
import org.hexad.librarymanagementservice.repository.BookRepository;
import org.hexad.librarymanagementservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LibraryService libraryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_return_empty_library_when_no_books_present() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<Book> result = libraryService.viewBooks();
        assertEquals(0, result.size());
    }

    @Test
    public void should_return_list_of_books_from_library_when_books_present() {
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = libraryService.viewBooks();
        assertEquals(2, result.size());
        assertEquals("The Alchemist", result.get(0).getTitle());
        assertEquals("Love and Pain and the Whole Damn Thing", result.get(1).getTitle());
    }

    @Test
    public void should_return_borrowed_books_by_user() {
//        Long userId = 1L;
//        User user = new User(1L, "John Doe", "","","",null);
//        BorrowRecord borrowRecord = new BorrowRecord(1L,user, book, getDate("2021-06-01"));
//        when(userRepository.findById(userId).getBorrowedBooks()).thenReturn(List.of(borrowRecord));
//
//        List<Book> result = libraryService.viewBorrowedBooks(userId);
//        assertEquals(2, result.size());
//        assertEquals("The Alchemist", result.get(0).getTitle());
//        assertEquals("Love and Pain and the Whole Damn Thing", result.get(1).getTitle());
    }

    @Test
    public void should_borrow_book_successfully() {

    }

    @Test
    public void should_return_book_successfully() {

    }

    public Date getDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    Book book = new Book(1L, "The Alchemist", "Paulo Coelho", getDate("1992-12-10"), "Fiction", "English", "1st", "A book about following your dreams", 10);
    Book book2 = new Book(2L, "Love and Pain and the Whole Damn Thing", "Meredithe Akhurst", getDate("1998-06-5"), "Fiction", "English", "2nd", "Action|Adventure|Comedy", 6);
    List<Book> books = List.of(book, book2);
}