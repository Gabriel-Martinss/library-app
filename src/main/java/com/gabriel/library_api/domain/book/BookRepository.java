package com.gabriel.library_api.domain.book;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByStatus(BookStatus status);
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
}
