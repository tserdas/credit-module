package com.inghub.creditmodule.service;

import com.inghub.creditmodule.controller.request.PayLoanRequest;
import com.inghub.creditmodule.controller.response.GetInstallmentResponse;
import com.inghub.creditmodule.controller.response.PayLoanResponse;

import java.util.List;

public interface InstallmentService {
    PayLoanResponse payLoan(PayLoanRequest request);

    List<GetInstallmentResponse> getInstallment(Long loanId);
}
