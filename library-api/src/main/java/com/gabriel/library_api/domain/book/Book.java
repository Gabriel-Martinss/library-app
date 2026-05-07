package com.gabriel.library_api.domain.book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.JoinColumn;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "isbn", nullable = false, unique = true, length = 20)
    private String isbn;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BookStatus status;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Long author;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Long category;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Book(String title, String isbn, BookStatus status, Long author, Long category) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.category = category;
        this.status = BookStatus.AVAILABLE;
    }

    public void borrow() {
        if (this.status != BookStatus.AVAILABLE) {
            throw new IllegalStateException("Book is not available for borrowing.");
        // TODO: Implement specific exception handling for better error management
        }
        this.status = BookStatus.BORROWED;
    }

    public void returnBook() {
        if (this.status != BookStatus.BORROWED) {
            throw new IllegalStateException("Book is not currently borrowed."); 
        // TODO: Implement specific exception handling for better error management
        }
        this.status = BookStatus.AVAILABLE;
    }
}
