package no.nav.tjenester.pensjon.tjenestepensjon.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(value = "SimulerPensjonRequest")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimulerPensjonRequest {

    private List<Inntekt> inntekter;
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

    public List<Inntekt> getInntekter() {
        return inntekter;
    }

    public void setInntekter(List<Inntekt> inntekter) {
        this.inntekter = inntekter;
    }

    public String getSprak() {
        return sprak;
    }

    public void setSprak(String sprak) {
        this.sprak = sprak;
    }

    public String getFnr() {
        return fnr;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

    public String getSivilstandkode() {
        return sivilstandkode;
    }

    public void setSivilstandkode(String sivilstandkode) {
        this.sivilstandkode = sivilstandkode;
    }

    public List<Pensjonsbeholdningperiode> getPensjonsbeholdningsperioder() {
        return pensjonsbeholdningsperioder;
    }

    public void setPensjonsbeholdningsperioder(List<Pensjonsbeholdningperiode> pensjonsbeholdningsperioder) {
        this.pensjonsbeholdningsperioder = pensjonsbeholdningsperioder;
    }

    public List<Simuleringsperiode> getSimuleringsperioder() {
        return simuleringsperioder;
    }

    public void setSimuleringsperioder(List<Simuleringsperiode> simuleringsperioder) {
        this.simuleringsperioder = simuleringsperioder;
    }

    public Integer getSimulertAFPOffentlig() {
        return simulertAFPOffentlig;
    }

    public void setSimulertAFPOffentlig(Integer simulertAFPOffentlig) {
        this.simulertAFPOffentlig = simulertAFPOffentlig;
    }

    public SimulertAFPPrivat getSimulertAFPPrivat() {
        return simulertAFPPrivat;
    }

    public void setSimulertAFPPrivat(SimulertAFPPrivat simulertAFPPrivat) {
        this.simulertAFPPrivat = simulertAFPPrivat;
    }
}
