package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Arbeidsavtale {



    @JsonProperty("yrke")
    private Yrke yrke;

    @JsonProperty("avlønningstype")
    private Avlønningstype avlønningstype;

    @JsonProperty("avtaltArbeidstimerPerUke")
    private Integer avtaltArbeidstimerPerUke;

    @JsonProperty("stillingsprosent")
    private Integer stillingsprosent;

    @JsonProperty("beregnetAntallTimerPerUke")
    private Integer beregnetAntallTimerPerUke;

    @JsonProperty("sisteLønnsendringsdato")
    public LocalDate sisteLønnnsendringsdato;

    @JsonProperty("fomGyldighetsperiode")
    public LocalDate fomGyldighetsperiode;



    public Yrke getYrke() {
        return yrke;
    }

    public void setYrke(Yrke yrke) {
        this.yrke = yrke;
    }

    public Avlønningstype getAvlønningstype() {
        return avlønningstype;
    }

    public void setAvlønningstype(Avlønningstype avlønningstype) {
        this.avlønningstype = avlønningstype;
    }

    public Integer getAvtaltArbeidstimerPerUke() {
        return avtaltArbeidstimerPerUke;
    }

    public void setAvtaltArbeidstimerPerUke(Integer avtaltArbeidstimerPerUke) {
        this.avtaltArbeidstimerPerUke = avtaltArbeidstimerPerUke;
    }

    public Integer getStillingsprosent() {
        return stillingsprosent;
    }

    public void setStillingsprosent(Integer stillingsprosent) {
        this.stillingsprosent = stillingsprosent;
    }

    public Integer getBeregnetAntallTimerPerUke() {
        return beregnetAntallTimerPerUke;
    }

    public void setBeregnetAntallTimerPerUke(Integer beregnetAntallTimerPerUke) {
        this.beregnetAntallTimerPerUke = beregnetAntallTimerPerUke;
    }

    public LocalDate getSisteLønnnsendringsdato() {
        return sisteLønnnsendringsdato;
    }

    public void setSisteLønnnsendringsdato(LocalDate sisteLønnnsendringsdato) {
        this.sisteLønnnsendringsdato = sisteLønnnsendringsdato;
    }

    public LocalDate getFomGyldighetsperiode() {
        return fomGyldighetsperiode;
    }

    public void setFomGyldighetsperiode(LocalDate fomGyldighetsperiode) {
        this.fomGyldighetsperiode = fomGyldighetsperiode;
    }
}
