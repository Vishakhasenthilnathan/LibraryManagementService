package org.hexad.librarymanagementservice.controller;


import org.hexad.librarymanagementservice.model.Book;
import org.hexad.librarymanagementservice.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryController.class)
public class LibraryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;

    @BeforeEach
    void setup() {
    }

    @Test
    public void should_return_empty_library_when_no_books_present() throws Exception {
        when(libraryService.viewBooks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/library/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void should_return_list_of_books_from_library_when_books_present() throws Exception {
        when(libraryService.viewBooks()).thenReturn(books);

        mockMvc.perform(get("/library/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(book.getId()))
                .andExpect(jsonPath("$[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$[1].id").value(book2.getId()))
                .andExpect(jsonPath("$[1].title").value(book2.getTitle()));
    }

    @Test
    public void should_borrow_book_successfully() throws Exception {
        Long userId = 1L;
        Long bookId = 1L;
        when(libraryService.borrowBook(userId, bookId)).thenReturn("Book borrowed successfully");

        mockMvc.perform(post("/library/borrow/{userId}/{bookId}", userId, bookId))
                .andExpect(status().isOk())
                .andExpect(content().string("Book borrowed successfully"));
    }

    @Test
    public void should_return_book_successfully() throws Exception {
        Long userId = 1L;
        Long bookId = 1L;
        when(libraryService.returnBook(userId, bookId)).thenReturn("Book returned successfully");

        mockMvc.perform(post("/library/return/{userId}/{bookId}", userId, bookId))
                .andExpect(status().isOk())
                .andExpect(content().string("Book returned successfully"));
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
