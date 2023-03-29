package gbrstd.clinica.validator;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.metadata.ConstraintDescriptor;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Date;

public class DateRangeValidator implements ConstraintValidator<DateRange, Date> {

    private static final String PATTERN = "yyyy-MM-dd";

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {

        final ConstraintDescriptor<?> descriptor = ((ConstraintValidatorContextImpl) context).getConstraintDescriptor();

        String minDate = descriptor.getAttributes().get("min").toString();
        String maxDate = descriptor.getAttributes().get("max").toString();

        boolean minNow = Boolean.parseBoolean(descriptor.getAttributes().get("minNow").toString());
        boolean maxNow = Boolean.parseBoolean(descriptor.getAttributes().get("maxNow").toString());

        try {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN).withResolverStyle(ResolverStyle.SMART);

            LocalDate date = value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            LocalDate min = minDate.isBlank() ? LocalDate.MIN : LocalDate.parse(minDate, formatter);
            LocalDate max = maxDate.isBlank() ? LocalDate.MAX : LocalDate.parse(maxDate, formatter);

            if (minNow) min = LocalDate.now();
            if (maxNow) max = LocalDate.now();

            return (date.isBefore(max) || date.isEqual(max)) && (date.isAfter(min) || date.isEqual(min));
        } catch (Exception e) {
            return false;
        }
    }

}
