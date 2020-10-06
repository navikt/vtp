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
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pdl.graphql.GraphQLRequest;

public class PdlMockTest {

    private static TestscenarioRepository testScenarioRepository;
    private static TestscenarioHenter testscenarioHenter;
    private static PdlMock pdlMock;

    private final ObjectMapper objectMapper = createObjectMapper();
    private final ObjectReader objectReaderHentPersonResponse = objectMapper.readerFor(HentPersonQueryResponse.class);

    @BeforeAll
    public static void setup() throws IOException {
        testScenarioRepository = new DelegatingTestscenarioRepository(
                TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        testscenarioHenter = TestscenarioHenter.getInstance();


        pdlMock = new PdlMock(testScenarioRepository);
    }

    @Test
    public void hent_person() throws JsonProcessingException {
        // Arrange
        var testscenarioObjekt = testscenarioHenter.hentScenario("1");
        var testscenarioJson = testscenarioObjekt == null ? "{}" : testscenarioHenter.toJson(testscenarioObjekt);
        var testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());
        var søker = testscenario.getPersonopplysninger().getSøker();

        var ident = søker.getIdent();
        var projection = byggProjectionPersonResponse();
        var query = String.format("query { hentPerson(ident: \"%s\") %s }", ident, projection);

        var request = GraphQLRequest.builder().withQuery(query).build();

        // Act
        var rawResponse = pdlMock.graphQLRequest(null, null, null, null, request);

        // Assert
        var response = (HentPersonQueryResponse) konverterTilGraphResponse(rawResponse);
        var person = response.hentPerson();
        assertThat(person).isNotNull();
        assertThat(person.getNavn().get(0).getFornavn()).isEqualTo(søker.getFornavn().toUpperCase());

    }
    @Test
    public void hent_identer() throws JsonProcessingException {


    }

    // Hjelpemetode som oversetter resultat (LinkedHashMap) til objektgraf (GraphQLResult). Forenkler testing.
    private <T extends GraphQLResult> T konverterTilGraphResponse(Map<String, Object> response) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(response);
        var graphResponse = objectReaderHentPersonResponse.readValue(json);
        return (T) graphResponse;
    }

    private String byggProjectionPersonResponse() {
        var projeksjon = new PersonResponseProjection()
                .foedsel(new FoedselResponseProjection()
                        .foedselsdato())
                .navn(new NavnResponseProjection()
                        .fornavn()
                        .mellomnavn()
                        .etternavn()
                        .forkortetNavn())
                .statsborgerskap(new StatsborgerskapResponseProjection()
                        .land())
                .kjoenn(new KjoennResponseProjection()
                        .kjoenn())
                .bostedsadresse(new BostedsadresseResponseProjection()
                .angittFlyttedato()
                .gyldigFraOgMed()
                .gyldigTilOgMed()
                .vegadresse(new VegadresseResponseProjection()
                        .adressenavn()
                        .husnummer()
                        .husbokstav()
                        .postnummer()))
                .geografiskTilknytning(new GeografiskTilknytningResponseProjection()
                        .gtType()
                        .gtLand())
                .folkeregisterpersonstatus(new FolkeregisterpersonstatusResponseProjection()
                        .status())
                .familierelasjoner(new FamilierelasjonResponseProjection()
                        .relatertPersonsIdent()
                        .relatertPersonsRolle());
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
