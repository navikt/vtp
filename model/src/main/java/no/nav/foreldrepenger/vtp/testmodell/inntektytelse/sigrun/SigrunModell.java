package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SigrunModell(List<Inntektsår> inntektsår) {

    public static final String SIGRUN_OPPFØRING_TEKNISK_NAVN = "personinntektNaering";
    public static final String SIGRUN_OPPFØRING_SKATTEOPPGJØR = "skatteoppgjoersdato";

    public SigrunModell() {
        this(null);
    }

    @JsonCreator
    public SigrunModell(@JsonProperty("inntektsår") List<Inntektsår> inntektsår) {
        this.inntektsår = Optional.ofNullable(inntektsår).orElse(List.of());
    }
}
