package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/*
Ref: https://app-t11.adeo.no/inntektstub/api/v1/kodeverk/Inntektstype
[
{"navn":"Loennsinntekt","term":"Lønnsinntekt"},
{"navn":"Naeringsinntekt","term":"Næringsinntekt"},
{"navn":"PensjonEllerTrygd","term":"Pensjon eller trygd"},
{"navn":"YtelseFraOffentlige","term":"Ytelse fra offentlige"}]
 */

public class InntektType {

    private static List<String> VALID_KODER;
    static {
        List<String> koder = new ArrayList<>();
        koder.add("Lønnsinntekt");
        koder.add("Næringsinntekt");
        koder.add("PensjonEllerTrygd");
        koder.add("YtelseFraOffentlige");

        VALID_KODER = Collections.unmodifiableList(koder);
    }

    public static final InntektType LØNNSINNTEKT = new InntektType("Lønnsinntekt");
    public static final InntektType NÆRINGSINNTEKT = new InntektType("Næringsinntekt");
    public static final InntektType PENSJON_ELLER_TRYGD = new InntektType("PensjonEllerTrygd");
    public static final InntektType YTELSE_FRA_OFFENTLIGE = new InntektType("YtelseFraOffentlige");

    private String kode;

    @JsonCreator
    public InntektType(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODER.contains(kode)) {
            throw new IllegalArgumentException("Kode er ikke gyldig Inntektskomponent inntektstype: " + kode);
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
        return Objects.equals(getKode(), ((InntektType) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }





}
