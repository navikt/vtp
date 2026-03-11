package no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder;

/*
    Hentet fra: https://confluence.adeo.no/spaces/BOA/pages/494765717/Fagarkiv+-+Avslutning+Avlevering+og+Kassasjon#FagarkivAvslutning%2CAvleveringogKassasjon-SakStatus
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public class Sakstatus {

    private static List<String> VALID_KODE;

    static {
        List<String> koder = new ArrayList<>();
        koder.add("AAPEN");
        koder.add("AVSLUTTET");
        koder.add("AVLEVERT");
        koder.add("AVBRUTT");
        koder.add("KASSERT");

        VALID_KODE = Collections.unmodifiableList(koder);
    }

    @JsonValue
    private String kode;

    public final static Sakstatus AAPEN = new Sakstatus("AAPEN");
    public final static Sakstatus AVSLUTTET = new Sakstatus("AVSLUTTET");
    public final static Sakstatus AVLEVERT = new Sakstatus("AVLEVERT");
    public final static Sakstatus AVBRUTT = new Sakstatus("AVBRUTT");
    public final static Sakstatus KASSERT = new Sakstatus("KASSERT");

    public Sakstatus(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODE.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i Joark sakstatus: " + kode);
        }
    }

    public String getKode() {return kode;}

    public void setKode(String kode){this.kode = kode;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        return Objects.equals(getKode(), ((Sakstatus) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }
}
