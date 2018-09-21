package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder;

/*
    Hentet fra: https://modapp.adeo.no/kodeverksklient/viskodeverk/Dokumentstatuser/1?9
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public class Dokumentstatus {

    private static List<String> VALID_KODER;

    static {
        List<String> koder = new ArrayList<>();
        koder.add("AVBRUTT");
        koder.add("FERDIGSTILT");
        koder.add("UNDER_REDIGERING");

        VALID_KODER = Collections.unmodifiableList(koder);
    }

    public static Dokumentstatus AVBRUTT = new Dokumentstatus("AVBRUTT");
    public static Dokumentstatus FERDIGSTILT = new Dokumentstatus("FERDIGSTILT");
    public static Dokumentstatus UNDER_REDIGERING = new Dokumentstatus("UNDER_REDIGERING");

    private String kode;

    public Dokumentstatus(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODER.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i Joark dokumentstatus: " + kode);
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
        return Objects.equals(getKode(), ((Dokumentstatus) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }


}
