package no.nav.foreldrepenger.fpmock2.testmodell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

public class Feilkode {
    private static List<String> VALID_KODER;
    static {
        List<String> koder = new ArrayList<>();
        koder.add(FeilKodeKonstanter.UGYLDIG_INPUT);
        koder.add(FeilKodeKonstanter.PERSON_IKKE_FUNNET);
        koder.add(FeilKodeKonstanter.SIKKERHET_BEGRENSNING);

        VALID_KODER = Collections.unmodifiableList(koder);
    }

    private String kode;
    
    @JsonCreator
    public Feilkode(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODER.contains(kode)) {
            throw new IllegalArgumentException("Kode er ikke gyldig feilkode: " + kode);
        }
    }

    @JsonValue
    public String getKode() {
        return kode;
    }
    
    public String getTermnavn() {
        return getKode();
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    @Override
    public String toString() {
        return getKode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        return Objects.equals(getKode(), ((Landkode) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }

}
