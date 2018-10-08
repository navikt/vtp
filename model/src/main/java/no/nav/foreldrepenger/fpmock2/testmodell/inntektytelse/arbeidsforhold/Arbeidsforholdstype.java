package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Arbeidsforholdstype {

    private static List<String> VALID_KODER;

    static {
        List<String> koder = new ArrayList<>();
        koder.add("ordinaertArbeidsforhold");
        //TODO: Fyll ut denne

        VALID_KODER = Collections.unmodifiableList(koder);
    }

    public static final Arbeidsforholdstype ORDINÆRT_ARBEIDSFORHOLD = new Arbeidsforholdstype("ordinaertArbeidsforhold");

    private String kode;

    @JsonCreator
    public Arbeidsforholdstype(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODER.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i AAReg arbeidsforholdstype: " + kode);
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
        return Objects.equals(getKode(), ((Arbeidsforholdstype) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }




}
