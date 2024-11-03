package org.hexad.librarymanagementservice.controller;

import org.hexad.librarymanagementservice.model.Book;
import org.hexad.librarymanagementservice.model.BorrowRecord;
import org.hexad.librarymanagementservice.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/library/v1")
public class LibraryController {
    private final LibraryService libraryService;

    public LibraryController(@Autowired LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/books")
    public List<Book> viewBooks() {
        return libraryService.viewBooks();
    }

    @GetMapping("/borrowedBooks")
    public List<BorrowRecord> viewBorrowedBooks() {
        return libraryService.viewBorrowedBooks();
    }

    @GetMapping("/books/{userName}/{phoneNumber}")
    public List<Book> viewBorrowedBooksByUser(@PathVariable String userName, @PathVariable String phoneNumber) {
        return libraryService.viewBorrowedBooks(userName, phoneNumber);
    }

    @PostMapping("/borrow/{userName}/{phoneNumber}/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable String userName, @PathVariable String phoneNumber, @PathVariable Long bookId) {
        String result = libraryService.borrowBook(userName, phoneNumber, bookId);
        if ("Book borrowed successfully".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else if("Book already borrowed".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/return/{userName}/{phoneNumber}/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable String userName, @PathVariable String phoneNumber, @PathVariable Long bookId) {
        String result = libraryService.returnBook(userName, phoneNumber, bookId);
        if ("Book returned successfully".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }
}
