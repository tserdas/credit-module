package com.inghub.creditmodule.validator.annotation;

import com.inghub.creditmodule.validator.LoanInstallmentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LoanInstallmentValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoanInstallment {
    String message() default "Number of installments must be 6, 9, 12, or 24";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
