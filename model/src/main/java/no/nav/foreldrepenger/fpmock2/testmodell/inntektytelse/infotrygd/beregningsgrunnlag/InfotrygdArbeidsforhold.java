package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.fpmock2.testmodell.util.VariabelContainer;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.ScenarioVirksomheter;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.VirksomhetModell;

public class InfotrygdArbeidsforhold {

    @JsonProperty("orgnr")
    private String lokalOrgnr;

    @JsonProperty("beløp")
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

    public String getLokalOrgnr() {
        return lokalOrgnr;
    }

    public void setLokalOrgnr(String lokalOrgnr) {
        this.lokalOrgnr = lokalOrgnr;
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
