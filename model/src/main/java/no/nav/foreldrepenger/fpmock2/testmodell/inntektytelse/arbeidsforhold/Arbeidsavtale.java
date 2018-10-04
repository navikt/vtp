package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold;

import com.fasterxml.jackson.annotation.JsonProperty;

class Arbeidsavtale {


    private Yrke yrke;

    private Avlønningstype avlønningstype;

    private Integer avtaltArbeidstimerPerUke;

    @JsonProperty("stillingsprosent")
    private Integer stillingsprosent;

    private Integer beregnetAntallTimerPerUke;






}
