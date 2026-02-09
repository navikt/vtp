package no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public class BrukerType {

    private static List<String> VALID_KODE;

    static {
        List<String> koder = new ArrayList<>();
        koder.add("FNR");
        koder.add("ORGNR");
        koder.add("AKTOERID");

        VALID_KODE = Collections.unmodifiableList(koder);
    }

    @JsonValue
    private String kode;

    public static BrukerType FNR = new BrukerType("FNR");
    public static BrukerType ORGNR = new BrukerType("ORGNR");
    public static BrukerType AKTOERID = new BrukerType("AKTOERID");


    public BrukerType(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODE.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i journalpost brukertype: " + kode);
        }
    }

    public String getKode() {return kode;}

    public void setKode(String kode){this.kode = kode;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        return Objects.equals(getKode(), ((BrukerType) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }

}
