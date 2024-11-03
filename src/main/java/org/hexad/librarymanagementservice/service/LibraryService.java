package org.hexad.librarymanagementservice.service;

import org.hexad.librarymanagementservice.model.Book;
import org.hexad.librarymanagementservice.model.BorrowRecord;
import org.hexad.librarymanagementservice.model.User;
import org.hexad.librarymanagementservice.repository.BookRepository;
import org.hexad.librarymanagementservice.repository.BorrowRecordRepository;
import org.hexad.librarymanagementservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    @Autowired
    public LibraryService(BookRepository bookRepository, UserRepository userRepository, BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public List<Book> viewBooks() {
        return bookRepository.findAll();
    }

    public List<Book> viewBorrowedBooks(String userName, String phoneNumber) {
        User user = userRepository.findByNameAndPhoneNumber(userName, phoneNumber);
        return getBorrowedBooks(user).stream()
                .map(BorrowRecord::getBook)
                .collect(Collectors.toList());
    }

    public String borrowBook(String userName, String phoneNumber, Long bookId) {
        User user = userRepository.findByNameAndPhoneNumber(userName, phoneNumber);
        Book book = bookRepository.findById(bookId);

        if (book == null) {
            return "Book not found";
        }

        if (user == null) {
            return "User is invalid";
        }

        if (isBookAlreadyBorrowed(user, book)) {
            return "Book already borrowed";
        }

        if (getBorrowedBooks(user).size() >= user.getBorrowingLimit()) {
            return "Borrowing limit reached";
        }

        if (book.getCopiesAvailable() > 0) {
            return processBorrowing(user, book);
        } else {
            return "Book not available";
        }
    }

    private boolean isBookAlreadyBorrowed(User user, Book book) {
        return getBorrowedBooks(user).stream()
                .anyMatch(b -> b.getBook().getId().equals(book.getId()));
    }

    private String processBorrowing(User user, Book book) {
        try {
            BorrowRecord borrowRecord = new BorrowRecord();
            borrowRecord.setBook(book);
            borrowRecord.setBorrowDate(Date.valueOf(LocalDate.now()));
            borrowRecordRepository.save(borrowRecord);

            List<BorrowRecord> existingBorrowRecords =  new ArrayList<>(getBorrowedBooks(user));
            existingBorrowRecords.add(borrowRecord);
            user.setBorrowedBooks(existingBorrowRecords);

            bookRepository.updateBookQuantity(book.getId(), book.getCopiesAvailable() - 1);
            return "Book borrowed successfully";
        } catch (Exception e) {
            return "Failed to borrow book: " + e.getMessage();
        }
    }

    private List<BorrowRecord> getBorrowedBooks(User user) {
        return user.getBorrowedBooks();
    }

    public String returnBook(String userName, String phoneNumber, Long bookId) {
        User user = userRepository.findByNameAndPhoneNumber(userName, phoneNumber);
        Book book = bookRepository.findById(bookId);

        List<BorrowRecord> bw = getBorrowedBooks(user);
        Optional<BorrowRecord> borrowRecord = bw.stream()
                .filter(b -> b.getBook().equals(book))
                .findFirst();

        return borrowRecord.map(record -> processReturning(user, book, record)).orElse("No such borrowed book found");
    }

    private String processReturning(User user, Book book, BorrowRecord borrowRecord) {
        List<BorrowRecord> existingBorrowRecords = new ArrayList<>(getBorrowedBooks(user));
        existingBorrowRecords.remove(borrowRecord);
        user.setBorrowedBooks(existingBorrowRecords);
        userRepository.updateUser(user.getId(), existingBorrowRecords);
        borrowRecordRepository.delete(borrowRecord);
        bookRepository.updateBookQuantity(book.getId(), book.getCopiesAvailable() + 1);

        return "Book returned successfully";
    }
}