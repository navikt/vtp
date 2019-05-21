package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonstatusModell extends Periodisert {

    @JsonProperty("kode")
    private Personstatuser kode;

    PersonstatusModell() {
    }

    public PersonstatusModell(Personstatuser personstatus) {
        this(personstatus, null, null);
    }

    public PersonstatusModell(String personstatus) {
        this(personstatus, null, null);
    }

    public PersonstatusModell(String personstatus, LocalDate fom, LocalDate tom) {
        this(Personstatuser.valueOf(personstatus), fom, tom);
    }

    public PersonstatusModell(Personstatuser personstatus, LocalDate fom, LocalDate tom) {
        super(fom, tom);
        this.kode = Objects.requireNonNull(personstatus, "personstatus");
    }

    public String getStatus() {
        return kode.name();
    }

    public Personstatuser getPersonstatusType() {
        return kode;
    }

    /** NAV kodeverk: http://nav.no/kodeverk/Kodeverk/Personstatuser. */
    public enum Personstatuser {
        ABNR, ADNR, BOSA, DØD, FOSV, FØDR, UFUL, UREG, UTAN, UTPE, UTVA;
    }
}
