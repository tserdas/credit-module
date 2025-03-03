package com.inghub.creditmodule.repository;

import com.inghub.creditmodule.repository.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {

    List<LoanInstallment> findByLoanId(Long loanId);

    List<LoanInstallment> findByLoanIdAndIsPaidFalseOrderByDueDateAsc(Long loanId);

    List<LoanInstallment> findByLoanIdAndIsPaidFalse(Long loanId);
}
