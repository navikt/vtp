package no.nav.fager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import no.nav.fager.graphql.GraphQLRequest;
import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.SakModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.ArbeidsgiverPortalRepository;

@ExtendWith(MockitoExtension.class)
class FagerGraphqlTjenesteTest {

    @Mock
    private ArbeidsgiverPortalRepository arbeidsgiverPortalRepository;

    private FagerGraphqlTjeneste graphQLTjeneste;

    @BeforeEach
    void setUp() {
        graphQLTjeneste = new FagerGraphqlTjeneste(arbeidsgiverPortalRepository);
    }

    @Test
    void nySakTest() {
        // Arrange
        var forespørselUuid = "c91385c1-2b8f-483b-bf4e-b6a988cd6ad5";
        var merkelap = "Inntektsmelding foreldrepenger";
        var orgnr = "992257822";
        var tittel = "Inntektsmelding for Louise Elg (04.11.92)";
        var lenke = "https://arbeidsgiver.intern.dev.nav.no/fp-im-dialog/" + forespørselUuid;
        var status = "UNDER_BEHANDLING";
        var overstyrtStatusTekst = "";

        var request = GraphQLRequest.builder()
                .withQuery("mutation nySak { nySak: nySak(grupperingsid: \"" + forespørselUuid
                        + "\", merkelapp: \"" + merkelap
                        + "\", virksomhetsnummer: \"" + orgnr
                        + "\", mottakere: [ { altinn: { serviceCode: \"4936\", serviceEdition: \"1\" } } ], tittel: \"" + tittel
                        + "\", lenke: \"" + lenke
                        + "\", initiellStatus: " + status
                        + ", overstyrStatustekstMed: \"" + overstyrtStatusTekst + "\"){ __typename ...on NySakVellykket { id } ...on UgyldigMerkelapp { feilmelding } ...on UgyldigMottaker { feilmelding } ...on DuplikatGrupperingsid { feilmelding } ...on DuplikatGrupperingsidEtterDelete { feilmelding } ...on UkjentProdusent { feilmelding } ...on UkjentRolle { feilmelding } } }")
                .withOperationName("nySak")
                .build();

        // Mock repo
        var sakId = UUID.randomUUID();
        when(arbeidsgiverPortalRepository.nySak(forespørselUuid, merkelap, orgnr, tittel, lenke, overstyrtStatusTekst)).thenReturn(sakId);

        // Act
        var result = graphQLTjeneste.sak(request).toSpecification();

        // Assert
        assertThat(result.get("data")).isNotNull()
                .extracting("nySak")
                .extracting("id")
                .matches(id -> id.equals(sakId.toString()));
    }

    @Test
    void nySakStatusTest() {
        // Arrange
        var sakId = "fa24e8ad-b32f-4da5-aee9-ffe96a00152a";
        var nyStatus = "FERDIG";
        var overstyrtStatusTekst = "";

        var request = GraphQLRequest.builder()
                .withQuery("mutation nyStatusSak { nyStatusSak: nyStatusSak(id: \"" + sakId
                        + "\", nyStatus: " + nyStatus
                        + ", overstyrStatustekstMed: \"" + overstyrtStatusTekst + "\"){ __typename ...on NyStatusSakVellykket { id } ...on UgyldigMerkelapp { feilmelding } ...on Konflikt { feilmelding } ...on UkjentProdusent { feilmelding } ...on SakFinnesIkke { feilmelding } } }")
                .withOperationName("nyStatusSak")
                .build();

        // Mock repo
        when(arbeidsgiverPortalRepository.nyStatusSak(sakId, SakModell.SakStatus.valueOf(nyStatus), overstyrtStatusTekst)).thenReturn(UUID.fromString(sakId));

        // Act
        var result = graphQLTjeneste.sak(request).toSpecification();

        // Assert
        assertThat(result.get("data")).isNotNull()
                .extracting("nyStatusSak")
                .extracting("id")
                .matches(id -> id.equals(sakId));
    }

