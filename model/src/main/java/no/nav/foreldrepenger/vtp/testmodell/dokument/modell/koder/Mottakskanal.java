package no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder;

/*
    reelt en kopi av no.nav.saf.Kanal
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public class Mottakskanal {
    private static List<String> VALID_KODE;

    static {
        List<String> koder = new ArrayList<>();
        koder.add("ALTINN");
        koder.add("EESSI");
        koder.add("EIA");
        koder.add("EKST_OPPS");
        koder.add("LOKAL_UTSKRIFT");
        koder.add("NAV_NO");
        koder.add("SENTRAL_UTSKRIFT");
        koder.add("SDP");
        koder.add("SKAN_NETS");
        koder.add("SKAN_PEN");
        koder.add("SKAN_IM");
        koder.add("TRYGDERETTEN");
        koder.add("HELSENETTET");
        koder.add("INGEN_DISTRIBUSJON");
        koder.add("NAV_NO_UINNLOGGET");
        koder.add("INNSENDT_NAV_ANSATT");
        koder.add("UKJENT");
        VALID_KODE = Collections.unmodifiableList(koder);
    }

    private String kode;

    public static final Mottakskanal ALTINN = new Mottakskanal("ALTINN");
    public static final Mottakskanal EESSI = new Mottakskanal("EESSI");
    public static final Mottakskanal EIA = new Mottakskanal("EIA");
    public static final Mottakskanal EKST_OPPS = new Mottakskanal("EKST_OPPS");
    public static final Mottakskanal LOKAL_UTSKRIFT = new Mottakskanal("LOKAL_UTSKRIFT");
    public static final Mottakskanal NAV_NO = new Mottakskanal("NAV_NO");
    public static final Mottakskanal SENTRAL_UTSKRIFT = new Mottakskanal("SENTRAL_UTSKRIFT");
    public static final Mottakskanal SDP = new Mottakskanal("SDP");
    public static final Mottakskanal SKAN_NETS = new Mottakskanal("SKAN_NETS");
    public static final Mottakskanal SKAN_PEN = new Mottakskanal("SKAN_PEN");
    public static final Mottakskanal SKAN_IM = new Mottakskanal("SKAN_IM");
    public static final Mottakskanal TRYGDERETTEN = new Mottakskanal("TRYGDERETTEN");
    public static final Mottakskanal HELSENETTET = new Mottakskanal("HELSENETTET");
    public static final Mottakskanal INGEN_DISTRIBUSJON = new Mottakskanal("INGEN_DISTRIBUSJON");
    public static final Mottakskanal NAV_NO_UINNLOGGET = new Mottakskanal("NAV_NO_UINNLOGGET");
    public static final Mottakskanal INNSENDT_NAV_ANSATT = new Mottakskanal("INNSENDT_NAV_ANSATT");
    public static final Mottakskanal UKJENT = new Mottakskanal("UKJENT");

    public Mottakskanal(String kode) {
        this.kode = kode == null ? this.kode : kode;
        if (kode != null && !VALID_KODE.contains(kode)) {
            throw new IllegalArgumentException("Kode er ikke implementert i Mottakskanal: " + kode);
        }
    }

    public static Mottakskanal fraKode(String kode) {
        if (VALID_KODE.contains(kode)) {
            return new Mottakskanal(kode);
        }
        return Mottakskanal.UKJENT;
    }

    @JsonValue
    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        return Objects.equals(getKode(), ((Mottakskanal) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }
}
