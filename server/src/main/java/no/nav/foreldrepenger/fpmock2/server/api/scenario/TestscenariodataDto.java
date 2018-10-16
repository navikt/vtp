package no.nav.foreldrepenger.fpmock2.server.api.scenario;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class TestscenariodataDto {

    @JsonProperty("inntektskomponent")
    private InntektskomponentModell inntektskomponentModell;

    @JsonProperty("aareg")
    private ArbeidsforholdModell arbeidsforholdModell;

    public TestscenariodataDto() {
    }

    public TestscenariodataDto(InntektskomponentModell inntektskomponentModell, ArbeidsforholdModell arbeidsforholdModell) {
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
