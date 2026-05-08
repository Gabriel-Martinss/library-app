package com.gabriel.library_api.domain.loan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
       long countByUserIdAndStatus(Long userId, LoanStatus status);
       List<Loan> findByUserId(Long userId);
       List<Loan> findByStatus(LoanStatus status);
}
