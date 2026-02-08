package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ArbeidskategoriKode {
    K00("00"),
    K01("01"),
    K02("02"),
    K03("03"),
    K04("04"),
    K05("05"),
    K06("06"),
    K07("07"),
    K08("08"),
    K09("09"),
    K10("10"),
    K11("11"),
    K12("12"),
    K13("13"),
    K14("14"),
    K15("15"),
    K16("17"),
    K17("17"),
    K18("18"),
    K19("19"),
    K20("20"),
    K21("21"),
    K22("22"),
    K23("23"),
    K24("24"),
    K25("25"),
    K26("26"),
    K27("27"),
    K30("30"),
    @JsonEnumDefaultValue
    K99("99");

    @JsonValue
    private final String kode;

    public String getKode() {
        return kode;
    }

    ArbeidskategoriKode(String kode) {
        this.kode = kode;
    }
}
