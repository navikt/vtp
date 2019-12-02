package no.nav.oppgave.infrastruktur.validering;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@SuppressWarnings("WeakerAccess")
public class IsoDateValidator implements ConstraintValidator<IsoDate, String> {

    @Override
    public void initialize(IsoDate isoDate) {
        //JSR303
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        if (isNotEmpty(date)) {
            try {
                LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

}
