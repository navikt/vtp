package no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DokumenttypeId {

    SØKNAD_SVANGERSKAPSPENGER("I000001", "Søknad om svangerskapspenger"),
    SØKNAD_FORELDREPENGER_ADOPSJON("I000002", "Søknad om foreldrepenger ved adopsjon"),
    SØKNAD_ENGANGSSTØNAD_FØDSEL("I000003", "Søknad om engangsstønad ved fødsel"),
    SØKNAD_ENGANGSSTØNAD_ADOPSJON("I000004", "Søknad om engangsstønad ved adopsjon"),
    SØKNAD_FORELDREPENGER_FØDSEL("I000005", "Søknad om foreldrepenger ved fødsel"),
    FLEKSIBELT_UTTAK_FORELDREPENGER("I000006", "Utsettelse eller gradert uttak av foreldrepenger (fleksibelt uttak)"),
    INNTEKTSOPPLYSNING_SELVSTENDIG("I000007", "Inntektsopplysninger om selvstendig næringsdrivende og/eller frilansere som skal ha foreldrepenger eller svangerskapspenger"),
    LEGEERKLÆRING("I000023", "Legeerklæring"),
    INNTEKTSOPPLYSNINGER("I000026", "Inntektsopplysninger for arbeidstaker som skal ha sykepenger, foreldrepenger, svangerskapspenger, pleie-/opplæringspenger"),
    KLAGE_DOKUMENT("I000027", "Klage/anke"),
    DOK_INNLEGGELSE("I000037", "Dokumentasjon av innleggelse i helseinstitusjon"),
    DOK_MORS_UTDANNING_ARBEID_SYKDOM("I000038", "Dokumentasjon av mors utdanning, arbeid eller sykdom"),
    DOK_MILITÆR_SIVIL_TJENESTE("I000039", "Dokumentasjon av militær- eller siviltjeneste"),
    DOK_ASYL_DATO("I000040", "Dokumentasjon av dato for asyl"),
    DOKUMENTASJON_AV_TERMIN_ELLER_FØDSEL("I000041", "Dokumentasjon av termindato, fødsel eller dato for omsorgsovertakelse"),
    DOKUMENTASJON_AV_OMSORGSOVERTAKELSE("I000042", "Dokumentasjon av dato for overtakelse av omsorg"),
    FORELDREPENGER_ENDRING_SØKNAD("I000050", "Søknad om endring av uttak av foreldrepenger eller overføring av kvote"),
    INNTEKTSMELDING("I000067", "Inntektsmelding"),

    // DoksysKoder
    FPPROD_POSITIVT_VEDTAKSBREV("000048", null),
    FPPROD_INNHENT_DOKUMENTASJON("000049", null),
    FPPROD_BEHANDLING_AVBRUTT("000050", null),
    FPPROD_AVSLAGSBREV("000051", null),
    FPPROD_UENDRET_UTFALL("000052", null),
    FPPROD_VEDTAK_OM_AVVIST_KLAGE("000054", null),
    FPPROD_VEDTAK_OM_STADFESTELSE("000055", null),
    FPPROD_FORLENGET_SAKSBEHANDLINGSTID("000056", null),
    FPPROD_FORLENGET_SAKSBEHANDLINGSTID__TIDLIG_SØKNAD("000056", null),
    FPPROD_VARSEL_OM_REVURDERING("000058", null),
    FPPROD_VEDTAK_OPPHEVET__SENDT_TIL_NY_BEHANDLING("000059", null),
    FPPROD_OVERFØRING_TIL_NAV_KLAGEINSTANS("000060", null),
    FPPROD_INNVILGELSESBREV_FORELDREPENGER("000061", null),
    FPPROD_SVAR_PÅ_INNSYNSKRAV("000071", null),
    FPPROD_OPPHØR_BREV("000085", null),
    FPPROD_AVSLAGSBREV_FORELDREPENGER("000080", null),
    FPPROD_IKKE_MOTTATT_SØKNAD("000091", null),
    FPPROD_FRITEKSTBREV("000096", null),
    FPPROD_VEDTAK_MEDHOLD("000114", null),
    UDEFINERT("-", null);

    private static final Map<String, DokumenttypeId> BY_OFFIFIELLKODE = new ConcurrentHashMap<>();

    static {
        for (DokumenttypeId e: values()) {
            BY_OFFIFIELLKODE.put(e.kode, e);
        }
    }

    private String kode;
    private String termnavn;

    DokumenttypeId(String kode, String termnavn) {
        this.kode = kode;
        this.termnavn = termnavn;
    }

    public static DokumenttypeId valueOfKode(String kode) {
        return BY_OFFIFIELLKODE.get(kode);
    }

    @JsonValue
    public String getKode() {
        return kode;
    }

    public String getTermnavn() {
        return termnavn;
    }
}
