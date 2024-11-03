package org.hexad.librarymanagementservice.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.hexad.librarymanagementservice.model.BorrowRecord;
import org.hexad.librarymanagementservice.model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Repository
@Component
public class UserRepository {

    private List<User> users = new ArrayList<>();

    @PostConstruct
    public void loadJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<User>> typeReference = new TypeReference<>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream("/Users.json");

        try {
            users = mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON data", e);
        }
    }

    public List<User> findAll() {
        return users;
    }

    public User findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public User findByNameAndPhoneNumber(String userName, String phoneNumber) {
        return users.stream()
                .filter(user -> user.getName().equals(userName) && user.getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElse(null);
    }

    public void save(User user) {
        users.add(user);
    }

    public void updateUser(Long userId, List<BorrowRecord> borrowedBooks) {
        System.out.println("updateUser userId = " + userId+" borrowedBooks = "+borrowedBooks);
        users.stream().filter(user -> user.getId().equals(userId)).forEach(user -> user.setBorrowedBooks(borrowedBooks));
        System.out.println("updateUser done =  "+users);

    }

    public void deleteById(Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}