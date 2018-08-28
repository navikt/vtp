package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

public class SakStatus {
    private static Map<String, String> VALID_KODER;
    static {
        Map<String, String> koder = new LinkedHashMap<>();
        koder.put("AKTIV", "Aktiv");
        koder.put("INAKT", "Inaktiv");
        koder.put("AVSLU", "Avsluttet");

        VALID_KODER = Collections.unmodifiableMap(koder);
    }

    public static final SakStatus AKTIV = new SakStatus("AKTIV");
    public static final SakStatus INAKT = new SakStatus("INAKT");
    public static final SakStatus AVSLU = new SakStatus("AVSLU");
    
    private String kode;
    
    @JsonCreator
    public SakStatus(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODER.containsKey(kode)) {
            throw new IllegalArgumentException("Kode er ikke gyldig ArenaModell ArenaMeldekort Sakstatus: " + kode);
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
