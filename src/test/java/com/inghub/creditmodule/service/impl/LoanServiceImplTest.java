package com.inghub.creditmodule.service.impl;

import com.inghub.creditmodule.controller.request.CreateLoanRequest;
import com.inghub.creditmodule.controller.response.CreateLoanResponse;
import com.inghub.creditmodule.controller.response.GetLoanResponse;
import com.inghub.creditmodule.repository.CustomerRepository;
import com.inghub.creditmodule.repository.LoanInstallmentRepository;
import com.inghub.creditmodule.repository.LoanRepository;
import com.inghub.creditmodule.repository.entity.Customer;
import com.inghub.creditmodule.repository.entity.Loan;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {
    @InjectMocks
    private LoanServiceImpl loanService;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;

    @Test
    void createLoanTest() {
        CreateLoanRequest request = Instancio.create(CreateLoanRequest.class);
        request.setAmount(BigDecimal.valueOf(20));
        request.setInterestRate(BigDecimal.valueOf(0.2));
        Customer customer = Instancio.create(Customer.class);
        customer.setUsedCreditLimit(BigDecimal.TEN);
        customer.setCreditLimit(BigDecimal.valueOf(100));
        when(this.customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        Loan loan = Instancio.create(Loan.class);
        when(this.loanRepository.save(any())).thenReturn(loan);
        CreateLoanResponse response = this.loanService.createLoan(request);
        assertEquals(1, response.getTotalAmount().compareTo(BigDecimal.ZERO));
        verify(this.loanInstallmentRepository, times(1)).saveAll(any());
    }

    @Test
    void getLoanTest() {
        Loan loan = Instancio.create(Loan.class);
        when(this.loanRepository.findByCustomerId(anyLong())).thenReturn(List.of(loan));
        List<GetLoanResponse> response = this.loanService.getLoan(anyLong());
        assertEquals(loan.getCustomerId(), response.get(0).getCustomerId());
    }
}
