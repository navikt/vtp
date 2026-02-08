package no.nav.pdl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;

import no.nav.foreldrepenger.vtp.testmodell.TestscenarioHenter;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pdl.graphql.GraphQLRequest;

class PdlMockTest extends PdlTestBase {

    private static final String SCENARIOID = "1";

    private static Testscenario testscenario;
    private static PdlMock pdlMock;

    private final ObjectReader hentPersonReader = JSON_MAPPER.readerFor(HentPersonQueryResponse.class);
    private final ObjectReader hentGeografiskTilknytningReader = JSON_MAPPER.readerFor(HentGeografiskTilknytningQueryResponse.class);
    private final ObjectReader hentIdenterReader = JSON_MAPPER.readerFor(HentIdenterQueryResponse.class);
    private final ObjectReader hentIdenterBolkReader = JSON_MAPPER.readerFor(HentIdenterBolkQueryResponse.class);

    @BeforeAll
    static void setup() {
        var testScenarioRepository = new DelegatingTestscenarioRepository(
                TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        var testscenarioHenter = TestscenarioHenter.getInstance();
        var testscenarioObjekt = testscenarioHenter.hentScenario(SCENARIOID);
        var testscenarioJson = testscenarioHenter.toJson(testscenarioObjekt);
        testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());
        pdlMock = new PdlMock(testScenarioRepository);
    }

    @Test
    void hent_person() throws JsonProcessingException {
        // Arrange
        var søker = testscenario.getPersonopplysninger().getSøker();
        var ident = søker.getIdent();
        var projection = byggProjectionPersonResponse(false);
        var query = String.format("query($ident: ID!){ hentPerson(ident: $ident) %s }", projection);
        var request = GraphQLRequest.builder().withQuery(query).withVariables(Map.of("ident", ident)).build();

        // Act
        var rawResponse = pdlMock.graphQLRequest(null, null, null, null, request);

        // Assert
        var response = (HentPersonQueryResponse) konverterTilGraphResponse(rawResponse, hentPersonReader);
        var person = response.hentPerson();
        assertThat(person).isNotNull();
        assertThat(person.getNavn().get(0).getFornavn()).isEqualTo(søker.getFornavn().toUpperCase());
        assertThat(person.getStatsborgerskap()).hasSize(1);
        assertThat(person.getFolkeregisterpersonstatus()).hasSize(1);
    }

    @Test
    void hent_person_med_historikk() throws JsonProcessingException {
        // Arrange
        var søker = testscenario.getPersonopplysninger().getSøker();
        var aktørIdent = søker.getAktørIdent();
        var projection = byggProjectionPersonResponse(true);
        var query = String.format("query { hentPerson(ident: \"%s\") %s }", aktørIdent, projection);
        var request = GraphQLRequest.builder().withQuery(query).build();

        // Act
        var rawResponse = pdlMock.graphQLRequest(null, null, null, null, request);

        // Assert
        var response = (HentPersonQueryResponse) konverterTilGraphResponse(rawResponse, hentPersonReader);
        var person = response.hentPerson();
        assertThat(person).isNotNull();
        assertThat(person.getNavn().get(0).getFornavn()).isEqualTo(søker.getFornavn().toUpperCase());

        // Sjekk at vi har fått med historikk
        assertThat(person.getStatsborgerskap()).hasSize(2);
        assertThat(person.getFolkeregisterpersonstatus()).hasSize(2);
    }

    @Test
    void hentGeografiskTilknytningTest() throws JsonProcessingException {

        // Arrange
        var søker = testscenario.getPersonopplysninger().getSøker();
        var aktørIdent = søker.getAktørIdent();
        var projection = new GeografiskTilknytningResponseProjection()
                .gtType()
                .gtLand()
                .regel()
                .toString();
        var query = String.format("query { hentGeografiskTilknytning(ident: \"%s\") %s }", aktørIdent, projection);
        var request = GraphQLRequest.builder().withQuery(query).build();

        // Act
        var rawResponse = pdlMock.graphQLRequest(null, null, null, null, request);

        // Assert
        var response = (HentGeografiskTilknytningQueryResponse) konverterTilGraphResponse(rawResponse, hentGeografiskTilknytningReader);
        var geografiskTilknytning = response.hentGeografiskTilknytning();
        assertThat(geografiskTilknytning).isNotNull();
        assertThat(geografiskTilknytning.getGtType()).isNotNull();
        assertThat(geografiskTilknytning.getRegel()).isNotNull();
    }

    @Test
    void hent_identer() throws JsonProcessingException {
        // Arrange
        var søker = testscenario.getPersonopplysninger().getSøker();
        var ident = søker.getIdent();
        var projection = new IdentlisteResponseProjection()
                .identer(new IdentInformasjonResponseProjection()
                        .ident()
                        .gruppe()
                )
                .toString();
        var query = String.format("query { hentIdenter(ident: \"%s\") %s }", ident, projection);
        var request = GraphQLRequest.builder().withQuery(query).build();

        // Act
        var rawResponse = pdlMock.graphQLRequest(null, null, null, null, request);

        // Assert
        var response = (HentIdenterQueryResponse) konverterTilGraphResponse(rawResponse, hentIdenterReader);
        var identliste = response.hentIdenter();
        assertThat(identliste).isNotNull();
        assertThat(identliste.getIdenter()).hasSize(2);

    }

