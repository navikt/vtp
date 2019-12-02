package no.nav.oppgave.infrastruktur.validering;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrganisasjonsnummerValidator implements ConstraintValidator<Organisasjonsnummer, String> {

    static final int[] ORGNR_VEKTER = {3, 2, 7, 6, 5, 4, 3, 2};

    @Override
    public void initialize(Organisasjonsnummer orgnr) {
        //JSR303
    }

    @Override
    public boolean isValid(String orgnr, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(orgnr);
    }

    public static boolean isValid(String orgnr) {
        if (StringUtils.isEmpty(orgnr)) {
            return true;
        } else if (!orgnr.matches("\\A[89]\\d{8}\\Z")) {
            return false;
        } else {
            int sum = 0;
            for (int i = 0; i < 8; i++) {
                int verdi = Integer.parseInt(orgnr.substring(i, i + 1));
                sum += (ORGNR_VEKTER[i] * verdi);
            }

            int rest = sum % 11;
            int kontrollSiffer = (rest == 0) ? 0 : (11 - rest);
            return kontrollSiffer == Integer.parseInt(orgnr.substring(8, 9));
        }
    }

}
