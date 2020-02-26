package no.nav.tjenester.pensjon.tjenestepensjon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "SimulerPensjonRequest")
@JsonDeserialize(as = SimulerPensjonRequest.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimulerPensjonRequest {

    @JsonProperty(value = "fnr", required = true)
    private String fnr; //todo might need to change this one!

    @JsonProperty(required = true)
    private String sivilstandkode;

    @JsonProperty(required = true)
    private String sprak = "norsk";

    @JsonProperty(required = true)
    private List<Simuleringsperiode> simuleringsperioder;

    private Integer simulertAFPOffentlig;

    private SimulertAFPPrivat simulertAFPPrivat;

    private List<Pensjonsbeholdningperiode> pensjonsbeholdningsperioder = new ArrayList<>();

    @JsonProperty(required = true)
    private List<Inntekt> inntekter;

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

    public String getSprak() {
        return sprak;
    }

    public void setSprak(String sprak) {
        this.sprak = sprak;
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

    public List<Pensjonsbeholdningperiode> getPensjonsbeholdningsperioder() {
        return pensjonsbeholdningsperioder;
    }

    public void setPensjonsbeholdningsperioder(List<Pensjonsbeholdningperiode> pensjonsbeholdningsperioder) {
        this.pensjonsbeholdningsperioder = pensjonsbeholdningsperioder;
    }

    public List<Inntekt> getInntekter() {
        return inntekter;
    }

    public void setInntekter(List<Inntekt> inntekter) {
        this.inntekter = inntekter;
    }
}
