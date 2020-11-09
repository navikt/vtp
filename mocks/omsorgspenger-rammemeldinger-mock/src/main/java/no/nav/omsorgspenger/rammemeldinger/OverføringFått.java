package no.nav.omsorgspenger.rammemeldinger;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDate;

public class OverføringFått {
    @JsonProperty("gjennomført")
    private LocalDate gjennomført;

    @JsonProperty("gyldigFraOgMed")
    private LocalDate gyldigFraOgMed;

    @JsonProperty("gyldigTilOgMed")
    private LocalDate gyldigTilOgMed;

    @JsonProperty("lengde")
    private Duration lengde;

    @JsonProperty("fra")
    private Person fra;

    public LocalDate getGjennomført() {
        return gjennomført;
    }

    public void setGjennomført(LocalDate gjennomført) {
        this.gjennomført = gjennomført;
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

    public Duration getLengde() {
        return lengde;
    }

    public void setLengde(Duration lengde) {
        this.lengde = lengde;
    }

    public Person getFra() {
        return fra;
    }

    public void setFra(Person fra) {
        this.fra = fra;
    }
}
