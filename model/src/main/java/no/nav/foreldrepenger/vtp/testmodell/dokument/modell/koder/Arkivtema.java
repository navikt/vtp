package no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder;

/*
Hentet fra: https://modapp.adeo.no/kodeverksklient/viskodeverk/Arkivtemaer/7?6
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;


public class Arkivtema {

    private static List<String> VALID_KODER;

    static {
        List<String> koder = new ArrayList<>();
        koder.add("FOR");
        koder.add("SYM");
        koder.add("SYK");
        koder.add("ENF");
        koder.add("OMS");
        koder.add("FRI");
        koder.add("UNG");

        VALID_KODER = Collections.unmodifiableList(koder);
    }

    public static Arkivtema FOR = new Arkivtema("FOR");
    public static Arkivtema SYM = new Arkivtema("SYM");
    public static Arkivtema SYK = new Arkivtema("SYK");
    public static Arkivtema ENF = new Arkivtema("ENF");
    public static Arkivtema OMS = new Arkivtema("OMS");
    public static Arkivtema FRI = new Arkivtema("FRI");
    public static Arkivtema UNG = new Arkivtema("UNG");

    private String kode;

    public Arkivtema(String kode){
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
