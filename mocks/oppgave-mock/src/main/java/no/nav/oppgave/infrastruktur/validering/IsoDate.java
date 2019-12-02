package no.nav.oppgave.infrastruktur.validering;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IsoDateValidator.class)
public @interface IsoDate {
    String message() default "{no.nav.oppgave.IsoDate.ugyldig}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
