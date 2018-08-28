package no.nav.foreldrepenger.fpmock2.testmodell.medlemskap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

public class PeriodeStatus {

    private static List<String> VALID_KODER;
    static {
        List<String> koder = new ArrayList<>();
        koder.add("INNV"); // innvilget
        koder.add("AVSL"); // avslått
        koder.add("AVST"); // avvist
        koder.add("GYLD"); // gyldig
        koder.add("UAVK"); // uavklart

        VALID_KODER = Collections.unmodifiableList(koder);
    }
    
    public static final PeriodeStatus INNV = new PeriodeStatus("INNV");
    public static final PeriodeStatus AVSL = new PeriodeStatus("AVSL");
    public static final PeriodeStatus AVST = new PeriodeStatus("AVST");
    public static final PeriodeStatus GYLD= new PeriodeStatus("GYLD");
    public static final PeriodeStatus UAVK= new PeriodeStatus("UAVK");
    
    private String kode;

    @JsonCreator
    public PeriodeStatus(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODER.contains(kode)) {
            throw new IllegalArgumentException("Kode er ikke gyldig Medl2 periodestatus type: " + kode);
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
