package no.nav.foreldrepenger.fpmock2.testmodell.medlemskap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

public class DekningType {
    private static List<String> VALID_KODER;
    static {
        List<String> trygdedekning = new ArrayList<>();
        trygdedekning.add("IT_DUMMY_EOS");
        trygdedekning.add("IHT_Avtale");
        trygdedekning.add("FTL_2-7_3_ledd_a");
        trygdedekning.add("FTL_2-7_3_ledd_b");
        trygdedekning.add("Full");
        trygdedekning.add("FTL_2-9_1_ledd_a");
        trygdedekning.add("FTL_2-9_a");
        trygdedekning.add("FTL_2-9_1_ledd_c");
        trygdedekning.add("FTL_2-9_1_ledd_b");
        trygdedekning.add("FTL_2-7_bok_a");
        trygdedekning.add("FTL_2-7_bok_b");
        trygdedekning.add("FTL_2-9_b");
        trygdedekning.add("FTL_2-9_c");
        trygdedekning.add("PENDEL");
        trygdedekning.add("FTL_2-9_2_ld_jfr_1a");
        trygdedekning.add("Unntatt");
        trygdedekning.add("FTL_2-9_2_ld_jfr_1c");
        trygdedekning.add("Opphor");
        trygdedekning.add("IKKEPENDEL");
        trygdedekning.add("IT_DUMMY");
        trygdedekning.add("FTL_2-9_2_ledd");
        trygdedekning.add("IHT_Avtale_Forord");
        trygdedekning.add("FTL_2-6");

        VALID_KODER = Collections.unmodifiableList(trygdedekning);
    }
    public static final DekningType IHT_AVTALE = new DekningType("IHT_Avtale");
    

    private String kode;

    @JsonCreator
    public DekningType(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODER.contains(kode)) {
            throw new IllegalArgumentException("Kode er ikke gyldig Medl2 trygdedekning type: " + kode);
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
