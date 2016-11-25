package ru.simplgroupp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Cross field constraint
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CrossConstraintValidator.class)
@Documented
public @interface CrossConstraint {

    String message() default "{ru.simplgroupp.validators.CrossConstraint}";

    String field();

    String crossField();

    Type constraint();

    Type condition();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        CrossConstraint[] value();
    }

    enum Type {
        NOT_NULL,
        NOT_BLANK,
        TRUE
    }
}
