package no.nav.oppgave.infrastruktur.validering;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

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
