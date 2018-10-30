package no.nav.foreldrepenger.autotest.engangsstonad;

import java.time.LocalDate;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvInnsynBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("engangsstonad")
public class Innsyn extends EngangsstonadTestBase{

    @Test
    public void behandleInnsynMorGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.opprettBehandling(saksbehandler.kodeverk.BehandlingType.getKode("BT-006"));
        saksbehandler.velgBehandling(saksbehandler.kodeverk.BehandlingType.getKode("BT-006"));
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderingAvInnsynBekreftelse.class)
            .setMottattDato(LocalDate.now())
            .setInnsynResultatType(saksbehandler.kodeverk.InnsynResultatType.getKode("INNV"))
            .skalSetteSakPåVent(false)
            .setBegrunnelse("Test");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderingAvInnsynBekreftelse.class);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "INNSYN_INNVILGET", "Behandlingstatus");
        verifiser(saksbehandler.harHistorikkinnslag("Brev bestilt"));
        verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"));
    }
    
    @Test
    public void behandleInnsynMorAvvist() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.opprettBehandling(saksbehandler.kodeverk.BehandlingType.getKode("BT-006"));
        saksbehandler.velgBehandling(saksbehandler.kodeverk.BehandlingType.getKode("BT-006"));
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderingAvInnsynBekreftelse.class)
            .setMottattDato(LocalDate.now())
            .setInnsynResultatType(saksbehandler.kodeverk.InnsynResultatType.getKode("AVVIST"))
            .setBegrunnelse("Test");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderingAvInnsynBekreftelse.class);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "INNSYN_AVVIST", "Behandlingstatus");
        verifiser(saksbehandler.harHistorikkinnslag("Brev bestilt"));
        verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"));
    }
    
    @Test
    public void behandleInnsynFarAvvist() throws Exception {
        TestscenarioDto testscenario = opprettScenario("61");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.terminFarEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.opprettBehandling(saksbehandler.kodeverk.BehandlingType.getKode("BT-006"));
        saksbehandler.velgBehandling(saksbehandler.kodeverk.BehandlingType.getKode("BT-006"));
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderingAvInnsynBekreftelse.class)
            .setMottattDato(LocalDate.now())
            .setInnsynResultatType(saksbehandler.kodeverk.InnsynResultatType.getKode("AVVIST"))
            .setBegrunnelse("Test");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderingAvInnsynBekreftelse.class);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "INNSYN_AVVIST", "Behandlingstatus");
        verifiser(saksbehandler.harHistorikkinnslag("Brev bestilt"));
        verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"));
    }
}
