package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.time.LocalDate;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatsborgerskapModell extends Periodisert {
    
    private static final Pattern LANDKODE = Pattern.compile("^[A-Z]{3}$");
    
    @JsonProperty("landkode")
    private String landkode;

    StatsborgerskapModell() {
    }

    public StatsborgerskapModell(String landkode) {
        this(landkode, null, null);
    }

    public StatsborgerskapModell(String landkode, LocalDate fom, LocalDate tom) {
        super(fom, tom);
        if(!LANDKODE.matcher(landkode).matches()) {
            throw new IllegalArgumentException("Landkode er ikke gyldig 3-bokstav ISO landkode: "+ landkode);
        }
        this.landkode = landkode;
    }

    public String getLandkode() {
        return landkode;
    }
}
