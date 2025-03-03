package com.inghub.creditmodule.controller.request;

import com.inghub.creditmodule.validator.annotation.LoanInstallment;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateLoanRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;
    private BigDecimal amount;
    @NotNull(message = "Interest rate cannot be null")
    @DecimalMin(value = "0.1", message = "Interest rate must be at least 0.1")
    @DecimalMax(value = "0.5", message = "Interest rate must be at most 0.5")
    private BigDecimal interestRate;
    @LoanInstallment
    private Integer numberOfInstallments;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(Integer numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }
}
