package pl.bilskik.citifier.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class NumberComparasionValidator implements ConstraintValidator<NumberComparasion, Object> {

    private String firstField;
    private String secondField;
    private String message;
    private String errorPath;

    @Override
    public void initialize(NumberComparasion constraintAnnotation) {
        this.firstField = constraintAnnotation.firstField();
        this.secondField = constraintAnnotation.secondField();
        this.message = constraintAnnotation.message();
        this.errorPath = constraintAnnotation.errorPath();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            if(errorPath == null || errorPath.isEmpty()) {
                errorPath = firstField;
            }

            Integer firstValue = (Integer) new BeanWrapperImpl(object).getPropertyValue(firstField);
            Integer secondValue = (Integer) new BeanWrapperImpl(object).getPropertyValue(secondField);

            boolean isValid = firstValue != null && secondValue != null && firstValue < secondValue;

            if(!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(errorPath)
                        .addConstraintViolation();
            }

            return isValid;
        } catch (Exception e) {
            return false;
        }
    }
}
