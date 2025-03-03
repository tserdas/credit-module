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
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstallmentServiceImplTest {
    @InjectMocks
    private InstallmentServiceImpl installmentService;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;

    @Test
    void payLoanTest_LoanPaid() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setCustomerId(1L);
        loan.setPaid(true);
        loan.setLoanAmount(BigDecimal.valueOf(138));
        loan.setNumberOfInstallment(6);
        loan.setCreateDate(OffsetDateTime.now().minusDays(1));
        when(this.loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));

        PayLoanRequest request = new PayLoanRequest();
        request.setLoanId(1L);
        request.setAmount(BigDecimal.valueOf(150));

        assertThrows(RuntimeException.class, () -> this.installmentService.payLoan(request));
    }

    @Test
    void payLoanTest_isBefore_3_terms() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setCustomerId(1L);
        loan.setPaid(false);
        loan.setLoanAmount(BigDecimal.valueOf(138));
        loan.setNumberOfInstallment(6);
        loan.setCreateDate(OffsetDateTime.now().minusDays(1));
        when(this.loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));

        LocalDate dueDate = LocalDate.now().withDayOfMonth(1).plusMonths(1);
        List<LoanInstallment> loanInstallmentList = new ArrayList<>();
        for (int i = 0; i < loan.getNumberOfInstallment(); i++) {
            LoanInstallment loanInstallment = new LoanInstallment();
            loanInstallment.setLoanId(loan.getId());
            loanInstallment.setPaid(false);
            loanInstallment.setAmount(BigDecimal.valueOf(23));
            loanInstallment.setDueDate(dueDate.plusMonths(i));
            loanInstallment.setPaidAmount(BigDecimal.ZERO);
            loanInstallmentList.add(loanInstallment);
        }
        when(this.loanInstallmentRepository.findByLoanIdAndIsPaidFalseOrderByDueDateAsc(anyLong())).thenReturn(loanInstallmentList);

        when(this.loanInstallmentRepository.findByLoanIdAndIsPaidFalse(anyLong())).thenReturn(List.of());

        Customer customer = Instancio.create(Customer.class);
        when(this.customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        PayLoanRequest request = new PayLoanRequest();
        request.setLoanId(1L);
        request.setAmount(BigDecimal.valueOf(150));

        PayLoanResponse response = this.installmentService.payLoan(request);

        assertEquals(3, response.getPaidInstallments());
        verify(this.loanInstallmentRepository, times(3)).save(any());
    }

    @Test
    void getInstallmentTest() {
        LoanInstallment loanInstallment = Instancio.create(LoanInstallment.class);
        when(this.loanInstallmentRepository.findByLoanId(anyLong())).thenReturn(List.of(loanInstallment));
        List<GetInstallmentResponse> response = this.installmentService.getInstallment(anyLong());
        assertEquals(loanInstallment.getLoanId(), response.get(0).getLoanId());
    }
}