    @Test
    void nyTilleggsinformasjonSakTest() {
        // Arrange
        var sakId = "fa24e8ad-b32f-4da5-aee9-ffe96a00152a";
        var tilleggsinfo = "Utført i Altinn eller i bedriftens lønns- og personalsystem";

        var request = GraphQLRequest.builder()
                .withQuery("mutation tilleggsinformasjonSak { tilleggsinformasjonSak: tilleggsinformasjonSak(id: \"" + sakId
                        + "\", tilleggsinformasjon: \"" + tilleggsinfo
                        + "\"){ __typename ...on TilleggsinformasjonSakVellykket { id } ...on SakFinnesIkke { feilmelding } ...on Konflikt { feilmelding } ...on UgyldigMerkelapp { feilmelding } ...on UkjentProdusent { feilmelding } } }")
                .withOperationName("tilleggsinformasjonSak")
                .build();

        // Mock repo
        when(arbeidsgiverPortalRepository.tilleggsinformasjonSak(sakId, tilleggsinfo)).thenReturn(UUID.fromString(sakId));

        // Act
        var result = graphQLTjeneste.sak(request).toSpecification();

        // Assert
        assertThat(result.get("data")).isNotNull()
                .extracting("tilleggsinformasjonSak")
                .extracting("id")
                .matches(id -> id.equals(sakId));
    }

    @Test
    void hardDeleteSakTest() {
        // Arrange
        var sakId = "fa24e8ad-b32f-4da5-aee9-ffe96a00152a";

        var request = GraphQLRequest.builder()
                .withQuery("mutation hardDeleteSak { hardDeleteSak: hardDeleteSak(id: \"" + sakId
                        + "\"){ __typename ...on HardDeleteSakVellykket { id } ...on SakFinnesIkke { feilmelding } ...on UgyldigMerkelapp { feilmelding } ...on UkjentProdusent { feilmelding } } }")
                .withOperationName("hardDeleteSak")
                .build();

        // Mock repo
        when(arbeidsgiverPortalRepository.slettSak(sakId)).thenReturn(UUID.fromString(sakId));

        // Act
        var result = graphQLTjeneste.sak(request).toSpecification();

        // Assert
        assertThat(result.get("data")).isNotNull()
                .extracting("hardDeleteSak")
                .extracting("id")
                .matches(id -> id.equals(sakId));
    }

