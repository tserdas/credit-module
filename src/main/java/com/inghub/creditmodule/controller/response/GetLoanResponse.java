package com.inghub.creditmodule.controller.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class GetLoanResponse {
    private Long loanId;
    private Long customerId;
    private BigDecimal loanAmount;
    private int numberOfInstallment;
    private OffsetDateTime createDate;
    private boolean isPaid;

    public GetLoanResponse(Long loanId, Long customerId, BigDecimal loanAmount, int numberOfInstallment, OffsetDateTime createDate, boolean isPaid) {
        this.loanId = loanId;
        this.customerId = customerId;
        this.loanAmount = loanAmount;
        this.numberOfInstallment = numberOfInstallment;
        this.createDate = createDate;
        this.isPaid = isPaid;
    }

    public GetLoanResponse() {
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getNumberOfInstallment() {
        return numberOfInstallment;
    }

    public void setNumberOfInstallment(int numberOfInstallment) {
        this.numberOfInstallment = numberOfInstallment;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
