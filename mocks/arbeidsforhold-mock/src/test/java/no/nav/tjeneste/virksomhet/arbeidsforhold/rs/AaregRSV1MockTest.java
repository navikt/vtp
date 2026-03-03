package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock.ARBEIDSFORHOLDTYPE;
import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock.HEADER_NAV_PERSONIDENT;
import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock.QPRM_FOM;
import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock.QPRM_TOM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Map;

import no.nav.vtp.Person;
import no.nav.vtp.PersonBuilder;
import no.nav.vtp.PersonRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.UriInfo;

@ExtendWith(MockitoExtension.class)
class AaregRSV1MockTest {

    private static AaregRSV1Mock aaregRSV1Mock;
    private static final PersonRepository personRepository = new PersonRepository();
    private static final Person person = PersonBuilder.lagAnnenPart();

    @Mock
    private HttpHeaders headers;

    @Mock
    private UriInfo uriInfo;

    @BeforeAll
    static void setup() {
        personRepository.leggTilPerson(person);
        aaregRSV1Mock = new AaregRSV1Mock(personRepository);
    }

    @Test
    void hentFremtidigArbeidsforholdTest() {
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(person.personopplysninger().identifikator().ident());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, person.arbeidsforhold().getFirst().ansettelsesperiodeFom().format(ISO_LOCAL_DATE))));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(2);
    }

    @Test
    void hentFremtidigArbeidsforholdTest2() {
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(person.personopplysninger().identifikator().ident());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, person.arbeidsforhold().get(1).ansettelsesperiodeTom().plusDays(1).format(ISO_LOCAL_DATE))));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(1);
    }

    @Test
    void hentArbeidsforholdMedTomOgFomTest() {
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(person.personopplysninger().identifikator().ident());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, person.arbeidsforhold().getFirst().ansettelsesperiodeFom().format(ISO_LOCAL_DATE),
                QPRM_TOM, person.arbeidsforhold().get(1).ansettelsesperiodeTom().format(ISO_LOCAL_DATE))));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(2);
    }

    @Test
    void hentArbeidsforholdMedTomOgFomTest2() {
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(person.personopplysninger().identifikator().ident());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, person.arbeidsforhold().get(1).ansettelsesperiodeTom().plusDays(1).format(ISO_LOCAL_DATE),
                QPRM_TOM, person.arbeidsforhold().get(1).ansettelsesperiodeTom().format(ISO_LOCAL_DATE))));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(1);
    }

    @Test
    void hentArbeidsforholdMedArbeidsforholdtype() {
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(person.personopplysninger().identifikator().ident());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, person.arbeidsforhold().getFirst().ansettelsesperiodeFom().format(ISO_LOCAL_DATE),
                ARBEIDSFORHOLDTYPE, "frilanserOppdragstakerHonorarPersonerMm")));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(1);
    }
}
