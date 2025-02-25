package no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum DokumenttypeId {

    SØKNAD_SVANGERSKAPSPENGER("I000001", "Søknad om svangerskapspenger"),
    SØKNAD_FORELDREPENGER_ADOPSJON("I000002", "Søknad om foreldrepenger ved adopsjon"),
    SØKNAD_ENGANGSSTØNAD_FØDSEL("I000003", "Søknad om engangsstønad ved fødsel"),
    SØKNAD_ENGANGSSTØNAD_ADOPSJON("I000004", "Søknad om engangsstønad ved adopsjon"),
    SØKNAD_FORELDREPENGER_FØDSEL("I000005", "Søknad om foreldrepenger ved fødsel"),
    FLEKSIBELT_UTTAK_FORELDREPENGER("I000006", "Utsettelse eller gradert uttak av foreldrepenger (fleksibelt uttak)"),
    FORELDREPENGER_ENDRING_SØKNAD("I000050", "Søknad om endring av uttak av foreldrepenger eller overføring av kvote"),

    // Inntektsmelding
    INNTEKTSMELDING("I000067", "Inntektsmelding"),

    // Inntekt
    INNTEKTSOPPLYSNING_SELVSTENDIG("I000007", "Inntektsopplysninger om selvstendig næringsdrivende og/eller frilansere som skal ha foreldrepenger eller svangerskapspenger"),
    INNTEKTSOPPLYSNINGER("I000026", "Inntektsopplysninger for arbeidstaker som skal ha sykepenger foreldrepenger svangerskapspenger pleie-/opplæringspenger"),
    INNTEKTSOPPLYSNINGERNY("I000226", "Inntektsopplysninger for arbeidstaker som skal ha sykepenger foreldrepenger svangerskapspenger pleie-/opplæringspenger og omsorgspenger"),
    INNTEKTSOPPLYSNINGSSKJEMA("I000052", "Inntektsopplysningsskjema"),
    DOK_INNTEKT("I000016", "Dokumentasjon av inntekt"),
    RESULTATREGNSKAP("I000032", "Resultatregnskap"),

    // Vedlegg fra brukerdialog og fyllut-sendinn
    LEGEERKLÆRING("I000023", "Legeerklæring"),
    DOK_INNLEGGELSE("I000037", "Dokumentasjon av innleggelse i helseinstitusjon"),
    DOK_MORS_UTDANNING_ARBEID_SYKDOM("I000038", "Dokumentasjon av mors utdanning arbeid eller sykdom"),
    DOK_MILITÆR_SIVIL_TJENESTE("I000039", "Dokumentasjon av militær- eller siviltjeneste"),
    DOKUMENTASJON_AV_TERMIN_ELLER_FØDSEL("I000041", "Dokumentasjon av termindato (lev. kun av mor), fødsel eller dato for omsorgsovertakelse"),
    DOKUMENTASJON_AV_OMSORGSOVERTAKELSE("I000042", "Dokumentasjon av dato for overtakelse av omsorg"),
    DOK_ETTERLØNN("I000044", "Dokumentasjon av etterlønn/sluttvederlag"),
    BESKRIVELSE_FUNKSJONSNEDSETTELSE("I000045", "Beskrivelse av funksjonsnedsettelse"),
    BEKREFTELSE_FRA_STUDIESTED("I000061", "Bekreftelse fra studiested/skole"),
    BEKREFTELSE_VENTET_FØDSELSDATO("I000062", "Bekreftelse på ventet fødselsdato"),
    FØDSELSATTEST("I000063", "Fødselsattest"),
    BEKREFTELSE_FRA_ARBEIDSGIVER("I000065", "Bekreftelse fra arbeidsgiver"),
    DOKUMENTASJON_ALENEOMSORG("I000110", "Dokumentasjon av aleneomsorg"),
    BEGRUNNELSE_SØKNAD_ETTERSKUDD("I000111", "Dokumentasjon av begrunnelse for hvorfor man søker tilbake i tid"),
    DOKUMENTASJON_INTRODUKSJONSPROGRAM("I000112", "Dokumentasjon av deltakelse i introduksjonsprogrammet"),
    DOKUMENTASJON_FORSVARSTJENESTE("I000116", "Bekreftelse på øvelse eller tjeneste i Forsvaret eller Sivilforsvaret"),
    DOKUMENTASJON_NAVTILTAK("I000117", "Bekreftelse på tiltak i regi av Arbeids- og velferdsetaten"),
    SEN_SØKNAD("I000118", "Begrunnelse for sen søknad"),
    MOR_INNLAGT("I000120", "Dokumentasjon på at mor er innlagt på sykehus"),
    MOR_SYK("I000121", "Dokumentasjon på at mor er syk"),
    FAR_INNLAGT("I000122", "Dokumentasjon på at far/medmor er innlagt på sykehus"),
    FAR_SYK("I000123", "Dokumentasjon på at far/medmor er syk"),
    BARN_INNLAGT("I000124", "Dokumentasjon på at barnet er innlagt på sykehus"),
    MOR_ARBEID_STUDIE("I000130", "Dokumentasjon på at mor studerer og arbeider til sammen heltid"),
    MOR_STUDIE("I000131", "Dokumentasjon på at mor studerer på heltid"),
    MOR_ARBEID("I000132", "Dokumentasjon på at mor er i arbeid"),
    MOR_KVALPROG("I000133", "Dokumentasjon av mors deltakelse i kvalifiseringsprogrammet"),
    SKATTEMELDING("I000140", "Skattemelding"),
    TERMINBEKREFTELSE("I000141", "Terminbekreftelse"),
    MEDISINSK_DOK("I000142", "Medisinsk dokumentasjon"),
    OPPHOLD("I000143", "Dokumentasjon på oppholdstillatelse"),
    REISE("I000144", "Dokumentasjon på reiser til og fra Norge"),
    OPPFØLGING("I000145", "Dokumentasjon på oppfølging i svangerskapet"),
    DOKUMENTASJON_INNTEKT("I000146", "Dokumentasjon på inntekt"),

    UDEFINERT("-", "Ukjent type dokument");

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
