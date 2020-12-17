package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

public record SigrunModell(List<Inntektsår> inntektsår) {

    public SigrunModell() {
        this(null);
    }

    @JsonCreator
    public SigrunModell(List<Inntektsår> inntektsår) {
        this.inntektsår = Optional.ofNullable(inntektsår).orElse(List.of());
    }
}
