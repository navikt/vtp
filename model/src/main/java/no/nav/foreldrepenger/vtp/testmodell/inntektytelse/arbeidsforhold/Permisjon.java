package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Permisjon {


    @JsonProperty("stillingsprosent")
    public Integer stillingsprosent;

    @JsonProperty("fomGyldighetsperiode")
    public LocalDate fomGyldighetsperiode;

    @JsonProperty("tomGyldighetsperiode")
    public LocalDate tomGyldighetsperiode;

    @JsonProperty("permisjonType")
    public Permisjonstype permisjonstype;


    public Integer getStillingsprosent() {
        return stillingsprosent;
    }

    public void setStillingsprosent(Integer stillingsprosent) {
        this.stillingsprosent = stillingsprosent;
    }

    public LocalDate getFomGyldighetsperiode() {
        return fomGyldighetsperiode;
    }

    public void setFomGyldighetsperiode(LocalDate fomGyldighetsperiode) {
        this.fomGyldighetsperiode = fomGyldighetsperiode;
    }

    public LocalDate getTomGyldighetsperiode() {
        return tomGyldighetsperiode;
    }

    public void setTomGyldighetsperiode(LocalDate tomGyldighetsperiode) {
        this.tomGyldighetsperiode = tomGyldighetsperiode;
    }

    public Permisjonstype getPermisjonstype() {
        return permisjonstype;
    }

    public void setPermisjonstype(Permisjonstype permisjonstype) {
        this.permisjonstype = permisjonstype;
    }
}
