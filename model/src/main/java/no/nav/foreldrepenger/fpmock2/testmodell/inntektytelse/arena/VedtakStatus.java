package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

public class VedtakStatus {
    private static Map<String, String> VALID_KODER;
    static {
        Map<String, String> koder = new LinkedHashMap<>();
        koder.put("IVERK", "Iverksatt");
        koder.put("OPPRE", "Opprettet");
        koder.put("INNST", "Innstilt");
        koder.put("REGIS", "Registrert");
        koder.put("MOTAT", "Mottatt");
        koder.put("GODKJ", "Godkjent");
        koder.put("AVSLU", "Avsluttet");

        VALID_KODER = Collections.unmodifiableMap(koder);
    }

    public static final VedtakStatus IVERK = new VedtakStatus("IVERK");
    public static final VedtakStatus OPPRE = new VedtakStatus("OPPRE");
    public static final VedtakStatus INNST = new VedtakStatus("INNST");
    public static final VedtakStatus GODKJ = new VedtakStatus("GODKJ");
    public static final VedtakStatus REGIS = new VedtakStatus("REGIS");
    public static final VedtakStatus MOTAT = new VedtakStatus("MOTAT");
    public static final VedtakStatus AVSLU = new VedtakStatus("AVSLU");
    private String kode;

    @JsonCreator
    public VedtakStatus(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODER.containsKey(kode)) {
            throw new IllegalArgumentException("Kode er ikke gyldig ArenaMeldekort Vedtakstatus: " + kode);
        }
    }

    @JsonValue
    public String getKode() {
        return kode;
    }

    public String getTermnavn() {
        return VALID_KODER.get(getKode());
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
