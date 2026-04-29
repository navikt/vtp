package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock.ARBEIDSFORHOLDTYPE;
import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock.HEADER_NAV_PERSONIDENT;
import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock.QPRM_FOM;
import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock.QPRM_TOM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.UriInfo;
import no.nav.vtp.PersonBuilder;
import no.nav.vtp.person.PersonRepository;

@ExtendWith(MockitoExtension.class)
class AaregRSV1MockTest {

    private final AaregRSV1Mock aaregRSV1Mock = new AaregRSV1Mock();

    @Mock
    private HttpHeaders headers;

    @Mock
    private UriInfo uriInfo;

    @Test
    void hentFremtidigArbeidsforholdTest() {
        var person = PersonBuilder.lagAnnenPart();
        PersonRepository.leggTilPerson(person);
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(person.personopplysninger().identifikator().value());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, person.arbeidsforhold().getFirst().ansettelsesperiodeFom().format(ISO_LOCAL_DATE))));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(2);
    }

    @Test
    void hentFremtidigArbeidsforholdTest2() {
        var person = PersonBuilder.lagAnnenPart();
        PersonRepository.leggTilPerson(person);
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(person.personopplysninger().identifikator().value());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, person.arbeidsforhold().get(1).ansettelsesperiodeTom().plusDays(1).format(ISO_LOCAL_DATE))));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(1);
    }

    @Test
    void hentArbeidsforholdMedTomOgFomTest() {
        var person = PersonBuilder.lagAnnenPart();
        PersonRepository.leggTilPerson(person);
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(person.personopplysninger().identifikator().value());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, person.arbeidsforhold().getFirst().ansettelsesperiodeFom().format(ISO_LOCAL_DATE),
                QPRM_TOM, person.arbeidsforhold().get(1).ansettelsesperiodeTom().format(ISO_LOCAL_DATE))));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(2);
    }

    @Test
    void hentArbeidsforholdMedTomOgFomTest2() {
        var person = PersonBuilder.lagAnnenPart();
        PersonRepository.leggTilPerson(person);
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(person.personopplysninger().identifikator().value());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, person.arbeidsforhold().get(1).ansettelsesperiodeTom().plusDays(1).format(ISO_LOCAL_DATE),
                QPRM_TOM, person.arbeidsforhold().get(1).ansettelsesperiodeTom().format(ISO_LOCAL_DATE))));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(1);
    }

    @Test
    void hentArbeidsforholdMedArbeidsforholdtype() {
        var person = PersonBuilder.lagAnnenPart();
        PersonRepository.leggTilPerson(person);
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(person.personopplysninger().identifikator().value());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, person.arbeidsforhold().getFirst().ansettelsesperiodeFom().format(ISO_LOCAL_DATE),
                ARBEIDSFORHOLDTYPE, "frilanserOppdragstakerHonorarPersonerMm")));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(1);
    }
}
