package no.nav.oppgave.infrastruktur.validering;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

import static java.util.Arrays.asList;

@SuppressWarnings("WeakerAccess")
public class OneOrMoreOfValidator implements ConstraintValidator<OneOrMoreOf, List<String>> {
    private String[] legalValues;

    @Override
    public void initialize(OneOrMoreOf oneOrMoreOf) {
        this.legalValues = oneOrMoreOf.legalValues();
    }

    @Override
    public boolean isValid(List<String> list, ConstraintValidatorContext constraintValidatorContext) {
        if (list == null || list.isEmpty()) {
            return true;
        } else {
            long count = list.stream().filter(entry -> asList(legalValues).contains(entry)).count();
            return count == list.size();
        }
    }
}
