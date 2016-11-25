package ru.simplgroupp.validators;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Cross constraint validator
 */
public class CrossConstraintValidator implements ConstraintValidator<CrossConstraint, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrossConstraintValidator.class);

    private String field;

    private String crossField;

    private CrossConstraint.Type constraint;

    private CrossConstraint.Type condition;

    @Override
    public void initialize(CrossConstraint crossConstraint) {
        field = crossConstraint.field();
        crossField = crossConstraint.crossField();
        constraint = crossConstraint.constraint();
        condition = crossConstraint.condition();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Object value = BeanUtils.getProperty(object, field);
            Object crossValue = BeanUtils.getProperty(object, crossField);

            boolean satisfyCondition = false;
            if (condition != null) {
                switch (condition) {
                    case NOT_NULL:
                        satisfyCondition = crossValue != null;
                        break;
                    case NOT_BLANK:
                        satisfyCondition = crossValue != null && StringUtils.isNotBlank(crossValue.toString());
                        break;
                    case TRUE:
                        satisfyCondition = crossValue != null && (boolean)crossValue;
                        break;
                }
            }

            if (satisfyCondition) {
                switch (constraint) {
                    case NOT_NULL:
                        if(value == null) {
                            constraintValidatorContext.buildConstraintViolationWithTemplate("javax.validation" +
                                    ".constraints.NotNull.message").addNode(field);
                        }
                        return false;
                    case NOT_BLANK:
                        if(StringUtils.isBlank(value.toString())) {
                            constraintValidatorContext.buildConstraintViolationWithTemplate("org.hibernate.validator" +
                                    ".constraints.NotBlank.message").addNode(field);
                        }
                        return false;
                    case TRUE:
                        if(!(boolean)value) {
                            constraintValidatorContext.buildConstraintViolationWithTemplate("javax.validation" +
                                    ".constraints.AssertTrue.message").addNode(field);
                        }
                        return false;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Can not validate object", e);
        }
        return true;
    }
}
