package no.nav.foreldrepenger.vtp.testmodell.medlemskap;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DekningType {
    IT_DUMMY_EOS("IT_DUMMY_EOS"),
    IHT_AVTALE("IHT_Avtale"),
    FTL_2_7_3_LEDD_A("FTL_2-7_3_ledd_a"),
    FTL_2_7_3_LEDD_B("FTL_2-7_3_ledd_b"),
    FULL("Full"),
    FTL_2_9_1_LEDD_A("FTL_2-9_1_ledd_a"),
    FTL_2_9_A("FTL_2-9_a"),
    FTL_2_9_1_LEDD_C("FTL_2-9_1_ledd_c"),
    FTL_2_9_1_LEDD_B("FTL_2-9_1_ledd_b"),
    FTL_2_7_BOK_A("FTL_2-7_bok_a"),
    FTL_2_7_BOK_B("FTL_2-7_bok_b"),
    FTL_2_9_B("FTL_2-9_b"),
    FTL_2_9_C("FTL_2-9_c"),
    PENDEL("PENDEL"),
    FTL_2_9_2_LD_JFR_1A("FTL_2-9_2_ld_jfr_1a"),
    UNNTATT("Unntatt"),
    FTL_2_9_2_LD_JFR_1C("FTL_2-9_2_ld_jfr_1c"),
    OPPHOR("Opphor"),
    IKKEPENDEL("IKKEPENDEL"),
    IT_DUMMY("IT_DUMMY"),
    FTL_2_9_2_LEDD("FTL_2-9_2_ledd"),
    IHT_AVTALE_FOROR("IHT_Avtale_Forord");

    @JsonValue
    private final String kode;

    DekningType(String kode) {
        this.kode = kode;
    }

    public String kode() {
        return kode;
    }
}
