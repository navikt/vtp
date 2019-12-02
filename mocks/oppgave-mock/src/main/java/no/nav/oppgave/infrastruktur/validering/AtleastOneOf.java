package no.nav.oppgave.infrastruktur.validering;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AtleastOneOfValidator.class)
public @interface AtleastOneOf {
    String message() default "{no.nav.oppgave.AtleastOneOf}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fields();

}
