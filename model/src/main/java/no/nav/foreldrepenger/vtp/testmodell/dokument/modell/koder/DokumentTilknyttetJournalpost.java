package no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public class DokumentTilknyttetJournalpost {

    private static final List<String> VALID_KODER = List.of("HOVEDDOKUMENT", "VEDLEGG");

    public static final DokumentTilknyttetJournalpost HOVEDDOKUMENT = new DokumentTilknyttetJournalpost("HOVEDDOKUMENT");
    public static final DokumentTilknyttetJournalpost VEDLEGG = new DokumentTilknyttetJournalpost("VEDLEGG");

    @JsonValue
    private String kode;

    DokumentTilknyttetJournalpost(String kode){
        this.kode = kode;
        if(kode != null && !VALID_KODER.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i Joark dokumentTilknyttetJournalpost: " + kode);
        }
    }

    public String getKode() {return kode;}

    public void setKode(String kode){this.kode = kode;}


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof DokumentTilknyttetJournalpost dok && Objects.equals(getKode(), dok.getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }

}
