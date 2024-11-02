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
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    public List<Book> viewBooks() {
        return bookRepository.findAll();
    }

    public List<Book> viewBorrowedBooks(long userId) {
        return userRepository.findById(userId).getBorrowedBooks().stream()
                .map(BorrowRecord::getBook)
                .toList();
    }

    public String borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId);
        Book book = bookRepository.findById(bookId);

        if(book == null) {
            return "book not found";
        }

        if(user == null) {
            return "user not found";
        }

        if (user.getBorrowedBooks().size() >= user.getBorrowingLimit()) {
            return "Borrowing limit reached";
        }

        if (book.getCopiesAvailable() > 0 && user.getBorrowedBooks().stream().noneMatch(b -> b.getBook().equals(book))) {
            try {
                BorrowRecord borrowRecord = new BorrowRecord();
                borrowRecord.setId((long) (borrowRecordRepository.findAll().size() + 1));
                borrowRecord.setUser(user);
                borrowRecord.setBook(book);
                borrowRecord.setBorrowDate(Date.valueOf(LocalDate.now()));

                bookRepository.updateBookQuantity(book.getId(),book.getCopiesAvailable() -1);
                System.out.println("Book copies available: "+book.getCopiesAvailable());
                System.out.println("borrowRecord: "+borrowRecord);
                borrowRecordRepository.save(borrowRecord);

                return "Book borrowed successfully";
            }
            catch (Exception e) {
                return "Failed to borrow book "+e;
            }
        } else {
            return "Book not available or already borrowed";
        }
    }

    public String returnBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId);
        Book book = bookRepository.findById(bookId);

        Optional<BorrowRecord> borrowRecordOptional = user.getBorrowedBooks().stream()
                .filter(b -> b.getBook().equals(book))
                .findFirst();

        if (borrowRecordOptional.isPresent()) {
            BorrowRecord borrowRecord = borrowRecordOptional.get();
            user.getBorrowedBooks().remove(borrowRecord);
            borrowRecordRepository.delete(borrowRecord);

            bookRepository.updateBookQuantity(book.getId(), book.getCopiesAvailable()+1);

            return "Book returned successfully";
        } else {
            return "No such borrowed book found";
        }
    }
}
