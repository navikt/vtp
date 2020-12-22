package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.util.VariabelContainer;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.ScenarioVirksomheter;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.VirksomhetModell;

public class InfotrygdArbeidsforhold {

    @JsonProperty("orgnr")
    private String lokalOrgnr;

    private BigDecimal beløp;

    @JsonProperty("inntektsperiode")
    private InfotrygdInntektsperiodeType inntektsPeriodeType;

    @JacksonInject
    private ScenarioVirksomheter virksomheter;

    @JacksonInject
    private VariabelContainer vars;

    public InfotrygdArbeidsforhold() {
    }

    public InfotrygdArbeidsforhold(String lokalOrgnr) {
        this.lokalOrgnr = lokalOrgnr;
    }

    public String getOrgnr() {
        return getVirksomhet().getOrgnr();
    }

    public VirksomhetModell getVirksomhet() {
        VirksomhetModell virksomhet = virksomheter.getVirksomhet(lokalOrgnr);
        vars.computeIfAbsent(lokalOrgnr, (n) -> virksomhet.getOrgnr());
        return virksomhet;
    }

    public BigDecimal getBeløp() {
        return beløp;
    }

    public void setBeløp(BigDecimal beløp) {
        this.beløp = beløp;
    }

    public InfotrygdInntektsperiodeType getInntektsPeriodeType() {
        return inntektsPeriodeType;
    }

    public void setInntektsPeriodeType(InfotrygdInntektsperiodeType inntektsPeriodeType) {
        this.inntektsPeriodeType = inntektsPeriodeType;
    }

}
