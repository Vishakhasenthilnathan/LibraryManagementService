package org.hexad.librarymanagementservice.controller;

import org.hexad.librarymanagementservice.model.Book;
import org.hexad.librarymanagementservice.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService libraryService;

    public LibraryController(@Autowired LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/books")
    public List<Book> viewBooks() {
        return libraryService.viewBooks();
    }

    @GetMapping("/{userId}/books")
    public List<Book> viewBorrowedBooksByUser(@PathVariable Long userId) {
        return libraryService.viewBorrowedBooks(userId);
    }

    @PostMapping("/borrow/{userId}/{bookId}")
    public String borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return libraryService.borrowBook(userId, bookId);
    }

    @PostMapping("/return/{userId}/{bookId}")
    public String returnBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return libraryService.returnBook(userId, bookId);
    }
}
