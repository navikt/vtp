package no.nav.oppgave.infrastruktur.validering;


import static no.nav.oppgave.infrastruktur.validering.CountFieldsMatching.count;

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
        return count(o, fields) <= 1L;
    }
}
