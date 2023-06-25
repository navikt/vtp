package no.nav.oppgave.infrastruktur.validering;

import org.apache.commons.lang3.math.NumberUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@SuppressWarnings("WeakerAccess")
public class AtleastOneOfValidator implements ConstraintValidator<AtleastOneOf, Object> {
    private String[] fields;

    @Override
    public void initialize(AtleastOneOf atleastOneOf) {
        this.fields = atleastOneOf.fields();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return CountFieldsMatching.count(o, fields) >= NumberUtils.LONG_ONE;
    }
}
