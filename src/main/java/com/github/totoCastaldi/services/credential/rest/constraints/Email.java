package com.github.totoCastaldi.services.credential.rest.constraints;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Created by toto on 16/11/15.
 */
@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface Email {

    String message() default "{com.github.totoCastaldi.services.constraints.email}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
