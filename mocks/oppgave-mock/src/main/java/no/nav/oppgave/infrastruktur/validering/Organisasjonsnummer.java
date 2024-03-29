package no.nav.oppgave.infrastruktur.validering;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = OrganisasjonsnummerValidator.class)
public @interface Organisasjonsnummer {
    String message() default "{no.nav.oppgave.Organisasjonsnummer.ugyldig}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
