package com.inghub.creditmodule.controller.impl;

import com.inghub.creditmodule.controller.LoanController;
import com.inghub.creditmodule.controller.request.CreateLoanRequest;
import com.inghub.creditmodule.controller.response.CreateLoanResponse;
import com.inghub.creditmodule.controller.response.GetLoanResponse;
import com.inghub.creditmodule.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoanControllerImpl implements LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanControllerImpl(LoanService loanService) {
        this.loanService = loanService;
    }

    @Override
    public ResponseEntity<CreateLoanResponse> createLoan(CreateLoanRequest request) {
        return ResponseEntity.ok(this.loanService.createLoan(request));
    }

    @Override
    public ResponseEntity<List<GetLoanResponse>> getLoan(Long customerId) {
        return ResponseEntity.ok(this.loanService.getLoan(customerId));
    }
}
