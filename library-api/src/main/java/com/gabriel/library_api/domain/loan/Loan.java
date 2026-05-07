package com.gabriel.library_api.domain.loan;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.gabriel.library_api.domain.user.User;
import com.gabriel.library_api.domain.book.Book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "loans")
public class Loan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "loan_date", nullable = false, updatable = false)
    private LocalDateTime loanDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "return_date", nullable = true)
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private LoanStatus status;

    public Loan(User user, Book book) {
        this.user = user;
        this.book = book;
        this.loanDate = LocalDateTime.now();
        this.dueDate = this.loanDate.plusDays(7);
        this.status = LoanStatus.ACTIVE;
    }

    public void returnLoan(){
        if (this.status == LoanStatus.ACTIVE) {
            throw new IllegalStateException("Cannot return a loan that is still active.");
        }

        this.returnDate = LocalDateTime.now();
        this.status = this.returnDate.isAfter(this.dueDate) ? LoanStatus.LATE : LoanStatus.RETURNED;
    }

}
