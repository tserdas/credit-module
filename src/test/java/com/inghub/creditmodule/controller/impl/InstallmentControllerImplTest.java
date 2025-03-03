package com.inghub.creditmodule.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inghub.creditmodule.config.SecurityConfig;
import com.inghub.creditmodule.controller.request.PayLoanRequest;
import com.inghub.creditmodule.controller.response.GetInstallmentResponse;
import com.inghub.creditmodule.controller.response.PayLoanResponse;
import com.inghub.creditmodule.service.impl.InstallmentServiceImpl;
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

@WebMvcTest(InstallmentControllerImpl.class)
@Import(SecurityConfig.class)
class InstallmentControllerImplTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private InstallmentServiceImpl installmentService;

    @Test
    void payLoanSuccess() throws Exception {
        String username = "admin";
        String password = "admin123";
        PayLoanRequest request = new PayLoanRequest();
        request.setAmount(BigDecimal.valueOf(1000));
        request.setLoanId(1L);
        PayLoanResponse response = Instancio.create(PayLoanResponse.class);
        response.setTotalPaid(BigDecimal.valueOf(1000));
        when(this.installmentService.payLoan(any())).thenReturn(response);
        mockMvc.perform(post("/api/installment/pay").with(httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPaid").value(BigDecimal.valueOf(1000)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getInstallmentSuccess() throws Exception {
        GetInstallmentResponse response = Instancio.create(GetInstallmentResponse.class);
        response.setLoanId(1L);
        when(this.installmentService.getInstallment(any())).thenReturn(List.of(response));
        mockMvc.perform(get("/api/installment/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].loanId").value(1));
    }
}
