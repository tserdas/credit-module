package com.inghub.creditmodule.service.impl;

import com.inghub.creditmodule.controller.request.CreateLoanRequest;
import com.inghub.creditmodule.controller.response.CreateLoanResponse;
import com.inghub.creditmodule.controller.response.GetLoanResponse;
import com.inghub.creditmodule.repository.CustomerRepository;
import com.inghub.creditmodule.repository.LoanInstallmentRepository;
import com.inghub.creditmodule.repository.LoanRepository;
import com.inghub.creditmodule.repository.entity.Customer;
import com.inghub.creditmodule.repository.entity.Loan;
import com.inghub.creditmodule.repository.entity.LoanInstallment;
import com.inghub.creditmodule.service.LoanService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, CustomerRepository customerRepository, LoanInstallmentRepository loanInstallmentRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.loanInstallmentRepository = loanInstallmentRepository;
    }

    @Transactional
    @Override
    public CreateLoanResponse createLoan(CreateLoanRequest request) {
        /**
         * customer table should not be empty. Credit Module requirements does not contain customer APIs, so I assume that the h2 db will fill manually like;
         * insert into customer values (1,'Turgay','Serdaş',10000,2000);
         * because I need to validate customer limit
         */
        Customer customer = this.customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found!"));
        BigDecimal loanAmount = request.getAmount().multiply(BigDecimal.ONE.add(request.getInterestRate()));
        if (customer.getUsedCreditLimit().add(loanAmount).compareTo(customer.getCreditLimit()) > 0) {
            throw new RuntimeException("No limit for new credit!");
        }
        Loan loan = this.createLoan(customer.getId(), loanAmount, request.getNumberOfInstallments());
        this.createLoanInstallment(loan, loanAmount);
        customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(loanAmount));
        return new CreateLoanResponse(loan.getId(), loanAmount, request.getNumberOfInstallments());
    }

    @Override
    public List<GetLoanResponse> getLoan(Long customerId) {
        List<GetLoanResponse> getLoanResponseList = new ArrayList<>();
        List<Loan> loanList = this.loanRepository.findByCustomerId(customerId);
        for (Loan loan : loanList) {
            GetLoanResponse getLoanResponse = new GetLoanResponse();
            getLoanResponse.setLoanId(loan.getId());
            getLoanResponse.setLoanAmount(loan.getLoanAmount());
            getLoanResponse.setCustomerId(loan.getCustomerId());
            getLoanResponse.setPaid(loan.getPaid());
            getLoanResponse.setCreateDate(loan.getCreateDate());
            getLoanResponse.setNumberOfInstallment(loan.getNumberOfInstallment());
            getLoanResponseList.add(getLoanResponse);
        }
        return getLoanResponseList;
    }

    @Override
    public Long getCustomerIdByLoan(Long loanId) {
        return this.loanRepository.findById(loanId).map(Loan::getCustomerId).orElseThrow(() -> new EntityNotFoundException("Loan not found!"));
    }

    private Loan createLoan(Long customerId, BigDecimal loanAmount, Integer numberOfInstallment) {
        Loan loan = new Loan();
        loan.setCustomerId(customerId);
        loan.setLoanAmount(loanAmount);
        loan.setPaid(false);
        loan.setNumberOfInstallment(numberOfInstallment);
        loan.setCreateDate(OffsetDateTime.now());
        return this.loanRepository.save(loan);
    }

    private void createLoanInstallment(Loan loan, BigDecimal loanAmount) {
        BigDecimal installmentAmount = loanAmount.divide(BigDecimal.valueOf(loan.getNumberOfInstallment()), 2, RoundingMode.HALF_UP);
        LocalDate dueDate = LocalDate.now().withDayOfMonth(1).plusMonths(1);
        List<LoanInstallment> loanInstallmentList = new ArrayList<>();
        for (int i = 0; i < loan.getNumberOfInstallment(); i++) {
            LoanInstallment loanInstallment = new LoanInstallment();
            loanInstallment.setLoanId(loan.getId());
            loanInstallment.setPaid(false);
            loanInstallment.setAmount(installmentAmount);
            loanInstallment.setDueDate(dueDate.plusMonths(i));
            loanInstallment.setPaidAmount(BigDecimal.ZERO);
            loanInstallmentList.add(loanInstallment);
        }
        if (loanInstallmentList.size() == loan.getNumberOfInstallment())
            this.loanInstallmentRepository.saveAll(loanInstallmentList);
        else
            throw new RuntimeException("System Exception!");
    }
}
