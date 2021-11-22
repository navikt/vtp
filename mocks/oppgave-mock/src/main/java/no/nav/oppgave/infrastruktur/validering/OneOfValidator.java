package no.nav.oppgave.infrastruktur.validering;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Arrays;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@SuppressWarnings("WeakerAccess")
public class OneOfValidator implements ConstraintValidator<OneOf, String> {
    private String[] legalValues;

    @Override
    public void initialize(OneOf oneOf) {
        this.legalValues = oneOf.legalValues();
    }

    @Override
    public boolean isValid(String oneOf, ConstraintValidatorContext constraintValidatorContext) {
        if (isBlank(oneOf)) {
            return true;
        }

        return Arrays.stream(legalValues).anyMatch(legalEntry -> legalEntry.equals(oneOf));
    }
}
