package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Avlønningstype {

    private static List<String> VALID_KODER;

    static {
        List<String> verdi = new ArrayList<>();
        verdi.add("fastlønn");
        // TODO: fyll ut denne

        VALID_KODER = Collections.unmodifiableList(verdi);
    }

    public static final Avlønningstype FASTLØNN = new Avlønningstype("fastlønn");

    private String kode;
    @JsonCreator
    public Avlønningstype(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode!= null && !VALID_KODER.contains(kode)) {
            throw new IllegalArgumentException("Kode er ikke implementert i AAReg(?) avlønningstype: " + kode);
        }
    }

    @JsonValue
    public String getKode(){ return kode; }

    public void setKode(String kode) { this.kode = kode;}

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
