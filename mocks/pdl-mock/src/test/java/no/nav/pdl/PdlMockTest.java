package no.nav.pdl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLResult;

import no.nav.foreldrepenger.vtp.testmodell.TestscenarioHenter;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pdl.graphql.GraphQLRequest;

public class PdlMockTest {

    private static final String SCENARIOID = "1";

    private static Testscenario testscenario;
    private static PdlMock pdlMock;

    private final ObjectMapper objectMapper = createObjectMapper();
    private final ObjectReader hentPersonReader = objectMapper.readerFor(HentPersonQueryResponse.class);
    private final ObjectReader hentGeografiskTilknytningReader = objectMapper.readerFor(HentGeografiskTilknytningQueryResponse.class);
    private final ObjectReader hentIdenterReader = objectMapper.readerFor(HentIdenterQueryResponse.class);
    private final ObjectReader hentIdenterBolkReader = objectMapper.readerFor(HentIdenterBolkQueryResponse.class);

    @BeforeAll
    public static void setup() throws IOException {
        var testScenarioRepository = new DelegatingTestscenarioRepository(
                TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        var testscenarioHenter = TestscenarioHenter.getInstance();
        var testscenarioObjekt = testscenarioHenter.hentScenario(SCENARIOID);
        var testscenarioJson = testscenarioHenter.toJson(testscenarioObjekt);
        testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());
        pdlMock = new PdlMock(testScenarioRepository);
    }

    @Test
    public void hent_person() throws JsonProcessingException {
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
    public void hent_person_med_historikk() throws JsonProcessingException {
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
    public void hentGeografiskTilknytningTest() throws JsonProcessingException {

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
    public void hent_identer() throws JsonProcessingException {
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
    public void hent_identer_gruppe() throws JsonProcessingException {
        // Arrange
        var søker = testscenario.getPersonopplysninger().getSøker();
        var ident = søker.getIdent();
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
    public void hent_aktørid_for_identliste() throws JsonProcessingException {
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

    // Hjelpemetode som oversetter resultat (LinkedHashMap) til objektgraf (GraphQLResult). Forenkler testing.
    private <T extends GraphQLResult> T konverterTilGraphResponse(Map<String, Object> response, ObjectReader objectReader) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(response);
        var graphResponse = objectReader.readValue(json);
        return (T) graphResponse;
    }

    private String byggProjectionPersonResponse(boolean historikk) {
        var projeksjon = new PersonResponseProjection()
                .foedsel(
                        new FoedselResponseProjection()
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
                                .forkortetNavn()
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
                .familierelasjoner(
                        new FamilierelasjonResponseProjection()
                                .relatertPersonsIdent()
                                .relatertPersonsRolle()
                )
                .adressebeskyttelse(
                        new AdressebeskyttelseResponseProjection()
                                .gradering()
                );
        return projeksjon.toString();
    }

    private static ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
                .setTimeZone(TimeZone.getTimeZone("Europe/Oslo"))
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
                .enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

}
