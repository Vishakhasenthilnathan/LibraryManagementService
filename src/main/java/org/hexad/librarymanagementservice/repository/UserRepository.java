package org.hexad.librarymanagementservice.repository;

import org.hexad.librarymanagementservice.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Book, Long> { }