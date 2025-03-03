package com.inghub.creditmodule.controller.impl;

import com.inghub.creditmodule.controller.InstallmentController;
import com.inghub.creditmodule.controller.request.PayLoanRequest;
import com.inghub.creditmodule.controller.response.GetInstallmentResponse;
import com.inghub.creditmodule.controller.response.PayLoanResponse;
import com.inghub.creditmodule.service.InstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InstallmentControllerImpl implements InstallmentController {

    private final InstallmentService installmentService;

    @Autowired
    public InstallmentControllerImpl(InstallmentService installmentService) {
        this.installmentService = installmentService;
    }

    @Override
    public ResponseEntity<PayLoanResponse> payLoan(PayLoanRequest request) {
        return ResponseEntity.ok(this.installmentService.payLoan(request));
    }

    @Override
    public ResponseEntity<List<GetInstallmentResponse>> getInstallment(Long loanId) {
        return ResponseEntity.ok(this.installmentService.getInstallment(loanId));
    }
}
