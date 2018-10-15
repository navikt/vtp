package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder;

/*
    Hentet fra: https://modapp.adeo.no/kodeverksklient/viskodeverk/Dokumentkategorier/1?7
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Dokumentkategori {

    private static List<String> VALID_KODER;
    static {
        List<String> koder = new ArrayList<>();
        koder.add("B");
        koder.add("ELEKTRONISK_DIALOG");
        koder.add("ES");
        koder.add("FORVALTNINGSNOTAT");
        koder.add("IB");
        koder.add("IS");
        koder.add("KA");
        koder.add("KD");
        koder.add("KS");
        koder.add("PUBL_BLANKETT_EOS");
        koder.add("SED");
        koder.add("SOK");
        koder.add("TS");
        koder.add("VB");

        VALID_KODER = Collections.unmodifiableList(koder);
    }

    public static Dokumentkategori BREV = new Dokumentkategori("B");
    public static Dokumentkategori ELEKTRONISK_DIALOG = new Dokumentkategori("ELEKTRONISK_DIALOG");
    public static Dokumentkategori ELEKTRONISK_SKJEMA = new Dokumentkategori("ES");
    public static Dokumentkategori FORVALTNINGSNOTAT = new Dokumentkategori("FORVALTNINGSNOTAT");
    public static Dokumentkategori INFORMASJONSBREV = new Dokumentkategori("IB");
    public static Dokumentkategori IKKE_TOLKBART_SKJEMA = new Dokumentkategori("IS");
    public static Dokumentkategori KLAGE_ANKE = new Dokumentkategori("KA");
    public static Dokumentkategori KONVERTERT_FRA_ELEKTRONSIK_ARKIV = new Dokumentkategori("KD");
    public static Dokumentkategori KONVERTERT_DATA_FRA_SYSTEM = new Dokumentkategori("KS");
    public static Dokumentkategori PUBLIKUMSBLANKETT_EOS = new Dokumentkategori("PUBL_BLANKETT_EOS");
    public static Dokumentkategori STRUKTURERT_ELEKTRONISK_DOKUMENT_EU_EOS = new Dokumentkategori("SED");
    public static Dokumentkategori SOKNAD = new Dokumentkategori("SOK");
    public static Dokumentkategori TOLKBART_SKJEMA = new Dokumentkategori("TS");
    public static Dokumentkategori VEDTAKSBREV = new Dokumentkategori("VB");

    private String kode;

    @JsonCreator
    public Dokumentkategori(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODER.contains(kode)) {
            throw new IllegalArgumentException("Kode er ikke implementert i Dokumentmodell dokumentkategori: " + kode);
        }
    }

    @JsonValue
    public String getKode() { return kode;}

    public void setKode(String kode) { this.kode = kode;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        return Objects.equals(getKode(), ((Dokumentkategori) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }

}