    @Test
    void hent_identer_gruppe() throws JsonProcessingException {
        // Arrange
        var søker = testscenario.getPersonopplysninger().getSøker();
        var ident = søker.getAktørIdent();
        var projection = new IdentlisteResponseProjection()
                .identer(new IdentInformasjonResponseProjection()
                        .ident()
                        .gruppe()
                )
                .toString();
        var grupper = "FOLKEREGISTERIDENT";
        var query = String.format("query { hentIdenter(ident: \"%s\", grupper: [%s]) %s }", ident, grupper, projection);

        var request = GraphQLRequest.builder().withQuery(query).build();

        // Act
        var rawResponse = pdlMock.graphQLRequest(null, null, null, null, request);

        // Assert
        var response = (HentIdenterQueryResponse) konverterTilGraphResponse(rawResponse, hentIdenterReader);
        var identliste = response.hentIdenter();
        assertThat(identliste).isNotNull();
        assertThat(identliste.getIdenter()).hasSize(1);

    }

    @Test
    void hent_aktørid_for_identliste() throws JsonProcessingException {
        // Arrange
        var søker = testscenario.getPersonopplysninger().getSøker();

        var folkeregisterId = søker.getIdent();
        var aktørIdent = søker.getAktørIdent();

        var projection = new HentIdenterBolkResultResponseProjection()
                .ident()
                .identer(new IdentInformasjonResponseProjection()
                        .ident()
                        .gruppe()
                )
                .toString();

        var query = String.format("query { hentIdenterBolk(identer: [\"%s\"]) %s }", folkeregisterId, projection);

        var request = GraphQLRequest.builder().withQuery(query).build();

        // Act
        var rawResponse = pdlMock.graphQLRequest(null, null, null, null, request);

        // Assert
        var response = (HentIdenterBolkQueryResponse) konverterTilGraphResponse(rawResponse, hentIdenterBolkReader);
        var identerliste = response.hentIdenterBolk();
        assertThat(identerliste).isNotNull();
        assertThat(identerliste).hasSize(1);
        assertThat(identerliste.get(0).getIdenter()).hasSize(2);

        assertThat(
                identerliste.get(0).getIdenter().stream()
                        .filter(i -> i.getGruppe().equals(IdentGruppe.FOLKEREGISTERIDENT))
                        .map(IdentInformasjon::getIdent)
                        .findFirst()
                        .orElseThrow()
        )
                .isEqualTo(folkeregisterId);

        assertThat(
                identerliste.get(0).getIdenter().stream()
                        .filter(i -> i.getGruppe().equals(IdentGruppe.AKTORID))
                        .map(IdentInformasjon::getIdent)
                        .findFirst()
                        .orElseThrow()
        )
                .isEqualTo(aktørIdent);
    }

    private String byggProjectionPersonResponse(boolean historikk) {
        var projeksjon = new PersonResponseProjection()
                .foedselsdato(
                        new FoedselsdatoResponseProjection()
                                .foedselsdato()
                )
                .doedsfall(
                        new DoedsfallResponseProjection()
                                .doedsdato()
                )
                .doedfoedtBarn(
                        new DoedfoedtBarnResponseProjection()
                                .dato()
                )
                .navn(new PersonNavnParametrizedInput(historikk),
                        new NavnResponseProjection()
                                .fornavn()
                                .mellomnavn()
                                .etternavn()
                )
                .statsborgerskap(
                        new PersonStatsborgerskapParametrizedInput(historikk),
                        new StatsborgerskapResponseProjection()
                                .land()
                )
                .kjoenn(new PersonKjoennParametrizedInput(historikk),
                        new KjoennResponseProjection()
                                .kjoenn()
                )
                .bostedsadresse(
                        new PersonBostedsadresseParametrizedInput(historikk),
                        new BostedsadresseResponseProjection()
                            .angittFlyttedato()
                            .gyldigFraOgMed()
                            .gyldigTilOgMed()
                            .vegadresse(new VegadresseResponseProjection()
                                    .matrikkelId()
                                    .adressenavn()
                                    .husnummer()
                                    .husbokstav()
                                    .postnummer()))
                .oppholdsadresse(
                        new OppholdsadresseResponseProjection()
                                .vegadresse(new VegadresseResponseProjection()
                                        .matrikkelId()
                                        .adressenavn()
                                        .husnummer()
                                        .husbokstav()
                                        .postnummer())
                )
                .folkeregisterpersonstatus(
                        new PersonFolkeregisterpersonstatusParametrizedInput(historikk),
                        new FolkeregisterpersonstatusResponseProjection()
                                .status()
                                .forenkletStatus()
                )
                .sivilstand(
                        new SivilstandResponseProjection()
                            .type()
                            .relatertVedSivilstand()
                )
                .forelderBarnRelasjon(
                        new ForelderBarnRelasjonResponseProjection()
                                .relatertPersonsIdent()
                                .relatertPersonsRolle()
                                .minRolleForPerson()
                )
                .adressebeskyttelse(
                        new AdressebeskyttelseResponseProjection()
                                .gradering()
                );
        return projeksjon.toString();
    }
}
