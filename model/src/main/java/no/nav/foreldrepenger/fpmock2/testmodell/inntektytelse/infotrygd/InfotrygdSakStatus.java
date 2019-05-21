package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

public class InfotrygdSakStatus {
    private static Map<String, String> VALID_KODER;
    static {
        Map<String, String> koder = new LinkedHashMap<>();
        koder.put("IP", "Ikke påbegynt");
        koder.put("UB", "Under behandling");
        koder.put("SG", "Sendt til saksbehandler");
        koder.put("UK", "Underkjent av saksbehandler");
        koder.put("RT", "Returnert");
        koder.put("FB", "Ferdig behandlet");
        koder.put("FI",  "Ferdig iverksatt");
        koder.put("RF", "Returnert feilsendt");
        koder.put("ST", "Sendt");
        koder.put("VD", "Videresendt direktoratet");
        koder.put("VI", "Venter iverksetting");
        koder.put("VT", "Videresendt trygderetten");
        
        VALID_KODER = Collections.unmodifiableMap(koder);
    }

    public static final InfotrygdSakStatus FERDIG_IVERKSATT=new InfotrygdSakStatus("FI");
    public static final InfotrygdSakStatus FERDIG_BEHANDLET=new InfotrygdSakStatus("FB");
    
    
    private String kode;

    @JsonCreator
    public InfotrygdSakStatus(String kode) {
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
