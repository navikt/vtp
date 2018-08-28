package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

public class InfotrygdInntektsperiodeType {
    private static Map<String, String> VALID_KODER;
    static {
        Map<String, String> koder = new LinkedHashMap<>();
        koder.put("D", "Daglig");
        koder.put("U", "Ukentlig");
        koder.put("F", "Fjorten dager");
        koder.put("M", "Måned");
        koder.put("Å", "Årlig");
        koder.put("X", "Fastsatt etter 25 prosent avvik");
        koder.put("Y", "Premiegrunnlag oppdrastaker (gjelder de to første ukene)");
        koder.put("-", "Udefinert");

        VALID_KODER = Collections.unmodifiableMap(koder);
    }

    public static final InfotrygdInntektsperiodeType ÅRLIG = new InfotrygdInntektsperiodeType("Å");
    public static final InfotrygdInntektsperiodeType DAGLIG = new InfotrygdInntektsperiodeType("D");
    public static final InfotrygdInntektsperiodeType MÅNEDLIG = new InfotrygdInntektsperiodeType("M");
    
    private String kode;
    
    @JsonCreator
    public InfotrygdInntektsperiodeType(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODER.containsKey(kode)) {
            throw new IllegalArgumentException("Kode er ikke gyldig Infotrygd inntektsperiodetype: " + kode);
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
