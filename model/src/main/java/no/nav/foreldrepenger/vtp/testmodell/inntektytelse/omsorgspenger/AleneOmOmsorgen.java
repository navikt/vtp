package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AleneOmOmsorgen {
    @JsonProperty("gjennomført")
    private LocalDate gjennomført;

    @JsonProperty("registrert")
    private LocalDate registrert;

    @JsonProperty("gyldigFraOgMed")
    private LocalDate gyldigFraOgMed;

    @JsonProperty("gyldigTilOgMed")
    private LocalDate gyldigTilOgMed;

    @JsonProperty("barn")
    private Person barn;

    @JsonProperty("kilder")
    private List<Kilde> kilder;

    public LocalDate getGjennomført() {
        return gjennomført;
    }

    public void setGjennomført(LocalDate gjennomført) {
        this.gjennomført = gjennomført;
    }

    public LocalDate getRegistrert() {
        return registrert;
    }

    public void setRegistrert(LocalDate registrert) {
        this.registrert = registrert;
    }

    public LocalDate getGyldigFraOgMed() {
        return gyldigFraOgMed;
    }

    public void setGyldigFraOgMed(LocalDate gyldigFraOgMed) {
        this.gyldigFraOgMed = gyldigFraOgMed;
    }

    public LocalDate getGyldigTilOgMed() {
        return gyldigTilOgMed;
    }

    public void setGyldigTilOgMed(LocalDate gyldigTilOgMed) {
        this.gyldigTilOgMed = gyldigTilOgMed;
    }

    public Person getBarn() {
        return barn;
    }

    public void setBarn(Person barn) {
        this.barn = barn;
    }

    public List<Kilde> getKilder() {
        if(kilder == null) {
            kilder = new ArrayList<>();
        }
        return kilder;
    }

    public void setKilder(List<Kilde> kilder) {
        this.kilder = kilder;
    }
}
