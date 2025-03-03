package com.inghub.creditmodule.service.impl;


import com.inghub.creditmodule.controller.request.PayLoanRequest;
import com.inghub.creditmodule.controller.response.GetInstallmentResponse;
import com.inghub.creditmodule.controller.response.PayLoanResponse;
import com.inghub.creditmodule.repository.CustomerRepository;
import com.inghub.creditmodule.repository.LoanInstallmentRepository;
import com.inghub.creditmodule.repository.LoanRepository;
import com.inghub.creditmodule.repository.entity.Customer;
import com.inghub.creditmodule.repository.entity.Loan;
import com.inghub.creditmodule.repository.entity.LoanInstallment;
import com.inghub.creditmodule.service.InstallmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstallmentServiceImpl implements InstallmentService {
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public InstallmentServiceImpl(LoanRepository loanRepository, LoanInstallmentRepository loanInstallmentRepository, CustomerRepository customerRepository) {
        this.loanRepository = loanRepository;
        this.loanInstallmentRepository = loanInstallmentRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Override
    public PayLoanResponse payLoan(PayLoanRequest request) {
        //validation
        Loan loan = this.loanRepository.findById(request.getLoanId()).orElseThrow(() -> new RuntimeException("Loan not found!"));
        if (Boolean.TRUE.equals(loan.getPaid())) {
            throw new RuntimeException("Loan already paid!");
        }
        List<LoanInstallment> loanInstallmentList = this.loanInstallmentRepository.findByLoanIdAndIsPaidFalseOrderByDueDateAsc(request.getLoanId());
        BigDecimal remainingAmount = request.getAmount();
        int paidInstallments = 0;
        BigDecimal totalPaid = BigDecimal.ZERO;
        //pay installments
        for (LoanInstallment loanInstallment : loanInstallmentList) {
            LocalDate today = LocalDate.now();
            if (loanInstallment.getAmount().compareTo(remainingAmount) > 0 || ChronoUnit.MONTHS.between(today, loanInstallment.getDueDate()) > 2) {
                break;
            }
            BigDecimal installmentAmount = loanInstallment.getAmount();

            if (today.isBefore(loanInstallment.getDueDate())) {
                long beforeDays = ChronoUnit.DAYS.between(today, loanInstallment.getDueDate());
                BigDecimal discount = loanInstallment.getAmount().multiply(BigDecimal.valueOf(.001)).multiply(BigDecimal.valueOf(beforeDays));
                installmentAmount = installmentAmount.subtract(discount);
            } else if (today.isAfter(loanInstallment.getDueDate())) {
                long afterDays = ChronoUnit.DAYS.between(loanInstallment.getDueDate(), today);
                BigDecimal penalty = loanInstallment.getAmount().multiply(BigDecimal.valueOf(.001)).multiply(BigDecimal.valueOf(afterDays));
                installmentAmount = installmentAmount.add(penalty);
            }
            remainingAmount = remainingAmount.subtract(installmentAmount);

            loanInstallment.setPaidAmount(installmentAmount);
            loanInstallment.setPaymentDate(today);
            loanInstallment.setPaid(true);
            totalPaid = totalPaid.add(installmentAmount);
            paidInstallments++;
            this.loanInstallmentRepository.save(loanInstallment);
        }
        //check loan completely paid
        List<LoanInstallment> loanInstallmentListAfterPayment = this.loanInstallmentRepository.findByLoanIdAndIsPaidFalse(request.getLoanId());
        if (loanInstallmentListAfterPayment.isEmpty()) {
            loan.setPaid(true);
        }
        //customer limit update
        Customer customer = this.customerRepository.findById(loan.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found!"));
        customer.setUsedCreditLimit(customer.getUsedCreditLimit().subtract(totalPaid));
        //response
        return new PayLoanResponse(paidInstallments, totalPaid, loan.getPaid());
    }

    @Override
    public List<GetInstallmentResponse> getInstallment(Long loanId) {
        List<GetInstallmentResponse> getInstallmentResponseList = new ArrayList<>();
        List<LoanInstallment> loanInstallmentList = this.loanInstallmentRepository.findByLoanId(loanId);
        for (LoanInstallment loanInstallment : loanInstallmentList) {
            GetInstallmentResponse getInstallmentResponse = getInstallmentResponse(loanInstallment);
            getInstallmentResponseList.add(getInstallmentResponse);
        }
        return getInstallmentResponseList;
    }

    private GetInstallmentResponse getInstallmentResponse(LoanInstallment loanInstallment) {
        GetInstallmentResponse getInstallmentResponse = new GetInstallmentResponse();
        getInstallmentResponse.setId(loanInstallment.getId());
        getInstallmentResponse.setAmount(loanInstallment.getAmount());
        getInstallmentResponse.setPaid(loanInstallment.getPaid());
        getInstallmentResponse.setDueDate(loanInstallment.getDueDate());
        getInstallmentResponse.setPaymentDate(loanInstallment.getPaymentDate());
        getInstallmentResponse.setPaidAmount(loanInstallment.getPaidAmount());
        getInstallmentResponse.setLoanId(loanInstallment.getLoanId());
        return getInstallmentResponse;
    }
}
