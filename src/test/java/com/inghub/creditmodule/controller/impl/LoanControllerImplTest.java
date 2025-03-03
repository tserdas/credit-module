package com.inghub.creditmodule.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inghub.creditmodule.config.SecurityConfig;
import com.inghub.creditmodule.controller.request.CreateLoanRequest;
import com.inghub.creditmodule.controller.response.CreateLoanResponse;
import com.inghub.creditmodule.controller.response.GetLoanResponse;
import com.inghub.creditmodule.service.impl.LoanServiceImpl;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanControllerImpl.class)
@Import(SecurityConfig.class)
class LoanControllerImplTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private LoanServiceImpl loanService;

    @Test
    void createLoanSuccess() throws Exception {
        String username = "admin";
        String password = "admin123";
        CreateLoanRequest request = Instancio.create(CreateLoanRequest.class);
        request.setCustomerId(1L);
        request.setInterestRate(BigDecimal.valueOf(.2));
        request.setNumberOfInstallments(6);
        CreateLoanResponse response = Instancio.create(CreateLoanResponse.class);
        response.setLoanId(1L);
        when(this.loanService.createLoan(any())).thenReturn(response);
        mockMvc.perform(post("/api/loan/create").with(httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getLoanSuccess() throws Exception {
        GetLoanResponse getLoanResponse = Instancio.create(GetLoanResponse.class);
        getLoanResponse.setCustomerId(1L);
        when(this.loanService.getLoan(any())).thenReturn(List.of(getLoanResponse));
        mockMvc.perform(get("/api/loan/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1));
    }

}
