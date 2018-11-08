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
        koder.add("000049");
        koder.add("000048");
        koder.add("000050");
        koder.add("000051");
        koder.add("000052");
        koder.add("000058");
        koder.add("000056");
        koder.add("000056");
        koder.add("000054");
        koder.add("000055");
        koder.add("000059");
        koder.add("000060");
        koder.add("000071");
        koder.add("000061");
        koder.add("000085");
        koder.add("000091");
        koder.add("000080");
        koder.add("000096");
        koder.add("000056");

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
    public static DokumenttypeId FPPROD_INNHENT_DOKUMENTASJON = new DokumenttypeId("000049");
    public static DokumenttypeId FPPROD_POSITIVT_VEDTAKSBREV = new DokumenttypeId("000048");
    public static DokumenttypeId FPPROD_BEHANDLING_AVBRUTT = new DokumenttypeId("000050");
    public static DokumenttypeId FPPROD_AVSLAGSBREV = new DokumenttypeId("000051");
    public static DokumenttypeId FPPROD_UENDRET_UTFALL = new DokumenttypeId("000052");
    public static DokumenttypeId FPPROD_VARSEL_OM_REVURDERING = new DokumenttypeId("000058");
    public static DokumenttypeId FPPROD_FORLENGET_SAKSBEHANDLINGSTID = new DokumenttypeId("000056");
    public static DokumenttypeId FPPROD_FORLENGET_SAKSBEHANDLINGSTID_MEDLEMSKAP = new DokumenttypeId("000056");
    public static DokumenttypeId FPPROD_VEDTAK_OM_AVVIST_KLAGE = new DokumenttypeId("000054");
    public static DokumenttypeId FPPROD_VEDTAK_OM_STADFESTELSE = new DokumenttypeId("000055");
    public static DokumenttypeId FPPROD_VEDTAK_OPPHEVET__SENDT_TIL_NY_BEHANDLING = new DokumenttypeId("000059");
    public static DokumenttypeId FPPROD_OVERFØRING_TIL_NAV_KLAGEINSTANS = new DokumenttypeId("000060");
    public static DokumenttypeId FPPROD_SVAR_PÅ_INNSYNSKRAV = new DokumenttypeId("000071");
    public static DokumenttypeId FPPROD_INNVILGELSESBREV_FORELDREPENGER = new DokumenttypeId("000061");
    public static DokumenttypeId FPPROD_OPPHØR_BREV = new DokumenttypeId("000085");
    public static DokumenttypeId FPPROD_IKKE_MOTTATT_SØKNAD = new DokumenttypeId("000091");
    public static DokumenttypeId FPPROD_AVSLAGSBREV_FORELDREPENGER = new DokumenttypeId("000080");
    public static DokumenttypeId FPPROD_FRITEKSTBREV = new DokumenttypeId("000096");
    public static DokumenttypeId FPPROD_FORLENGET_SAKSBEHANDLINGSTID__TIDLIG_SØKNAD = new DokumenttypeId("000056");

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
