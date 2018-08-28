package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

/**
 * Infotrygd stønadsklasse 2.
 * 
 * @see https://confluence.adeo.no/pages/viewpage.action?pageId=220537850
 */
public class InfotrygdBeregningsgrunnlagBehandlingtema {
    private static Map<String, Entry<String, String>> VALID_KODER;
    static {
        Map<String, Entry<String, String>> koder = new HashMap<>();

        registrer(koder, null, "Sykepenger", "SP");
        registrer(koder, "SP", "Sykepenger", "SP");
        
        registrer(koder, "OM", "Omsorgspenger", "");
        registrer(koder, "OP", "Opplæringspenger", "");
        registrer(koder, "PB", "Pleiepenger sykt barn (identdato før 1.10.2017)", "");
        registrer(koder, "PI", "Pleiepenger (identdato før 1.10.2017)", "");
        registrer(koder, "PP", "Pleiepenger pårørende", "");
        registrer(koder, "PN", "Pleiepenger ny ordning (identdato etter 1.10.2017)", "");
        
        registrer(koder, "FØ", "Foreldrepenger fødsel", "FA");
        registrer(koder, "AP", "Foreldrepenger Adopsjon", "FA");
        registrer(koder, "SV", "Svangerskapspenger", "FA");
        registrer(koder, "Z", "Svangerskapspenger", "FA");
        
        registrer(koder, "AE", "Engangsstønad Adopsjon", "FA");
        registrer(koder, "FE", "Engansstønad Fødsel", "FA");
        
        VALID_KODER = Collections.unmodifiableMap(koder);
    }

    public static final InfotrygdBeregningsgrunnlagBehandlingtema FORELDREPENGER_FØDSEL = new InfotrygdBeregningsgrunnlagBehandlingtema("FØ");

    public static final InfotrygdBeregningsgrunnlagBehandlingtema SYKEPENGER = new InfotrygdBeregningsgrunnlagBehandlingtema("SP");

    private String kode;

    @JsonCreator
    public InfotrygdBeregningsgrunnlagBehandlingtema(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODER.containsKey(kode)) {
            throw new IllegalArgumentException("Kode er ikke gyldig Infotrygd Behandlingstema: " + kode);
        }
    }

    private static void registrer(Map<String, Entry<String, String>> koder, String kode, String beskrivelse, String tema) {
        koder.put(kode, new SimpleEntry<>(beskrivelse, tema));
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
    
    public String getTema() {
        Entry<String, String> list = VALID_KODER.get(kode);
        return list.getValue();
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