    @Test
    void nyOppgaveTest() {
        // Arrange
        var merkelapp = "Inntektsmelding foreldrepenger";
        var tekst = "Innsending av inntektsmelding for foreldrepenger";
        var forespørselUuid = "283df343-54e9-4600-ab90-55a62605f74d";
        var lenke = "https://arbeidsgiver.intern.dev.nav.no/fp-im-dialog/" + forespørselUuid;
        var orgnr = "991078045";
        var request = GraphQLRequest.builder()
                .withQuery(
                        "mutation nyOppgave { nyOppgave: nyOppgave(nyOppgave: { mottaker: { altinn: { serviceCode: \"4936\", serviceEdition: \"1\" } }, mottakere: [  ], notifikasjon: { merkelapp: \""
                                + merkelapp
                                + "\", tekst: \"" + tekst
                                + "\", lenke: \"" + lenke
                                + "\" }, metadata: { virksomhetsnummer: \"" + orgnr
                                + "\", eksternId: \"" + forespørselUuid
                                + "\", grupperingsid: \"" + forespørselUuid + "\" }, eksterneVarsler: [ { altinntjeneste: { mottaker: { serviceCode: \"4936\", serviceEdition: \"1\" }, tittel: \"Du har fått en oppgave fra Nav\", innhold: \"En av dine ansatte har sendt søknad om foreldrepenger og vi trenger inntektsmelding for å behandle søknaden. Logg inn på Min side – arbeidsgiver på nav.no\", sendetidspunkt: { sendevindu: LOEPENDE } } } ], paaminnelse: { tidspunkt: { etterOpprettelse: \"PT336H\" }, eksterneVarsler: [ { altinntjeneste: { mottaker: { serviceCode: \"4936\", serviceEdition: \"1\" }, tittel: \"Påminnelse: Du har en oppgave fra Nav\", innhold: \"En av dine ansatte har sendt søknad om foreldrepenger og vi trenger inntektsmelding for å behandle søknaden. Logg inn på Min side – arbeidsgiver på nav.no\", sendevindu: LOEPENDE } } ] } }){ __typename ...on NyOppgaveVellykket { id } ...on UgyldigMerkelapp { feilmelding } ...on UgyldigMottaker { feilmelding } ...on DuplikatEksternIdOgMerkelapp { feilmelding } ...on UkjentProdusent { feilmelding } ...on UkjentRolle { feilmelding } ...on UgyldigPaaminnelseTidspunkt { feilmelding } } }")
                .withOperationName("nyOppgave")
                .build();

        // Mock repo
        var oppgaveId = UUID.randomUUID();
        when(arbeidsgiverPortalRepository.nyOppgave(forespørselUuid, merkelapp, orgnr, tekst, lenke)).thenReturn(oppgaveId);

        // Act
        var result = graphQLTjeneste.oppgave(request).toSpecification();

        // Assert
        assertThat(result.get("data")).isNotNull()
                .extracting("nyOppgave")
                .extracting("id")
                .isNotNull()
                .matches(id -> id.equals(oppgaveId.toString()));
    }

    @Test
    void oppgaveUtførtTest() {
        // Arrange
        var oppgaveId = UUID.randomUUID();
        var request = GraphQLRequest.builder()
                .withQuery(
                        "mutation oppgaveUtfoert { oppgaveUtfoert: oppgaveUtfoert(id: \"" + oppgaveId + "\", utfoertTidspunkt: \"2024-11-04T20:58:49.73357925+01:00\"){ __typename ...on OppgaveUtfoertVellykket { id } ...on UgyldigMerkelapp { feilmelding } ...on NotifikasjonFinnesIkke { feilmelding } ...on UkjentProdusent { feilmelding } } }")
                .withOperationName("oppgaveUtfoert")
                .build();

        // Mock repo
        when(arbeidsgiverPortalRepository.utførOppgave(oppgaveId.toString())).thenReturn(oppgaveId);

        // Act
        var result = graphQLTjeneste.oppgave(request).toSpecification();

        // Assert
        assertThat(result.get("data")).isNotNull()
                .extracting("oppgaveUtfoert")
                .extracting("id")
                .matches(id -> id.equals(oppgaveId.toString()));
    }

    @Test
    void oppgaveUtgåttTest() {
        // Arrange
        var oppgaveId = UUID.randomUUID();
        var request = GraphQLRequest.builder()
                .withQuery(
                        "mutation oppgaveUtgaatt { oppgaveUtgaatt: oppgaveUtgaatt(id: \"" + oppgaveId + "\", utgaattTidspunkt: \"2024-11-04T20:58:49.73357925+01:00\"){ __typename ...on OppgaveUtgaattVellykket { id } ...on UgyldigMerkelapp { feilmelding } ...on NotifikasjonFinnesIkke { feilmelding } ...on UkjentProdusent { feilmelding } } }")
                .withOperationName("oppgaveUtgaatt")
                .build();

        // Mock repo
        when(arbeidsgiverPortalRepository.utgåttOppgave(oppgaveId.toString())).thenReturn(oppgaveId);

        // Act
        var result = graphQLTjeneste.oppgave(request).toSpecification();

        // Assert
        assertThat(result.get("data")).isNotNull()
                .extracting("oppgaveUtgaatt")
                .extracting("id")
                .matches(id -> id.equals(oppgaveId.toString()));
    }
}
