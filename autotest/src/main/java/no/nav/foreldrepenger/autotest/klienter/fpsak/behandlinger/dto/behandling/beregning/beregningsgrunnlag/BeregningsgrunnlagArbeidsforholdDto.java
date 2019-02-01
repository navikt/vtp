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
}
