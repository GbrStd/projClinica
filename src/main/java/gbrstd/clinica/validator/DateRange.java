package gbrstd.clinica.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface DateRange {

    String message() default "{DateRange.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String min() default "";

    String max() default "";

    boolean minNow() default false;

    boolean maxNow() default false;

}
