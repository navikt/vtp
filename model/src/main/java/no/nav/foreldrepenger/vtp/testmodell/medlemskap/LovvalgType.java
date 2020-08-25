package no.nav.foreldrepenger.vtp.testmodell.medlemskap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Landkode;

public class LovvalgType {

    private static List<String> VALID_KODER;
    static {
        List<String> koder = new ArrayList<>();
        koder.add("FORL"); // foreløpig
        koder.add("UAVK"); // under avklaring
        koder.add("ENDL"); // endelig

        VALID_KODER = Collections.unmodifiableList(koder);
    }

    public static final LovvalgType ENDL = new LovvalgType("ENDL");
    public static final LovvalgType UAVK = new LovvalgType("UAVK");
    public static final LovvalgType FORL = new LovvalgType("FORL");

    private String kode;

    public LovvalgType(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODER.contains(kode)) {
            throw new IllegalArgumentException("Kode er ikke gyldig Medl2 medlemskaptype type: " + kode);
        }
    }

    @JsonValue
    public String getKode() {
        return kode;
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
