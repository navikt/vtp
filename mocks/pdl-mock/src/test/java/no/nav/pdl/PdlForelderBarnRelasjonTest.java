package no.nav.pdl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;

import no.nav.pdl.graphql.GraphQLRequest;
import no.nav.vtp.PersonBuilder;
import no.nav.vtp.person.PersonRepository;

class PdlForelderBarnRelasjonTest extends PdlTestBase {

        private final PdlMock pdlMock = new PdlMock();
        private static final PersonResponseProjection projeksjon = getPersonForelderBarnRelasjonResponseProjection();

        private final ObjectReader hentPersonReader = JSON_MAPPER.readerFor(HentPersonQueryResponse.class);

        @Test
        void hent_forelderBarnRelasjon_søker_test() throws JsonProcessingException {
            var scenario = PersonBuilder.lagPersoner();
            PersonRepository.leggTilPersoner(scenario.allePersoner());
            var query = String.format("query($ident: ID!){ hentPerson(ident: $ident) %s }", projeksjon);
            var requestSøker = GraphQLRequest.builder().withQuery(query).withVariables(Map.of("ident", scenario.søkerIdent())).build();

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
    void hent_forelderBarnRelasjon_annenpart_test() throws JsonProcessingException {
        var scenario = PersonBuilder.lagPersoner();
        PersonRepository.leggTilPersoner(scenario.allePersoner());
        var query = String.format("query($ident: ID!){ hentPerson(ident: $ident) %s }", projeksjon);
        var requestAnnenpart = GraphQLRequest.builder().withQuery(query).withVariables(Map.of("ident", scenario.annenPartIdent())).build();

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
    void hent_forelderBarnRelasjon_barn_test() throws JsonProcessingException {
        var scenario = PersonBuilder.lagPersoner();
        PersonRepository.leggTilPersoner(scenario.allePersoner());
        var barnIdent = scenario.barn1Ident();
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
