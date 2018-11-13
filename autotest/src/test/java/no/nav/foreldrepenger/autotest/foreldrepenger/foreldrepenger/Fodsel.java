package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("foreldrepenger")
public class Fodsel extends ForeldrepengerTestBase{

    @Test
    public void MorSøkerFodselMedArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, LocalDate.now().minusMonths(1));
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldinger.get(0);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        fordel.sendInnInntektsmelding(inntektsmeldingsBuilder, testscenario, saksnummer);
    }
}
