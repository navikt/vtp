package no.nav.pdl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
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

class PdlForelderBarnRelasjonTest extends PdlTestBase {

        private static final String SCENARIOID = "1";

        private static Testscenario testscenario;
        private static PdlMock pdlMock;
        private static PersonResponseProjection projeksjon;

        private final ObjectReader hentPersonReader = objectMapper.readerFor(HentPersonQueryResponse.class);

        @BeforeAll
        public static void setup() throws IOException {
            var testScenarioRepository = new DelegatingTestscenarioRepository(
                    TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
            var testscenarioHenter = TestscenarioHenter.getInstance();
            var testscenarioObjekt = testscenarioHenter.hentScenario(SCENARIOID);
            var testscenarioJson = testscenarioHenter.toJson(testscenarioObjekt);
            testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());
            pdlMock = new PdlMock(testScenarioRepository);
            projeksjon = getPersonForelderBarnRelasjonResponseProjection();
        }

        @Test
        public void hent_forelderBarnRelasjon_søker_test() throws JsonProcessingException {
            var søker = testscenario.getPersonopplysninger().getSøker();
            var query = String.format("query($ident: ID!){ hentPerson(ident: $ident) %s }", projeksjon);
            var requestSøker = GraphQLRequest.builder().withQuery(query).withVariables(Map.of("ident", søker.getIdent())).build();

            // Act
            var rawResponseSøker = pdlMock.graphQLRequest(null, null, null, null, requestSøker);
            var responseSøker = (HentPersonQueryResponse) konverterTilGraphResponse(rawResponseSøker, hentPersonReader);
            var personSøker = responseSøker.hentPerson();

            // Assert
            assertThat(personSøker.getForelderBarnRelasjon()).hasSize(2);
            assertThat(personSøker.getForelderBarnRelasjon().get(0).getMinRolleForPerson()).isEqualTo(ForelderBarnRelasjonRolle.MOR);
            assertThat(personSøker.getForelderBarnRelasjon().get(0).getRelatertPersonsRolle()).isEqualTo(ForelderBarnRelasjonRolle.BARN);
            assertThat(personSøker.getForelderBarnRelasjon().get(1).getMinRolleForPerson()).isEqualTo(ForelderBarnRelasjonRolle.MOR);
            assertThat(personSøker.getForelderBarnRelasjon().get(1).getRelatertPersonsRolle()).isEqualTo(ForelderBarnRelasjonRolle.BARN);
        }

    @Test
    public void hent_forelderBarnRelasjon_annenpart_test() throws JsonProcessingException {
        var annenpart = testscenario.getPersonopplysninger().getAnnenPart();
        var query = String.format("query($ident: ID!){ hentPerson(ident: $ident) %s }", projeksjon);
        var requestAnnenpart = GraphQLRequest.builder().withQuery(query).withVariables(Map.of("ident", annenpart.getIdent())).build();

        // Act
        var rawResponseAnnenpart = pdlMock.graphQLRequest(null, null, null, null, requestAnnenpart);
        var responseAnnenpart = (HentPersonQueryResponse) konverterTilGraphResponse(rawResponseAnnenpart, hentPersonReader);
        var personAnnenpart = responseAnnenpart.hentPerson();

        // Assert
        assertThat(personAnnenpart.getForelderBarnRelasjon()).hasSize(2);
        assertThat(personAnnenpart.getForelderBarnRelasjon().get(0).getMinRolleForPerson()).isEqualTo(ForelderBarnRelasjonRolle.FAR);
        assertThat(personAnnenpart.getForelderBarnRelasjon().get(0).getRelatertPersonsRolle()).isEqualTo(ForelderBarnRelasjonRolle.BARN);
        assertThat(personAnnenpart.getForelderBarnRelasjon().get(1).getMinRolleForPerson()).isEqualTo(ForelderBarnRelasjonRolle.FAR);
        assertThat(personAnnenpart.getForelderBarnRelasjon().get(1).getRelatertPersonsRolle()).isEqualTo(ForelderBarnRelasjonRolle.BARN);
    }

    @Test
    public void hent_forelderBarnRelasjon_barn_test() throws JsonProcessingException {
        var barnIdent = testscenario.getPersonopplysninger().getIdenter().getIdent("${barn1}");
        var query = String.format("query($ident: ID!){ hentPerson(ident: $ident) %s }", projeksjon);
        var requestBarn = GraphQLRequest.builder().withQuery(query).withVariables(Map.of("ident", barnIdent)).build();

        // Act
        var rawResponseBarn = pdlMock.graphQLRequest(null, null, null, null, requestBarn);
        var responseBarn = (HentPersonQueryResponse) konverterTilGraphResponse(rawResponseBarn, hentPersonReader);
        var personBarn = responseBarn.hentPerson();

        // Assert
        assertThat(personBarn.getForelderBarnRelasjon()).hasSize(2);
        assertThat(personBarn.getForelderBarnRelasjon().get(0).getMinRolleForPerson()).isEqualTo(ForelderBarnRelasjonRolle.BARN);
        assertThat(personBarn.getForelderBarnRelasjon().get(0).getRelatertPersonsRolle()).isEqualTo(ForelderBarnRelasjonRolle.MOR);
        assertThat(personBarn.getForelderBarnRelasjon().get(1).getMinRolleForPerson()).isEqualTo(ForelderBarnRelasjonRolle.BARN);
        assertThat(personBarn.getForelderBarnRelasjon().get(1).getRelatertPersonsRolle()).isEqualTo(ForelderBarnRelasjonRolle.FAR);
    }


    private static PersonResponseProjection getPersonForelderBarnRelasjonResponseProjection() {
        return new PersonResponseProjection()
                .forelderBarnRelasjon(new ForelderBarnRelasjonResponseProjection()
                        .relatertPersonsIdent()
                        .relatertPersonsRolle()
                        .minRolleForPerson()
                )
                .bostedsadresse(
                        new PersonBostedsadresseParametrizedInput(false),
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
                );
    }
}
