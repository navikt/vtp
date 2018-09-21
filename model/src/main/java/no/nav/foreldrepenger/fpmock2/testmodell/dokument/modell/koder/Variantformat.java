package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder;

/*
    Hentet fra: https://modapp.adeo.no/kodeverksklient/viskodeverk/Variantformater/2?13
 */


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public class Variantformat {

    private static List<String> VALID_KODER;

    static {
        List<String> koder = new ArrayList<>();
        koder.add("ARKIV");
        koder.add("FULLVERSJON");
        koder.add("ORIGINAL");

        VALID_KODER = Collections.unmodifiableList(koder);
    }

    private String kode;

    public static Variantformat ARKIV = new Variantformat("ARKIV");
    public static Variantformat FULLVERSJON = new Variantformat("FULLVERSJON");
    public static Variantformat ORIGINAL = new Variantformat("ORIGINAL");

    public Variantformat(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODER.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i Joark arkivtema: " + kode);
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
        return Objects.equals(getKode(), ((Arkivtema) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }


}
