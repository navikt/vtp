package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Oppføring(String tekniskNavn, @JsonProperty("beløp") String verdi) {

    public String tilSigrunMockResponseFormat() {
        return "{\n" + "\"tekniskNavn\"" + ": \"" + tekniskNavn + "\"," + "\n" + "\"verdi\"" + ": \"" + verdi + "\"\n}";
    }
}
