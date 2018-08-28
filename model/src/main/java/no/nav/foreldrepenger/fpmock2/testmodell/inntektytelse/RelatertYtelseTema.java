package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

public class RelatertYtelseTema {
    private static Map<String, String> VALID_KODER;
    static {
        Map<String, String> koder = new LinkedHashMap<>();
        koder.put("AAP", "Arbeidsavklaringspenger");
        koder.put("DAG", "Dagpenger");
        koder.put("FA", "Foreldrepenger");
        koder.put("EF", "Enslig forsørger");
        koder.put("SP", "Sykepenger");
        koder.put("BS", "Pårørende sykdom");
        VALID_KODER = Collections.unmodifiableMap(koder);
    }

    public static final RelatertYtelseTema AAP = new RelatertYtelseTema("AAP");
    public static final RelatertYtelseTema FA = new RelatertYtelseTema("FA");
    public static final RelatertYtelseTema SP = new RelatertYtelseTema("SP");
    public static final RelatertYtelseTema DAG = new RelatertYtelseTema("DAG");
    
    private String kode;

    @JsonCreator
    public RelatertYtelseTema(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODER.containsKey(kode)) {
            throw new IllegalArgumentException("Kode er ikke gyldig Tema: " + kode);
        }
    }

    @JsonValue
    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    @Override
    public String toString() {
        return getKode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        return Objects.equals(getKode(), ((Landkode) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }

}
