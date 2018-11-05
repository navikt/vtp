package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder;

/*
    Hentet fra: https://modapp.adeo.no/kodeverksklient/viskodeverk/DokumentTypeId-er/6?8
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public class DokumenttypeId {

    private static List<String> VALID_KODER;
    static {
        List<String> koder = new ArrayList<>();
        koder.add("I000003");
        koder.add("I000004");
        koder.add("I000041");
        koder.add("I000042");
        koder.add("I000027");
        koder.add("I000067");
        koder.add("I000005");
        koder.add("I000002");
        koder.add("I000037");
        koder.add("I000023");
        koder.add("I000038");
        koder.add("I000007");
        koder.add("I000006");
        koder.add("I000048");
        koder.add("I000060");

        VALID_KODER = Collections.unmodifiableList(koder);
    }

    private String kode;

    public static DokumenttypeId FOEDSELSSOKNAD_ENGANGSSTONAD = new DokumenttypeId("I000003");
    public static DokumenttypeId ADOPSJONSSOKNAD_ENGANGSSTONAD = new DokumenttypeId("I000004");
    public static DokumenttypeId TERMINBEKREFTELSE = new DokumenttypeId("I000041");
    public static DokumenttypeId ADOPSJONSDOKUMENTASJON = new DokumenttypeId("I000042");
    public static DokumenttypeId KLAGEANKE = new DokumenttypeId("I000027");
    public static DokumenttypeId INNTEKTSMELDING = new DokumenttypeId("I000067");
    public static DokumenttypeId FOEDSELSSOKNAD_FORELDREPENGER = new DokumenttypeId("I000005");
    public static DokumenttypeId ADOPSJONSSOKNAD_FORELDREPENGER = new DokumenttypeId("I000002");
    public static DokumenttypeId DOK_INNLEGGELSE_HELSEINSTITUSJON = new DokumenttypeId("I000037");
    public static DokumenttypeId LEGEERKLARING = new DokumenttypeId("I000023");
    public static DokumenttypeId DOK_MORS_UTDANNING_ARBEID_SYKDOM = new DokumenttypeId("I000038");
    public static DokumenttypeId INNTEKTSKJEMA_SN_FL = new DokumenttypeId("I000007");
    public static DokumenttypeId UTSETTELSE_ELLER_GRADERTUTTAK_SOKNAD = new DokumenttypeId("I000006");
    public static DokumenttypeId BREV = new DokumenttypeId("I000048");
    public static DokumenttypeId ANNET = new DokumenttypeId("I000060");

    public DokumenttypeId(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODER.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i Joark arkivfiltype: " + kode);
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
        return Objects.equals(getKode(), ((Arkivfiltype) obj).getKode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }

}
