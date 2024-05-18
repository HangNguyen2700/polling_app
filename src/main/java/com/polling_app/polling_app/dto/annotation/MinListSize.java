package com.polling_app.polling_app.dto.annotation;

import com.polling_app.polling_app.dto.validator.MinListSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinListSizeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinListSize {
    String message() default "The list must be a minimum size";

    long min() default -1L;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
