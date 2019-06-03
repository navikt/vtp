package no.nav.foreldrepenger.autotest.foreldrepenger.engangsstonad;

import java.time.LocalDate;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.base.EngangsstonadTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvInnsynBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.autotest.util.AllureHelper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Execution(ExecutionMode.CONCURRENT)
@Tag("fpsak")
@Tag("engangsstonad")
public class Innsyn extends EngangsstonadTestBase {

    @Test
    @DisplayName("Behandle innsyn for mor - godkjent")
    @Description("Behandle innsyn for mor - godkjent happy case")
    public void behandleInnsynMorGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        saksbehandler.oprettBehandlingInnsyn(null);
        saksbehandler.velgDokumentInnsynBehandling();

        saksbehandler.hentAksjonspunktbekreftelse(VurderingAvInnsynBekreftelse.class)
                .setMottattDato(LocalDate.now())
                .setInnsynResultatType(saksbehandler.kodeverk.InnsynResultatType.getKode("INNV"))
                .skalSetteSakPåVent(false)
                .setBegrunnelse("Test");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderingAvInnsynBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        AllureHelper.debugLoggBehandlingsliste(saksbehandler.behandlinger);
        AllureHelper.debugLoggHistorikkinnslag(saksbehandler.getHistorikkInnslag());
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.BREV_SENDT);
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "INNSYN_INNVILGET", "Behandlingstatus");
        verifiser(saksbehandler.harHistorikkinnslag(HistorikkInnslag.BREV_BESTILT), "Brev er ikke bestilt etter innsyn er godkjent");
        verifiser(saksbehandler.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT), "Brev er ikke sendt etter innsyn er godkjent");
    }

    @Test
    @DisplayName("Behandle innsyn for mor - avvist")
    @Description("Behandle innsyn for mor - avvist ved vurdering")
    public void behandleInnsynMorAvvist() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        saksbehandler.oprettBehandlingInnsyn(null);
        saksbehandler.velgDokumentInnsynBehandling();

        saksbehandler.hentAksjonspunktbekreftelse(VurderingAvInnsynBekreftelse.class)
                .setMottattDato(LocalDate.now())
                .setInnsynResultatType(saksbehandler.kodeverk.InnsynResultatType.getKode("AVVIST"))
                .setBegrunnelse("Test");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderingAvInnsynBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.BREV_SENDT);
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "INNSYN_AVVIST", "Behandlingstatus");
        verifiser(saksbehandler.harHistorikkinnslag(HistorikkInnslag.BREV_BESTILT), "Brev er ikke bestilt etter innsyn er godkjent");
        verifiser(saksbehandler.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT), "Brev er ikke sendt etter innsyn er godkjent");
    }
    @Disabled //Disabled til Kafka støtte for brev er i VTP
    @Test
    @DisplayName("Behandle innsyn for far - avvist")
    @Description("Behandle innsyn for far - avvist ved vurdering")
    public void behandleInnsynFarAvvist() throws Exception {
        TestscenarioDto testscenario = opprettScenario("61");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.terminFarEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        saksbehandler.oprettBehandlingInnsyn(null);
        saksbehandler.velgDokumentInnsynBehandling();

        saksbehandler.hentAksjonspunktbekreftelse(VurderingAvInnsynBekreftelse.class)
                .setMottattDato(LocalDate.now())
                .setInnsynResultatType(saksbehandler.kodeverk.InnsynResultatType.getKode("AVVIST"))
                .setBegrunnelse("Test");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderingAvInnsynBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.BREV_SENDT);
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "INNSYN_AVVIST", "Behandlingstatus");
        verifiser(saksbehandler.harHistorikkinnslag(HistorikkInnslag.BREV_BESTILT), "Brev er ikke bestilt etter innsyn er godkjent");
        verifiser(saksbehandler.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT), "Brev er ikke sendt etter innsyn er godkjent");
    }
}
