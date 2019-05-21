package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BeregningsgrunnlagArbeidsforholdDto {
    protected String arbeidsgiverNavn;
    protected String arbeidsgiverId;
    protected String startdato;
    protected String opphoersdato;
    protected String arbeidsforholdId;
    protected Kode arbeidsforholdType;
    protected AktørId aktørId;

    public String getArbeidsgiverNavn() {
        return arbeidsgiverNavn;
    }

    public String getArbeidsgiverId() {
        return arbeidsgiverId;
    }

    public String getStartdato() {
        return startdato;
    }

    public String getOpphoersdato() {
        return opphoersdato;
    }

    public String getArbeidsforholdId() {
        return arbeidsforholdId;
    }

    public Kode getArbeidsforholdType() {
        return arbeidsforholdType;
    }

    public AktørId getAktørId() {
        return aktørId;
    }
}
