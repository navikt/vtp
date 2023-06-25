package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock.ARBEIDSFORHOLDTYPE;
import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock.HEADER_NAV_PERSONIDENT;
import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock.QPRM_FOM;
import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock.QPRM_TOM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.UriInfo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import no.nav.foreldrepenger.vtp.testmodell.TestscenarioHenter;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

@ExtendWith(MockitoExtension.class)
class AaregRSV1MockTest {

    private static final String SCENARIOID = "1";

    private static AaregRSV1Mock aaregRSV1Mock;
    private static Testscenario testscenario;

    @Mock
    private HttpHeaders headers;

    @Mock
    private UriInfo uriInfo;


    @BeforeAll
    public static void setup() {
        var testScenarioRepository = new DelegatingTestscenarioRepository(
                TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        var testscenarioHenter = TestscenarioHenter.getInstance();
        var testscenarioObjekt = testscenarioHenter.hentScenario(SCENARIOID);
        var testscenarioJson = testscenarioHenter.toJson(testscenarioObjekt);
        testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());
        aaregRSV1Mock = new AaregRSV1Mock(testScenarioRepository);
    }

    @Test
    void hentFremtidigArbeidsforholdTest() {
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(testscenario.getPersonopplysninger().getAnnenPart().getIdent());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, testscenario.getAnnenpartInntektYtelse().arbeidsforholdModell().arbeidsforhold().get(0)
                        .ansettelsesperiodeFom().format(ISO_LOCAL_DATE))));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(2);
    }

    @Test
    void hentFremtidigArbeidsforholdTest2() {
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(testscenario.getPersonopplysninger().getAnnenPart().getIdent());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, testscenario.getAnnenpartInntektYtelse().arbeidsforholdModell().arbeidsforhold().get(1)
                        .ansettelsesperiodeTom().plusDays(1).format(ISO_LOCAL_DATE))));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(1);
    }


    @Test
    void hentArbeidsforholdMedTomOgFomTest() {
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(testscenario.getPersonopplysninger().getAnnenPart().getIdent());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, testscenario.getAnnenpartInntektYtelse().arbeidsforholdModell().arbeidsforhold().get(1).ansettelsesperiodeFom().format(ISO_LOCAL_DATE),
                QPRM_TOM, testscenario.getAnnenpartInntektYtelse().arbeidsforholdModell().arbeidsforhold().get(1).ansettelsesperiodeTom().format(ISO_LOCAL_DATE))));
        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);

        assertThat(arbeidsforhold).hasSize(2);
    }


    @Test
    void hentArbeidsforholdMedTomOgFomTest2() {
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(testscenario.getPersonopplysninger().getAnnenPart().getIdent());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, testscenario.getAnnenpartInntektYtelse().arbeidsforholdModell().arbeidsforhold().get(1).ansettelsesperiodeTom().plusDays(1).format(ISO_LOCAL_DATE),
                QPRM_TOM, testscenario.getAnnenpartInntektYtelse().arbeidsforholdModell().arbeidsforhold().get(1).ansettelsesperiodeTom().format(ISO_LOCAL_DATE))));
        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);

        assertThat(arbeidsforhold).hasSize(1);
    }

    @Test
    void hentArbeidsforholdMedArbeidsforholdtype() {
        when(headers.getHeaderString(HEADER_NAV_PERSONIDENT))
                .thenReturn(testscenario.getPersonopplysninger().getAnnenPart().getIdent());
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>(Map.of(
                QPRM_FOM, testscenario.getAnnenpartInntektYtelse().arbeidsforholdModell().arbeidsforhold().get(0)
                        .ansettelsesperiodeFom().format(ISO_LOCAL_DATE),
                ARBEIDSFORHOLDTYPE, "frilanserOppdragstakerHonorarPersonerMm")));

        var arbeidsforhold = aaregRSV1Mock.hentArbeidsforholdFor(headers, uriInfo);
        assertThat(arbeidsforhold).hasSize(1);
    }
}
