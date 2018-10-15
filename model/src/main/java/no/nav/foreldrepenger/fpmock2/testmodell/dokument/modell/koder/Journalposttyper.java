package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder;

/*
    Hentet fra: https://modapp.adeo.no/kodeverksklient/viskodeverk/Journalposttyper/1?5
*/

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Journalposttyper {
    private static List<String> VALID_KODE;

    static {
        List<String> koder = new ArrayList<>();
        koder.add("I");
        koder.add("N");
        koder.add("U");

        VALID_KODE = Collections.unmodifiableList(koder);
    }

    private String kode;

    public static Journalposttyper INNGAAENDE_DOKUMENT = new Journalposttyper("I");
    public static Journalposttyper NOTAT = new Journalposttyper("N");
    public static Journalposttyper UTGAAENDE_DOKUMENT = new Journalposttyper("U");

    public Journalposttyper(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODE.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i Joark journalposttyper: " + kode);
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
        return Objects.equals(getKode(), ((Journalposttyper) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }
}
