package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

public record ArbeidsforholdModell(List<Arbeidsforhold> arbeidsforhold) {

    public ArbeidsforholdModell() {
        this(null);
    }

    @JsonCreator
    public ArbeidsforholdModell(List<Arbeidsforhold> arbeidsforhold) {
        this.arbeidsforhold = Optional.ofNullable(arbeidsforhold).orElse(List.of());
    }
}
