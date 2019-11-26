package no.nav.medl2.rest.api.v1;

final class MedlemskapsunntakApiParams { //NOSONAR
    static final String API_PARAM_INKLUDER_SPORINGSINFO = "Hvorvidt man er interessert i å se sporingsinformasjonen som er registrert på perioden.";
    static final String API_PARAM_UNNTAK_ID = "Den funksjonelle ID'en til perioden man ønsker å hente.";
    static final String API_PARAM_STATUSER = "Dersom man ikke ønsker å hente perioder uansett status kan man spesifisere hvilke man er interessert i her.";
    static final String API_PARAM_FRA_OG_MED = "Definerer startdatoen for vinduet man skal hente perioder for. Kun perioder som er innenfor dette vinduet vil returneres. Datoen er på ISO-8601-format.";
    static final String API_PARAM_TIL_OG_MED = "Definerer sluttdatoen for vinduet man skal hente perioder for. Kun perioder som er innenfor dette vinduet vil returneres. Datoen er på ISO-8601-format.";
    static final String API_PARAM_INKLUDER_SPORINGSINFO_PERSON = "Hvorvidt man er interessert i å se sporingsinformasjonen som er registrert på periodene som hentes ut.";
    static final String API_PARAM_EKSKLUDER_KILDER = "Dersom man ikke ønsker å hente perioder uansett kilde kan man spesifisere hvilke man er interessert i her.<br/>Kodeverk: <a href=\"https://kodeverk-web.nais.adeo.no/kodeverksoversikt/kodeverk/KildesystemMedl\" target=\"_blank\">KildesystemMedl</a>";
    static final String API_PARAM_PERSONIDENT = "Identen til personen man ønsker å slå opp. Dette kan være en naturlig ident (DNR/FNR) eller en aktørId.";
    static final String API_OPERATION_MEDLEMSKAPSUNNTAK = "Henter ut informasjon om et spesifikt medlemskapsunntak.";
    static final String API_OPERATION_MEDLEMSKAPSUNNTAK_I_PERIODE = "Henter ut medlemskapsunntak for en spesifikk person.";
}
