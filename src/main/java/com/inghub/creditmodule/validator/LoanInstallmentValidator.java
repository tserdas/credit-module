package com.inghub.creditmodule.validator;

import com.inghub.creditmodule.validator.annotation.LoanInstallment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class LoanInstallmentValidator implements ConstraintValidator<LoanInstallment, Integer> {
    private static final List<Integer> VALID_INSTALLMENTS = List.of(6, 9, 12, 24);

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && VALID_INSTALLMENTS.contains(value);
    }
}
