package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SivilstandModell extends Periodisert {

    @JsonProperty("kode")
    private Sivilstander kode;

    SivilstandModell() {
    }

    public SivilstandModell(Sivilstander sivilstand) {
        this(sivilstand, null, null);
     }
    
    public SivilstandModell(String sivilstand) {
       this(sivilstand, null, null);
    }

    public SivilstandModell(String sivilstand, LocalDate fom, LocalDate tom) {
        this(Sivilstander.valueOf(sivilstand), fom, tom);
    }

    public SivilstandModell(Sivilstander sivilstand, LocalDate fom, LocalDate tom) {
        super(fom, tom);
        this.kode = Objects.requireNonNull(sivilstand, "sivilstand");
    }

    public String getKode() {
        return kode.name();
    }
    
    public Sivilstander getSivilstandType() {
        return kode;
    }

    /** NAV kodeverk: http://nav.no/kodeverk/Kodeverk/Sivilstander. */
    public enum Sivilstander {
        ENKE, GIFT, GJPA, GLAD, REPA, SAMB, SEPA, SEPR, SKIL, SKPA, UGIF;
    }
}
