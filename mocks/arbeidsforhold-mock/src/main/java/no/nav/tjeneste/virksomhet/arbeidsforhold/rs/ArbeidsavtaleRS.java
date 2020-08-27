package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import java.math.BigDecimal;
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
    private BigDecimal stillingsprosent;
    @JsonProperty("antallTimerPrUke")
    private BigDecimal antallTimerPrUke;
    @JsonProperty("beregnetAntallTimerPrUke")
    private BigDecimal beregnetAntallTimerPrUke;
    @JsonProperty("sistLoennsendring")
    private LocalDate sistLoennsendring;
    @JsonProperty("gyldighetsperiode")
    private PeriodeRS gyldighetsperiode;
    @JsonProperty("yrke")
    private String yrke; // (kodeverk: Yrker)

    public BigDecimal getStillingsprosent() {
        return stillingsprosent;
    }

    public BigDecimal getAntallTimerPrUke() {
        return antallTimerPrUke;
    }

    public BigDecimal getBeregnetAntallTimerPrUke() {
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
        this.stillingsprosent = new BigDecimal(avtale.getStillingsprosent());
        this.antallTimerPrUke = new BigDecimal(avtale.getAvtaltArbeidstimerPerUke());
        this.beregnetAntallTimerPrUke = new BigDecimal(avtale.getBeregnetAntallTimerPerUke());
        this.sistLoennsendring = avtale.getSisteLønnnsendringsdato();
        this.yrke = avtale.getYrke() != null && avtale.getYrke().getYrke() != null ? avtale.getYrke().getYrke() : "8269102";
        this.gyldighetsperiode = new PeriodeRS(avtale.getFomGyldighetsperiode(), avtale.getTomGyldighetsperiode());
    }
}
