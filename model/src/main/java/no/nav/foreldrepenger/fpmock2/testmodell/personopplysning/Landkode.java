package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.util.Objects;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Landkode {
    private static final Pattern LANDKODE = Pattern.compile("^[A-Z]{3}$");
    
    public static final Landkode NOR = new Landkode("NOR");
    public static final Landkode USA = new Landkode("USA");
    public static final Landkode SWE = new Landkode("SWE");
    public static final Landkode DEU = new Landkode("DEU");
    
    private String iso3bokstavLandkode = "NOR"; // default til norge

    @JsonCreator
    public Landkode(String kode) {
        this.iso3bokstavLandkode=kode;
        if(!LANDKODE.matcher(kode).matches()) {
            throw new IllegalArgumentException("Landkode er ikke gyldig 3-bokstav ISO landkode: "+ kode);
        }
    }

    @JsonValue
    public String getKode() {
        return iso3bokstavLandkode;
    }

    public void setKode(String kode) {
        this.iso3bokstavLandkode = kode;
    }

    @Override
    public String toString() {
        return getKode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj==this) {
            return true;
        } else if (obj==null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        return Objects.equals(getKode(), ((Landkode)obj).getKode());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }
}
