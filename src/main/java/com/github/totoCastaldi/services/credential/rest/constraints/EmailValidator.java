package com.github.totoCastaldi.services.credential.rest.constraints;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by toto on 16/11/15.
 */
public class EmailValidator implements ConstraintValidator<Email, String> {


    @Override
    public void initialize(Email email) {
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isNotBlank(string);
    }
}
