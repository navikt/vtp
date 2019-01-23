package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("smoke")
@Tag("foreldrepenger")
public class Termin extends ForeldrepengerTestBase{

    @Test
    @DisplayName("Mor søker med ett arbeidsforhold")
    @Description("Mor søkner med ett arbeidsforhold. Forventer at vedtak blir fattet og brev blir sendt")
    public void MorSøkerMedEttArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        LocalDate termindato = LocalDate.now().plusWeeks(3);
        LocalDate startDatoForeldrepenger = termindato.minusWeeks(3);
        
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), termindato);
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        
        saksbehandler.hentFagsak(saksnummer);
        debugListUtBehandling(saksbehandler.valgtBehandling);
        saksbehandler.ventTilHistorikkinnslag("Vedtak fattet");
        saksbehandler.ventTilHistorikkinnslag("Brev sendt");
        //TODO (OL): Byttet verifisering på historikkinnslag til asynk venting. Feilet med at behandlingen ikke hadde historikkinnslag
        //verifiser(saksbehandler.harHistorikkinnslag("Vedtak fattet"), "behandling har ikke historikkinslag 'Vedtak fattet'");
        //verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"), "behandling har ikke historikkinslag 'Brev sendt'");
    }
    
    @Test
    @DisplayName("Mor søker med ett arbeidsforhold. Inntektmelding innsendt før søknad")
    @Description("Mor med ett arbeidsforhold sender inn inntektsmelding før søknad. Forventer at vedtak bli fattet og brev blir sendt")
    public void MorSøkerMedEttArbeidsforholdInntektsmeldingFørSøknad() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        LocalDate termindato = LocalDate.now().plusWeeks(3);
        LocalDate startDatoForeldrepenger = termindato.minusWeeks(3);
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        Long saksnummer = fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario);
        
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), termindato);
        fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER, saksnummer);


        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);

        //TODO (OL): Flyttet til ventende sjekk - OK?
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag("Vedtak fattet");
        saksbehandler.ventTilHistorikkinnslag("Brev sendt");
        debugListUtBehandling(saksbehandler.valgtBehandling);
        //verifiser(saksbehandler.harHistorikkinnslag("Vedtak fattet"), "behandling har ikke historikkinslag 'Vedtak fattet'");
        //verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"), "behandling har ikke historikkinslag 'Brev sendt'");
    }
    
    @Tag("pending")
    @Test
    @Disabled
    public void MorSøkerMedEttArbeidsforholdOvergangFraYtelse() throws Exception {
        //TODO
    }
}
