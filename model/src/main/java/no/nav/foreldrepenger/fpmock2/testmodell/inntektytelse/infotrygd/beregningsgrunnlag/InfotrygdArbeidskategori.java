package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

public class InfotrygdArbeidskategori {
    private static Map<String, String> VALID_KODER;
    static {
        Map<String, String> koder = new LinkedHashMap<>();
        koder.put("00", "Fisker");
        koder.put("01", "Arbeidstaker");
        koder.put("02", "Selvstendig næringsdrivende");
        koder.put("03", "Kombinasjon arbeidstaker og selvstendig næringsdrivende");
        koder.put("04", "Sjømann");
        koder.put("05", "Jordbruker");
        koder.put("06", "Dagpenger");
        koder.put("07", "Inaktiv");
        koder.put("08", "Arbeidstaker");
        koder.put("09", "Arbeidstaker");
        koder.put("10", "Sjømann");
        koder.put("11", "Annet");
        koder.put("12", "Arbeidstaker");
        koder.put("13", "Kombinasjon arbeidstaker og jordbruker");
        koder.put("14", "Annet");
        koder.put("15", "Selvstendig næringsdrivende");
        koder.put("16", "Selvstendig næringsdrivende");
        koder.put("17", "Kombinasjon arbeidstaker og fisker");
        koder.put("18", "Annet");
        koder.put("19", "Frilanser");
        koder.put("20", "Kombinasjon arbeidstaker og frilanser");
        koder.put("21", "Annet");
        koder.put("22", "Annet");
        koder.put("23", "Kombinasjon arbeidstaker og dagpenger");
        koder.put("24", "Frilanser");
        koder.put("25", "Kombinasjon arbeidstaker og frilanser");
        koder.put("26", "Dagmamma");
        koder.put("27", "Arbeidstaker");
        koder.put("99", "Annet");
        koder.put("-", "Udefinert");

        VALID_KODER = Collections.unmodifiableMap(koder);
    }

    public static final InfotrygdArbeidskategori ARBEIDSTAKER = new InfotrygdArbeidskategori("01");

    private String kode;

    @JsonCreator
    public InfotrygdArbeidskategori(String kode) {
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
