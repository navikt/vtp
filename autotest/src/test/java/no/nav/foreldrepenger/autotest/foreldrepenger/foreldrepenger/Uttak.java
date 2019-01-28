package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

@Tag("regresjon")
@Tag("foreldrepenger")
public class Uttak extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Testcase for koblet sak")
    @Description("Mor og far søker etter fødsel med ett arbeidsforhold hver. 100% dekningsgrad.")
    public void testcase_morOgFarSøkerEtterFødsel_kantTilKantsøknad() throws Exception {
        TestscenarioDto testscenario = opprettScenario("82");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

        String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String farFnr = testscenario.getPersonopplysninger().getAnnenpartIdent();
        LocalDate fpStartDatoFar = fødselsdato.plusWeeks(10).plusDays(1);

        ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørId,morAktørId,
                fødselsdato, LocalDate.now(), FordelingErketyper.fordelingFarHappycaseKobletMedMorHappycase(fødselsdato));
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farFnr, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farFnr, fpStartDatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farFnr, saksnummerFar);
        saksbehandler.hentFagsak(saksnummerFar);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

    }
}
