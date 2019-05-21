package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public class DokumentTilknyttetJournalpost {

    private static List<String> VALID_KODER;

    static {
        List<String> koder = new ArrayList<>();
        koder.add("HOVEDDOKUMENT");
        koder.add("VEDLEGG");

        VALID_KODER = Collections.unmodifiableList(koder);
    }

    public static DokumentTilknyttetJournalpost HOVEDDOKUMENT = new DokumentTilknyttetJournalpost("HOVEDDOKUMENT");
    public static DokumentTilknyttetJournalpost VEDLEGG = new DokumentTilknyttetJournalpost("VEDLEGG");

    private String kode;

    public DokumentTilknyttetJournalpost(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODER.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i Joark dokumentTilknyttetJournalpost: " + kode);
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
        return Objects.equals(getKode(), ((DokumentTilknyttetJournalpost) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }

}
