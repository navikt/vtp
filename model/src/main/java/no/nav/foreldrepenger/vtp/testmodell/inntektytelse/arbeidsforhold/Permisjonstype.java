package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Permisjonstype {

    private static List<String> VALID_KODER;

    static {
        List<String> koder = new ArrayList<>();
        koder.add("permisjon");
        koder.add("permisjonMedForeldrepenger");
        koder.add("permisjonVedMilitaertjeneste");
        koder.add("permittering");
        koder.add("utdanningspermisjon");
        koder.add("velferdspermisjon");

        VALID_KODER = Collections.unmodifiableList(koder);
    }

    public static final Permisjonstype PERMISJON = new Permisjonstype("permisjon"); //$NON-NLS-1$
    public static final Permisjonstype PERMISJON_MED_FORELDREPENGER = new Permisjonstype("permisjonMedForeldrepenger"); //$NON-NLS-1$
    public static final Permisjonstype PERMISJON_VED_MILITÆRTJENESTE = new Permisjonstype("permisjonVedMilitaertjeneste"); //$NON-NLS-1$
    public static final Permisjonstype PERMITTERING = new Permisjonstype("permittering"); //$NON-NLS-1$
    public static final Permisjonstype UTDANNINGSPERMISJON = new Permisjonstype("utdanningspermisjon"); //$NON-NLS-1$
    public static final Permisjonstype VELFERDSPERMISJON = new Permisjonstype("velferdspermisjon"); //$NON-NLS-1$

    private String kode;

    @JsonCreator
    public Permisjonstype(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODER.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i AAReg permisjonstype: " + kode);
        }
    }

    @JsonValue
    public String getKode() {return kode;}

    public void setKode(String kode){this.kode = kode;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        return Objects.equals(getKode(), ((Permisjonstype) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }
}
