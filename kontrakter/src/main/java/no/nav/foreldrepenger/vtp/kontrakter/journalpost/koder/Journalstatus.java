package no.nav.foreldrepenger.vtp.kontrakter.journalpost.koder;

/*
    Hentet fra: https://modapp.adeo.no/kodeverksklient/viskodeverk/Journalstatuser/1?11
 */

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Journalstatus {
    JOURNALFØRT("J"),
    MOTTATT("MO"),
    MIDLERTIDIG_JOURNALFØRT("M"),
    AVBRUTT("A");

    @JsonValue
    private String kode;

    Journalstatus(String kode) {
        this.kode = kode;
    }

    @JsonCreator
    public static Journalstatus fraKode(String kode) {
        return Arrays.stream(Journalstatus.values())
                .filter(value -> value.getKode().equalsIgnoreCase(kode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ikke støttet Journalstatus " + kode));
    }

    public String getKode() {
        return kode;
    }
}
