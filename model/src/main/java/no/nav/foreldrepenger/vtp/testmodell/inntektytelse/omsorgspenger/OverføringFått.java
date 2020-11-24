package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @JsonProperty("kilder")
    private List<Kilde> kilder;

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
