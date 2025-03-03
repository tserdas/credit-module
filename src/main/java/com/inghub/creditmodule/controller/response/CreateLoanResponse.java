package com.inghub.creditmodule.controller.response;

import java.math.BigDecimal;

public class CreateLoanResponse {
    private Long loanId;
    private BigDecimal totalAmount;
    private Integer numberOfInstallments;

    public CreateLoanResponse(Long loanId, BigDecimal totalAmount, Integer numberOfInstallments) {
        this.loanId = loanId;
        this.totalAmount = totalAmount;
        this.numberOfInstallments = numberOfInstallments;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(Integer numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }
}
