package pl.bilskik.citifier.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class NumberComparasionValidator implements ConstraintValidator<NumberComparasion, Object> {

    private String firstField;
    private String secondField;

    @Override
    public void initialize(NumberComparasion constraintAnnotation) {
        this.firstField = constraintAnnotation.firstField();
        this.secondField = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Integer firstValue = (Integer) new BeanWrapperImpl(object).getPropertyValue(firstField);
            Integer secondValue = (Integer) new BeanWrapperImpl(object).getPropertyValue(secondField);

            return firstValue != null && secondValue != null && firstValue < secondValue;
        } catch (Exception e) {
            return false;
        }
    }
}
