package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("rammevedtak")
public class InfotrygdRammevedtaksGrunnlag extends InfotrygdBeregningsgrunnlag{

    private String rammevedtakTekst;

    private String annenpart;

    public String getRammevedtakTekst() {
        return String.format(rammevedtakTekst, annenpart);
    }

    public void setRammevedtakTekst(String rammevedtakTekst) {
        this.rammevedtakTekst = rammevedtakTekst;
    }
}
