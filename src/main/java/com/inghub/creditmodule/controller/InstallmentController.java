package com.inghub.creditmodule.controller;

import com.inghub.creditmodule.controller.request.PayLoanRequest;
import com.inghub.creditmodule.controller.response.GetInstallmentResponse;
import com.inghub.creditmodule.controller.response.PayLoanResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/installment")
public interface InstallmentController {

    @PostMapping("/pay")
    ResponseEntity<PayLoanResponse> payLoan(@Valid @RequestBody PayLoanRequest request);

    @GetMapping("{loanId}")
    ResponseEntity<List<GetInstallmentResponse>> getInstallment(@PathVariable Long loanId);

}
