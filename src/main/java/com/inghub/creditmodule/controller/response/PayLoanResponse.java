package com.inghub.creditmodule.controller.response;

import java.math.BigDecimal;

public class PayLoanResponse {
    private Integer paidInstallments;
    private BigDecimal totalPaid;
    private boolean isPaidCompletely;

    public PayLoanResponse(Integer paidInstallments, BigDecimal totalPaid, boolean isPaidCompletely) {
        this.paidInstallments = paidInstallments;
        this.totalPaid = totalPaid;
        this.isPaidCompletely = isPaidCompletely;
    }

    public Integer getPaidInstallments() {
        return paidInstallments;
    }

    public void setPaidInstallments(Integer paidInstallments) {
        this.paidInstallments = paidInstallments;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public boolean isPaidCompletely() {
        return isPaidCompletely;
    }

    public void setPaidCompletely(boolean paidCompletely) {
        isPaidCompletely = paidCompletely;
    }
}
