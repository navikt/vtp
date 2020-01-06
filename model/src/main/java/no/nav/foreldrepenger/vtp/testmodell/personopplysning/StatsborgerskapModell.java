package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class StatsborgerskapModell extends Periodisert {

    @JsonProperty("land")
    private Landkode land;

    StatsborgerskapModell() {
    }

    public StatsborgerskapModell(Landkode landkode) {
        this(landkode, null, null);
    }

    public StatsborgerskapModell(Landkode landkode, LocalDate fom, LocalDate tom) {
        super(fom, tom);
        this.land = landkode;
    }

    public String getLandkode() {
        return land==null?null:land.getKode();
    }

    public void setLand(Landkode land) {
        this.land = land;
    }
    public Landkode getLand() {
        return land;
    }
}
