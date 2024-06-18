package pl.bilskik.citifier.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDateTime;

public class LocalDateTimeComparasionValidator implements ConstraintValidator<LocalDateTimeComparasion, Object> {

    private String firstField;
    private String secondField;

    @Override
    public void initialize(LocalDateTimeComparasion constraintAnnotation) {
        this.firstField = constraintAnnotation.firstField();
        this.secondField = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDateTime localDateTimeStart = (LocalDateTime) new BeanWrapperImpl(obj).getPropertyValue(firstField);
            LocalDateTime localDateTimeFinish = (LocalDateTime) new BeanWrapperImpl(obj).getPropertyValue(secondField);

            if(localDateTimeStart == null || localDateTimeFinish == null) {
                return false;
            }

            return localDateTimeFinish.isAfter(localDateTimeStart);
        } catch(Exception e) {
            return false;
        }
    }
}
