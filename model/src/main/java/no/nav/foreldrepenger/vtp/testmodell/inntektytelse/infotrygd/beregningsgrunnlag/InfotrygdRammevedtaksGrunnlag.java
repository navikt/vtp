package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;

@JsonTypeName("rammevedtak")
public class InfotrygdRammevedtaksGrunnlag extends InfotrygdBeregningsgrunnlag{

    @JsonProperty("rammevedtakTekst")
    private String rammevedtakTekst;
    
    @JsonProperty("annenpart")
    private String annenpart;

    public String getRammevedtakTekst() {
        return String.format(rammevedtakTekst, annenpart);
    }

    public void setRammevedtakTekst(String rammevedtakTekst) {
        this.rammevedtakTekst = rammevedtakTekst;
    }
}
