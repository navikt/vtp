package no.nav.oppgave.infrastruktur.validering;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = OneOrMoreOfValidator.class)
public @interface OneOrMoreOf {
    String message() default "{no.nav.oppgave.OneOrMoreOf}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] legalValues();

    String name();
}
