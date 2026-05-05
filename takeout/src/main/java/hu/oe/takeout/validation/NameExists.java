package hu.oe.takeout.validation;

import hu.oe.takeout.rdbms.CategoryRepository;
import hu.oe.takeout.rdbms.TakeoutRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NameExistsValidator.class)

public @interface NameExists {
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

@Slf4j
@RequiredArgsConstructor
class NameExistsValidator implements ConstraintValidator<NameExists, String> {

    private final TakeoutRepository takeoutRepository;
    private final CategoryRepository categoryRepository;

    String message;

    @Override
    public void initialize(NameExists constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return true; // let @NotBlank handle emptiness if needed
        }

        boolean existsInTakeout = takeoutRepository.existsByName(value);
        boolean existsInCategory = categoryRepository.existsByName(value);

        return !existsInTakeout && !existsInCategory;
    }
}
