package org.hexad.librarymanagementservice.repository;

import org.hexad.librarymanagementservice.model.Book;
import org.hexad.librarymanagementservice.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Component
public class BorrowRecordRepository {
    private List<BorrowRecord> borrowRecords = new ArrayList<>();

    public List<BorrowRecord> findAll() {
        return borrowRecords;
    }

    public BorrowRecord findById(Long id) {
        return borrowRecords.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(BorrowRecord borrowRecord) {
        borrowRecords.add(borrowRecord);
    }

    public void deleteById(Long id) {
        borrowRecords.removeIf(book -> book.getId().equals(id));
    }
    public void delete(BorrowRecord borrowRecord) {
        borrowRecords.remove(borrowRecord);
    }
}
