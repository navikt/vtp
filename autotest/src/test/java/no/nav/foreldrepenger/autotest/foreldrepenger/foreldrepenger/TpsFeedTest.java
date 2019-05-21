package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.base.ForeldrepengerTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.DødshendelseDto;
import no.nav.foreldrepenger.fpmock2.kontrakter.FødselshendelseDto;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

public class TpsFeedTest extends ForeldrepengerTestBase {


    /**
     * Obs: Sekvensnummer må synkes med fp-abonnent ,Tabell:Input_feed, column:Next-url
     * Og med fp-sak tabell: mottat_hendese som lagrer hendelser med type+SEKVENSENUMMER. eks FØDSEL2 .
     * Dersom denne hendelsen finnes fra før ignoreres den av fp-sak.
     * <p>
     * <p>
     * GJELDER ALLE TESTER
     */
    @Test
    @Disabled
    public void mottaFøselshendelse() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        LocalDate termindato = LocalDate.now().plusWeeks(3);
        LocalDate startDatoForeldrepenger = termindato.minusWeeks(5);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), termindato);

        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);


        String fødselsnummerMor = testscenario.getPersonopplysninger().getSøkerIdent();
        String fødselsnummerBarn = "12345671234";
        FødselshendelseDto fødselshendelseDto = new FødselshendelseDto();
        fødselshendelseDto.setFnrMor(fødselsnummerMor);
        fødselshendelseDto.setFnrBarn(fødselsnummerBarn);
        fødselshendelseDto.setFødselsdato(termindato.minusWeeks(4));

        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        fordel.opprettTpsHendelse(fødselshendelseDto);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilAntallHistorikkinnslag(HistorikkInnslag.BEH_OPPDATERT_NYE_OPPL, 120, 2);
        verifiserLikhet(saksbehandler.harAntallHistorikkinnslag(HistorikkInnslag.BEH_OPPDATERT_NYE_OPPL), 2);
    }

    @Test
    @Disabled
    public void mottaDødhendelse() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        LocalDate termindato = LocalDate.now().plusWeeks(3);
        LocalDate startDatoForeldrepenger = termindato.minusWeeks(5);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), termindato);

        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);


        String fødselsnummerMor = testscenario.getPersonopplysninger().getSøkerIdent();
        DødshendelseDto dødshendelseDto = new DødshendelseDto();
        dødshendelseDto.setFnr(fødselsnummerMor);
        dødshendelseDto.setDoedsdato(LocalDate.now());

        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        fordel.opprettTpsHendelse(dødshendelseDto);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilAntallHistorikkinnslag(HistorikkInnslag.BEH_OPPDATERT_NYE_OPPL, 120, 2);
        verifiserLikhet(saksbehandler.harAntallHistorikkinnslag(HistorikkInnslag.BEH_OPPDATERT_NYE_OPPL), 2);
    }
}
