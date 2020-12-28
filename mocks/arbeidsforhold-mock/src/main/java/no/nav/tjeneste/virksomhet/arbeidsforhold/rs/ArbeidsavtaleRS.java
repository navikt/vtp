package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsavtale;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ArbeidsavtaleRS {

    @JsonProperty("stillingsprosent")
    private Double stillingsprosent;
    @JsonProperty("antallTimerPrUke")
    private Double antallTimerPrUke;
    @JsonProperty("beregnetAntallTimerPrUke")
    private Double beregnetAntallTimerPrUke;
    @JsonProperty("sistLoennsendring")
    private LocalDate sistLoennsendring;
    @JsonProperty("gyldighetsperiode")
    private PeriodeRS gyldighetsperiode;
    @JsonProperty("yrke")
    private String yrke; // (kodeverk: Yrker)

    public Double getStillingsprosent() {
        return stillingsprosent;
    }

    public Double getAntallTimerPrUke() {
        return antallTimerPrUke;
    }

    public Double getBeregnetAntallTimerPrUke() {
        return beregnetAntallTimerPrUke;
    }

    public LocalDate getSistLoennsendring() {
        return sistLoennsendring;
    }

    public PeriodeRS getGyldighetsperiode() {
        return gyldighetsperiode;
    }

    public String getYrke() {
        return yrke;
    }

    public ArbeidsavtaleRS(Arbeidsavtale avtale) {
        this.stillingsprosent = avtale.stillingsprosent() != null ? avtale.stillingsprosent().doubleValue() : null;
        this.antallTimerPrUke = avtale.avtaltArbeidstimerPerUke() != null ? avtale.avtaltArbeidstimerPerUke().doubleValue() : null;
        this.beregnetAntallTimerPrUke = avtale.beregnetAntallTimerPerUke() != null ? avtale.beregnetAntallTimerPerUke().doubleValue() : null;
        this.sistLoennsendring = avtale.sisteLønnnsendringsdato();
        this.yrke = avtale.yrke() != null && avtale.yrke().yrke() != null ? avtale.yrke().yrke() : "8269102";
        this.gyldighetsperiode = new PeriodeRS(avtale.fomGyldighetsperiode(), avtale.tomGyldighetsperiode());
    }
}
