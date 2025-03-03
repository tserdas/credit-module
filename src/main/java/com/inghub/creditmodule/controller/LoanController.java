package com.inghub.creditmodule.controller;

import com.inghub.creditmodule.controller.request.CreateLoanRequest;
import com.inghub.creditmodule.controller.response.CreateLoanResponse;
import com.inghub.creditmodule.controller.response.GetLoanResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/loan")
public interface LoanController {
    @PostMapping("/create")
    ResponseEntity<CreateLoanResponse> createLoan(@Valid @RequestBody CreateLoanRequest request);

    @GetMapping("/{customerId}")
    ResponseEntity<List<GetLoanResponse>> getLoan(@PathVariable Long customerId);
}
