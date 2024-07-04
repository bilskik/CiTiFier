package pl.bilskik.citifier.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Repeatable(LocalDateTimeComparasions.class)
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateTimeComparasionValidator.class)
public @interface LocalDateTimeComparasion {
    String message() default "Field values are not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String firstField();

    String secondField();
}
