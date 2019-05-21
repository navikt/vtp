package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

/*
    Hentet fra https://modapp.adeo.no/kodeverksklient/viskodeverk/Arkivfiltyper/3?1
 */
public class Arkivfiltype {
    
    private static List<String> VALID_KODER; 
    static {
        List<String> koder = new ArrayList<>();
        koder.add("DOC");
        koder.add("TIFF");
        koder.add("XLSX");
        koder.add("XML");
        koder.add("AXML");
        koder.add("PDF");
        koder.add("PDFA");
        
        VALID_KODER = Collections.unmodifiableList(koder);
    }
    
    public static final Arkivfiltype DOC = new Arkivfiltype("DOC");
    public static final Arkivfiltype TIFF = new Arkivfiltype("TIFF");
    public static final Arkivfiltype XLSX = new Arkivfiltype("XLSX");
    public static final Arkivfiltype XML = new Arkivfiltype("XML");
    public static final Arkivfiltype AXML = new Arkivfiltype("AXML");
    public static final Arkivfiltype PDF = new Arkivfiltype("PDF");
    public static final Arkivfiltype PDFA = new Arkivfiltype("PDFA");

    private String kode;

    public Arkivfiltype(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODER.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i Joark arkivfiltype: " + kode);
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
        return Objects.equals(getKode(), ((Arkivfiltype) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }


}
