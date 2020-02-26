package no.nav.tjenester.pensjon.tjenestepensjon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(value = "SimulerPensjonRequest")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimulerPensjonRequest {

    private String sprak;

    private String fnr; //todo might need to change this one!
    private String sivilstandkode;
    private List<Pensjonsbeholdningperiode> pensjonsbeholdningsperioder;
    private List<Simuleringsperiode> simuleringsperioder;
    private Integer simulertAFPOffentlig;
    private SimulertAFPPrivat simulertAFPPrivat;
    @JsonCreator
    public SimulerPensjonRequest(
            @JsonProperty(value = "fnr", required = true) String fnr,
            @JsonProperty(value = "sivilstandkode", defaultValue = "ugift", required = true) String sivilstandkode,
            @JsonProperty(value = "sprak", defaultValue = "norsk", required = true) String sprak,
            @JsonProperty(value = "simuleringsperioder" , required = true) List<Simuleringsperiode> simuleringsperioder,
            @JsonProperty(value = "simulertAFPOffentlig") Integer simulertAFPOffentlig,
            @JsonProperty(value = "simulertAFPPrivat") SimulertAFPPrivat simulertAFPPrivat,
            @JsonProperty(value = "pensjonsbeholdningsperioder") List<Pensjonsbeholdningperiode> pensjonsbeholdningsperioder,
            @JsonProperty(value = "inntekter", required = true) List<Inntekt> inntekter
    ) {
        this.fnr = fnr;
        this.sivilstandkode = sivilstandkode;
        this.sprak = sprak;
        this.simuleringsperioder = simuleringsperioder;
        this.simulertAFPOffentlig = simulertAFPOffentlig;
        this.simulertAFPPrivat = simulertAFPPrivat;
        this.pensjonsbeholdningsperioder = pensjonsbeholdningsperioder;
        this.inntekter = inntekter;
    }
    private List<Inntekt> inntekter;

    public String getFnr() {
        return fnr;
    }

    public String getSivilstandkode() {
        return sivilstandkode;
    }

    public String getSprak() {
        return sprak;
    }

    public List<Simuleringsperiode> getSimuleringsperioder() {
        return simuleringsperioder;
    }

    public Integer getSimulertAFPOffentlig() {
        return simulertAFPOffentlig;
    }

    public SimulertAFPPrivat getSimulertAFPPrivat() {
        return simulertAFPPrivat;
    }

    public List<Pensjonsbeholdningperiode> getPensjonsbeholdningsperioder() {
        return pensjonsbeholdningsperioder;
    }

    public List<Inntekt> getInntekter() {
        return inntekter;
    }
}
