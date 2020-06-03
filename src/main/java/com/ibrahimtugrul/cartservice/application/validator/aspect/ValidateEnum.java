package com.ibrahimtugrul.cartservice.application.validator.aspect;


import com.ibrahimtugrul.cartservice.application.validator.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;


/**
 *
 *
 */
@Documented
@Constraint(validatedBy = EnumValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "Value cannot be null")
@ReportAsSingleViolation
public @interface ValidateEnum {

    Class<? extends Enum<?>> enumClazz();

    String message() default "Invalid Enum Type";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
