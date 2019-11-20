package no.nav.oppgave.infrastruktur.validering;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IsoDateTimeValidator.class)
public @interface IsoDateTime {
    String message() default "{no.nav.oppgave.IsoDateTime.ugyldig}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
