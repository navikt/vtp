package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeografiskTilknytningModell extends Periodisert {
    
    public static GeografiskTilknytningModell defaultValue() {
        GeografiskTilknytningModell geo = new GeografiskTilknytningModell();
        geo.setType(GeografiskTilknytningType.Land);
        geo.setKode("NOR");
        return geo;
    }

    @JsonProperty("type")
    private GeografiskTilknytningType type;
    @JsonProperty("kode")
    private String kode;

    public GeografiskTilknytningModell() {
    }

    public GeografiskTilknytningModell(LocalDate fom, LocalDate tom) {
        super(fom, tom);
    }

    public enum GeografiskTilknytningType {
        // må matche case fra TPS / Kodeverkforvaltning
        Bydel, Kommune, Land
    }

    public GeografiskTilknytningType getGeografiskTilknytningType() {
        return type;
    }

    public String getGeografiskTilknytning() {
        return type.name();
    }

    public void setType(GeografiskTilknytningType type) {
        this.type = type;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }
}
