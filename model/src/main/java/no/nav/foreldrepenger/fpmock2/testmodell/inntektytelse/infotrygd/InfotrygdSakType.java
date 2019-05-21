package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

public class InfotrygdSakType {
    private static Map<String, String> VALID_KODER;
    static {
        Map<String, String> koder = new LinkedHashMap<>();
        koder.put("S", "Søknad");
        koder.put("R", "Revurdering");
        koder.put("T", "Tilbakebetalingssak");
        koder.put("A", "Anke");
        koder.put("K", "Klage");
        koder.put("I", "Informasjonssak");
        koder.put("J", "Journalsak");
        koder.put("DF", "Dispensasjon foreldelse");
        koder.put("DI", "dokumentinnsyn");
        koder.put("EG", "etterlyse girokort");
        koder.put("FS", "forespørsel");
        koder.put("JP", "Journalsak fra privatperson");
        koder.put("JT", "Journalsak fra trygdekontor");
        koder.put("JU", "Journalsak fra utenl trm");
        koder.put("KE", "Klage ettergivelse");
        koder.put("KS", "Kontrollsak");
        koder.put("KT", "Klage tilbakebetaling");
        koder.put("SE", "Søknad om ettergivelse");
        koder.put("SV", "Strafferettslig vurdering");
        koder.put("TE", "Tilbakebetaling endring");
        koder.put("TK", "Tidskonto");
        koder.put("TU", "Tipsutredning");
        koder.put("UA", "Utbetalt til annen");
        koder.put("-", "Udefinert");

        VALID_KODER = Collections.unmodifiableMap(koder);
    }

    public static final InfotrygdSakType SØKNAD = new InfotrygdSakType("S");
    
    private String kode;
    
    @JsonCreator
    public InfotrygdSakType(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODER.containsKey(kode)) {
            throw new IllegalArgumentException("Kode er ikke gyldig Infotrygd Saktype: " + kode);
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
