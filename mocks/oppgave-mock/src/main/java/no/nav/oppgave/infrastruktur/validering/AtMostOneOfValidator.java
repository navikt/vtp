package no.nav.oppgave.infrastruktur.validering;


import static no.nav.oppgave.infrastruktur.validering.CountFieldsMatching.count;
import static org.apache.commons.lang3.math.NumberUtils.LONG_ONE;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@SuppressWarnings("WeakerAccess")
public class AtMostOneOfValidator implements ConstraintValidator<AtMostOneOf, Object> {
    private String[] fields;

    @Override
    public void initialize(AtMostOneOf atMostOneOf) {
        this.fields = atMostOneOf.fields();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return count(o, fields) <= LONG_ONE;
    }
}
