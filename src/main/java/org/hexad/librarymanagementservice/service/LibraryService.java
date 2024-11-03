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
import java.util.stream.Collectors;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public LibraryService(@Autowired BookRepository bookRepository, @Autowired UserRepository userRepository, @Autowired BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public List<Book> viewBooks() {
        return bookRepository.findAll();
    }

    public List<Book> viewBorrowedBooks(String userName, String phoneNumber) {
        return getBorrowedBooks(userRepository.findByNameAndPhoneNumber(userName, phoneNumber)).stream()
                .map(BorrowRecord::getBook)
                .collect(Collectors.toList());

    }

    public List<BorrowRecord> viewBorrowedBooks() {
        return borrowRecordRepository.findAll();
    }

    public String borrowBook(String userName, String phoneNumber, Long bookId) {
        User user = userRepository.findByNameAndPhoneNumber(userName, phoneNumber);
        Book book = bookRepository.findById(bookId);

        if (book == null) {
            return "book not found";
        }

        if (user == null) {
            return "user not found";
        }

        if (getBorrowedBooks(user).size() >= user.getBorrowingLimit()) {
            return "Borrowing limit reached";
        }

        if (book.getCopiesAvailable() > 0 && getBorrowedBooks(user).stream().noneMatch(b -> b.getBook().getId().equals(book.getId()))) {
            try {
                BorrowRecord borrowRecord = new BorrowRecord();
                borrowRecord.setId((long) (borrowRecordRepository.findAll().size() + 1));
                borrowRecord.setBook(book);
                borrowRecord.setBorrowDate(Date.valueOf(LocalDate.now()));
                borrowRecordRepository.save(borrowRecord);

                List<BorrowRecord> existingBorrowRecords = getBorrowedBooks(user);
                existingBorrowRecords.add(borrowRecord);
                user.setBorrowedBooks(existingBorrowRecords);
//                userRepository.updateUser(user.getId(),existingBorrowRecords);

                bookRepository.updateBookQuantity(book.getId(), book.getCopiesAvailable() - 1);
                return "Book borrowed successfully";
            } catch (Exception e) {
                return "Failed to borrow book " + e;
            }
        } else if (getBorrowedBooks(user).stream().noneMatch(b -> b.getBook().equals(book))) {
            return "Book already borrowed";
        } else {
            return "Book not available or already borrowed";
        }
    }

    private static List<BorrowRecord> getBorrowedBooks(User user) {
        return user.getBorrowedBooks();
    }

    public String returnBook(String userName, String phoneNumber, Long bookId) {
        User user = userRepository.findByNameAndPhoneNumber(userName, phoneNumber);
        Book book = bookRepository.findById(bookId);
        List<BorrowRecord> existingBorrowRecords = getBorrowedBooks(user);

        Optional<BorrowRecord> borrowRecord = existingBorrowRecords.stream().filter(b -> b.getBook().getId().equals(bookId)).findFirst();
        System.out.println("user borrowRecordOptional = " + borrowRecord);

        if (borrowRecord.isPresent()) {
            existingBorrowRecords.remove(borrowRecord.get());
            user.setBorrowedBooks(existingBorrowRecords);
            System.out.println("user existingBorrowRecords = " + existingBorrowRecords);
            userRepository.updateUser(user.getId(), existingBorrowRecords);
            borrowRecordRepository.delete(borrowRecord.get());
            borrowRecordRepository.findAll().forEach(System.out::println);
            bookRepository.updateBookQuantity(book.getId(), book.getCopiesAvailable() + 1);

            return "Book returned successfully";
        } else {
            return "No such borrowed book found";
        }
    }
}
