package com.inghub.creditmodule.controller.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PayLoanRequest {
    @NotNull
    private Long loanId;
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
