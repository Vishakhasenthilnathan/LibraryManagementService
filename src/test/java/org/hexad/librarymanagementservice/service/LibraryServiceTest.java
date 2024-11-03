package org.hexad.librarymanagementservice.service;

import org.hexad.librarymanagementservice.model.Book;
import org.hexad.librarymanagementservice.model.BorrowRecord;
import org.hexad.librarymanagementservice.model.User;
import org.hexad.librarymanagementservice.repository.BookRepository;
import org.hexad.librarymanagementservice.repository.BorrowRecordRepository;
import org.hexad.librarymanagementservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class LibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @InjectMocks
    private LibraryService libraryService;

    private User user;
    private Book book;
    private BorrowRecord borrowRecord;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "John Doe", "", "1234567890", "Bangalore", null);
        book = new Book(1L, "The Alchemist", "Paulo Coelho", getDate("1988-10-10"), "", null, null, null, 1, "", "");
        borrowRecord = new BorrowRecord(1L, new User(1L, "John Doe", "", "1234567890", "Bangalore", null), book, getDate("2021-10-10"));
        user.setBorrowedBooks(List.of(borrowRecord));
    }

    @Test
    public void should_return_empty_library_when_no_books_present() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<Book> result = libraryService.viewBooks();
        assertEquals(0, result.size());
    }

    @Test
    public void should_return_list_of_books_from_library_when_books_present() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<Book> result = libraryService.viewBooks();
        assertEquals(1, result.size());
        assertEquals("The Alchemist", result.get(0).getTitle());
    }

    @Test
    public void should_return_borrowed_books_by_user() {
        when(userRepository.findByNameAndPhoneNumber(user.getName(), user.getPhoneNumber())).thenReturn(user);
        when(borrowRecordRepository.findAll()).thenReturn(List.of(borrowRecord));

        List<Book> result = libraryService.viewBorrowedBooks(user.getName(), user.getPhoneNumber());
        assertEquals(1, result.size());
        assertEquals("The Alchemist", result.get(0).getTitle());
    }

    @Test
    public void should_not_allow_to_already_borrowed_book() {
        when(userRepository.findByNameAndPhoneNumber(user.getName(), user.getPhoneNumber())).thenReturn(user);
        when(bookRepository.findById(book.getId())).thenReturn(book);
        String result = libraryService.borrowBook(user.getName(), user.getPhoneNumber(), book.getId());
        assertEquals("Book already borrowed", result);
    }

    @Test
    public void should_allow_to_borrow_book_successfully() {
        when(userRepository.findByNameAndPhoneNumber(user.getName(), user.getPhoneNumber())).thenReturn(user);
        Book newbook = new Book(2L, "The Pride and Prejudice", "abc", getDate("1988-10-10"), "", null, null, null, 1, "", "");
        when(bookRepository.findById(newbook.getId())).thenReturn(newbook);
        String result = libraryService.borrowBook(user.getName(), user.getPhoneNumber(), newbook.getId());
        assertEquals("Book borrowed successfully", result);
    }

    @Test
    public void should_return_book_successfully() {
        when(userRepository.findByNameAndPhoneNumber(user.getName(), user.getPhoneNumber())).thenReturn(user);
        when(bookRepository.findById(book.getId())).thenReturn(book);
        when(borrowRecordRepository.findAll()).thenReturn(List.of(borrowRecord));
        String result = libraryService.returnBook(user.getName(), user.getPhoneNumber(), book.getId());
        assertEquals("Book returned successfully", result);
    }

    public java.util.Date getDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}