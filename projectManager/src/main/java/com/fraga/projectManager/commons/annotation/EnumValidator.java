package com.fraga.projectManager.commons.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidatorImpl.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
    public @interface EnumValidator {
    Class<? extends Enum<?>> enumClass();
    String message() default "Valor inválido para enum";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
