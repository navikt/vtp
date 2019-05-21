package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
/*
[
{"navn":"kontantytelse","term":"Kontantytelse"},
{"navn":"naturalytelse","term":"Naturalytelse"},
{"navn":"utgiftsgodtgjoerelse","term":"Utgiftsgodtgjørelse"}]
 */
public class InntektFordel {

    private static List<String> VALID_KODER;
    static {
        List<String> koder = new ArrayList<>();
        koder.add("kontantytelse");
        koder.add("utgiftsgodtgjørelse");
        koder.add("naturalytelse");

        VALID_KODER = Collections.unmodifiableList(koder);
    }

    public static InntektFordel KONTANTYTELSE = new InntektFordel("kontantytelse");
    public static InntektFordel UTGIFTSGODTGJØRELSE = new InntektFordel("utgiftsgodtgjørelse");
    public static InntektFordel NATURALYTELSE = new InntektFordel("naturalytelse");

    private String kode;

    @JsonCreator
    public InntektFordel(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODER.contains(kode)) {
            throw new IllegalArgumentException("Kode er ikke implementert i Inntektskomponent inntektfordel: " + kode);
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
        return Objects.equals(getKode(), ((InntektFordel) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }



}
