package com.inghub.creditmodule.service;

import com.inghub.creditmodule.controller.request.CreateLoanRequest;
import com.inghub.creditmodule.controller.response.CreateLoanResponse;
import com.inghub.creditmodule.controller.response.GetLoanResponse;

import java.util.List;

public interface LoanService {
    CreateLoanResponse createLoan(CreateLoanRequest request);

    List<GetLoanResponse> getLoan(Long customerId);

    Long getCustomerIdByLoan(Long loanId);
}
