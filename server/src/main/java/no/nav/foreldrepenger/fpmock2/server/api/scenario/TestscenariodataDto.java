package no.nav.foreldrepenger.fpmock2.server.api.scenario;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class TestscenariodataDto {

    @JsonProperty("inntektskomponent")
    private InntektskomponentModell inntektskomponentModell;

    @JsonProperty("aareg")
    private ArbeidsforholdModell arbeidsforholdModell;

    TestscenariodataDto(InntektskomponentModell inntektskomponentModell, ArbeidsforholdModell arbeidsforholdModell) {
        this.inntektskomponentModell = inntektskomponentModell;
        this.arbeidsforholdModell = arbeidsforholdModell;

    }

    public InntektskomponentModell getInntektskomponentModell() {
        return inntektskomponentModell;
    }

    public ArbeidsforholdModell getArbeidsforholdModell() {
        return arbeidsforholdModell;
    }
}
