package ru.simplgroupp.validators;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

/**
 * Age validator
 */
public class AgeValidator implements ConstraintValidator<Age, Date> {

    private int min;

    private int max;

    @Override
    public void initialize(Age age) {
        min = age.min();
        max = age.max();
    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) {
            return true;
        }
        int age = Years.yearsBetween(new LocalDate(date), new LocalDate()).getYears();
        return age >= min && age <= max;
    }
}
