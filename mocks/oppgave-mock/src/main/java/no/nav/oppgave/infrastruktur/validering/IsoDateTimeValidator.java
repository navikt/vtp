package no.nav.oppgave.infrastruktur.validering;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@SuppressWarnings("WeakerAccess")
public class IsoDateTimeValidator implements ConstraintValidator<IsoDateTime, String> {

    @Override
    public void initialize(IsoDateTime isoDateTime) {
        //JSR303
    }

    @Override
    public boolean isValid(String dateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (dateTime != null && !dateTime.isEmpty()) {
            try {
                LocalDate.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

}
