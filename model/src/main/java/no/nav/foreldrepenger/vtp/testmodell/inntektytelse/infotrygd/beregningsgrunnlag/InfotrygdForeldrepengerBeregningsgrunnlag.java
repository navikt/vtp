package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.time.LocalDate;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("foreldrepenger")
public class InfotrygdForeldrepengerBeregningsgrunnlag extends InfotrygdBeregningsgrunnlagPeriodeYtelse {

    private LocalDate opprinneligStartdato;
    private Integer dekningsgrad;
    private Integer gradering;
    private LocalDate fødselsdatoBarn;

    public LocalDate getOpprinneligStartdato() {
        return opprinneligStartdato;
    }

    public void setOpprinneligStartdato(LocalDate opprinneligStartdato) {
        this.opprinneligStartdato = opprinneligStartdato;
    }

    public Integer getDekningsgrad() {
        return dekningsgrad;
    }

    public void setDekningsgrad(Integer dekningsgrad) {
        this.dekningsgrad = dekningsgrad;
    }

    public Integer getGradering() {
        return gradering;
    }

    public void setGradering(Integer gradering) {
        this.gradering = gradering;
    }

    public LocalDate getFødselsdatoBarn() {
        return fødselsdatoBarn;
    }

    public void setFødselsdatoBarn(LocalDate fødselsdatoBarn) {
        this.fødselsdatoBarn = fødselsdatoBarn;
    }

    public void setArbeidsforhold(InfotrygdArbeidsforhold... arbeidsforhold) {
        setArbeidsforhold(Arrays.asList(arbeidsforhold));
    }

    public void setVedtak(InfotrygdVedtak vedtak) {
        setVedtak(Arrays.asList(vedtak));
    }
}
