package com.ibrahimtugrul.cartservice.application.validator;



import com.ibrahimtugrul.cartservice.application.validator.aspect.ValidateEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

/**
 *
 */
public class EnumValidator implements ConstraintValidator<ValidateEnum, String> {

    private List<String> valueList;

    @Override
    public void initialize(ValidateEnum constraintAnnotation) {
        valueList = of(constraintAnnotation.enumClazz().getEnumConstants()).map(e->e.toString()).collect(toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return valueList.contains(value.toUpperCase());
    }

}