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
    
    private static List<String> VALID_KODER = new ArrayList<>();

    private String kode;

    public static DokumenttypeId FOEDSELSSOKNAD_ENGANGSSTONAD = createDokumenttypeId("I000003");
    public static DokumenttypeId ADOPSJONSSOKNAD_ENGANGSSTONAD = createDokumenttypeId("I000004");
    public static DokumenttypeId TERMINBEKREFTELSE = createDokumenttypeId("I000041");
    public static DokumenttypeId ADOPSJONSDOKUMENTASJON = createDokumenttypeId("I000042");
    public static DokumenttypeId KLAGEANKE = createDokumenttypeId("I000027");
    public static DokumenttypeId INNTEKTSMELDING = createDokumenttypeId("I000067");
    public static DokumenttypeId FOEDSELSSOKNAD_FORELDREPENGER = createDokumenttypeId("I000005");
    public static DokumenttypeId ADOPSJONSSOKNAD_FORELDREPENGER = createDokumenttypeId("I000002");
    public static DokumenttypeId DOK_INNLEGGELSE_HELSEINSTITUSJON = createDokumenttypeId("I000037");
    public static DokumenttypeId LEGEERKLARING = createDokumenttypeId("I000023");
    public static DokumenttypeId DOK_MORS_UTDANNING_ARBEID_SYKDOM = createDokumenttypeId("I000038");
    public static DokumenttypeId INNTEKTSKJEMA_SN_FL = createDokumenttypeId("I000007");
    public static DokumenttypeId UTSETTELSE_ELLER_GRADERTUTTAK_SOKNAD = createDokumenttypeId("I000006");
    public static DokumenttypeId FPPROD_INNHENT_DOKUMENTASJON = createDokumenttypeId("000049");
    public static DokumenttypeId FPPROD_POSITIVT_VEDTAKSBREV = createDokumenttypeId("000048");
    public static DokumenttypeId FPPROD_BEHANDLING_AVBRUTT = createDokumenttypeId("000050");
    public static DokumenttypeId FPPROD_AVSLAGSBREV = createDokumenttypeId("000051");
    public static DokumenttypeId FPPROD_UENDRET_UTFALL = createDokumenttypeId("000052");
    public static DokumenttypeId FPPROD_VARSEL_OM_REVURDERING = createDokumenttypeId("000058");
    public static DokumenttypeId FPPROD_FORLENGET_SAKSBEHANDLINGSTID = createDokumenttypeId("000056");
    public static DokumenttypeId FPPROD_FORLENGET_SAKSBEHANDLINGSTID_MEDLEMSKAP = createDokumenttypeId("000056");
    public static DokumenttypeId FPPROD_VEDTAK_OM_AVVIST_KLAGE = createDokumenttypeId("000054");
    public static DokumenttypeId FPPROD_VEDTAK_OM_STADFESTELSE = createDokumenttypeId("000055");
    public static DokumenttypeId FPPROD_VEDTAK_OPPHEVET__SENDT_TIL_NY_BEHANDLING = createDokumenttypeId("000059");
    public static DokumenttypeId FPPROD_OVERFØRING_TIL_NAV_KLAGEINSTANS = createDokumenttypeId("000060");
    public static DokumenttypeId FPPROD_SVAR_PÅ_INNSYNSKRAV = createDokumenttypeId("000071");
    public static DokumenttypeId FPPROD_INNVILGELSESBREV_FORELDREPENGER = createDokumenttypeId("000061");
    public static DokumenttypeId FPPROD_OPPHØR_BREV = createDokumenttypeId("000085");
    public static DokumenttypeId FPPROD_IKKE_MOTTATT_SØKNAD = createDokumenttypeId("000091");
    public static DokumenttypeId FPPROD_AVSLAGSBREV_FORELDREPENGER = createDokumenttypeId("000080");
    public static DokumenttypeId FPPROD_FRITEKSTBREV = createDokumenttypeId("000096");
    public static DokumenttypeId FPPROD_FORLENGET_SAKSBEHANDLINGSTID__TIDLIG_SØKNAD = createDokumenttypeId("000056");
    public static DokumenttypeId FORELDREPENGER_ENDRING_SØKNAD = createDokumenttypeId("I000050");
    public static DokumenttypeId FPPROD_VEDTAK_MEDHOLD = createDokumenttypeId("000114");

    public DokumenttypeId(String kode){
        this.kode = kode == null ? this.kode : kode;
        if(kode != null && !VALID_KODER.contains(kode)){
            throw new IllegalArgumentException("Kode er ikke implementert i Joark dokumenttytpeId: " + kode);
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
    public String toString(){
        return getKode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKode());
    }
    
    private static DokumenttypeId createDokumenttypeId(String kode) {
        ArrayList<String> koder = new ArrayList<>();
        VALID_KODER.forEach(k -> koder.add(k));
        koder.add(kode);
        VALID_KODER = Collections.unmodifiableList(koder);
        return new DokumenttypeId(kode);
    }

}
