package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder;

/*
    Hentet fra: https://modapp.adeo.no/kodeverksklient/viskodeverk/Journalstatuser/1?11
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public class Journalstatus {

    private static List<String> VALID_KODE;

    static {
        List<String> koder = new ArrayList<>();
        koder.add("J");
        koder.add("MO");
        koder.add("M");
        koder.add("A");

        VALID_KODE = Collections.unmodifiableList(koder);

    }

    private String kode;

    public static Journalstatus JOURNALFØRT = new Journalstatus("J");
    public static Journalstatus MOTTATT = new Journalstatus("MO");
    public static Journalstatus MIDLERTIDIG_JOURNALFØRT = new Journalstatus("M");
    public static Journalstatus AVBRUTT = new Journalstatus("A");

    public Journalstatus(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODE.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i Joark journalstatus: " + kode);
        }
    }

    @JsonValue
    public String getKode() {return kode;}

    public void setKode(String kode){this.kode = kode;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        return Objects.equals(getKode(), ((Journalstatus) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }

}
