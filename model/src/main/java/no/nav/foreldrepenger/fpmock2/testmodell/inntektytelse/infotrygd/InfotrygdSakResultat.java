package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

/**
 * Infotrygd ytelse sak resultat.
 * 
 * Brukes ikke av SP, FA, BS, Pårørende sykdom, Pleiepenger (PN) har ikke opplysning om resultat.  Engangssstønad returnerer "I" (Innvilget) som standard.
 * Returneres kun for Enslig forsørger?
 * 
 * @see https://confluence.adeo.no/pages/viewpage.action?pageId=220537850
 */
public class InfotrygdSakResultat {
    private static Map<String, String> VALID_KODER;
    static {
        Map<String, String> koder = new LinkedHashMap<>();
        koder.put("?","beslutningsstøtte Besl st");
        koder.put("A","Avslag");
        koder.put("AK","avvist klage");
        koder.put("AV","advarsel");
        koder.put("DI","delvis innvilget");
        koder.put("DT","delvis tilbakebetale");
        koder.put("FB","ferdigbehandlet");
        koder.put("FI","fortsatt innvilget");
        koder.put("H","henlagt / trukket tilbake");
        koder.put("HB","henlagt / bortfalt");
        koder.put("I","Innvilget");
        koder.put("IN","innvilget ny situasjon");
        koder.put("IS","ikke straffbart");
        koder.put("IT","ikke tilbakebetale");
        koder.put("MO","midlertidig opphørt");
        koder.put("MT","mottatt");
        koder.put("O","opphørt");
        koder.put("PA","politianmeldelse");
        koder.put("R","redusert");
        koder.put("SB","sak i bero");
        koder.put("TB","tilbakebetale");
        koder.put("TH","tips henlagt");
        koder.put("TO","tips oppfølging");
        koder.put("Ø","økning");
        VALID_KODER = Collections.unmodifiableMap(koder);
    }

    private String kode;

    @JsonCreator
    public InfotrygdSakResultat(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODER.containsKey(kode)) {
            throw new IllegalArgumentException("Kode er ikke gyldig Infotrygd Sak Resultat: " + kode);
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
