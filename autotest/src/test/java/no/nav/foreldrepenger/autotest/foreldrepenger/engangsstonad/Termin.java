package no.nav.foreldrepenger.autotest.foreldrepenger.engangsstonad;

import java.time.LocalDate;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarBrukerHarGyldigPeriodeBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTerminBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrAdopsjonsvilkaaret;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrFodselsvilkaaret;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("smoke")
@Tag("engangsstonad")
public class Termin extends EngangsstonadTestBase{

    @Test
    public void behandleTerminMorGodkjent()  throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.terminMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaTerminBekreftelse.class)
            .antallBarn(1)
            .utstedtdato(LocalDate.now().minusMonths(1))
            .setTermindato(LocalDate.now().plusMonths(1));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTerminBekreftelse.class);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_TERMINBEKREFTELSE));
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();
        
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingstatus");
        
    }
    
    
    @Test
    public void behandleTerminMorManglerDokumentasjon() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.terminMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.sendBrev("INNHEN", "Søker", "Trenger utstedt dato");
        saksbehandler.settBehandlingPåVent(LocalDate.now().plusDays(2), saksbehandler.kodeverk.Venteårsak.getKode("AVV_DOK"));
        
        //Todo mock brev
        //verifiser(saksbehandler.harDokument(""), "Behandling har ikke dokument");
        
        saksbehandler.ventTilHistorikkinnslag("Brev bestilt");
        saksbehandler.ventTilHistorikkinnslag("Brev sendt");
    }
    
    @Test
    public void behandleTerminMorOvertyrt()  throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.terminMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaTerminBekreftelse.class)
            .antallBarn(1)
            .utstedtdato(LocalDate.now().minusMonths(1))
            .setTermindato(LocalDate.now().plusMonths(1));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTerminBekreftelse.class);
        
        overstyrer.erLoggetInnMedRolle(Rolle.OVERSTYRER);
        overstyrer.hentFagsak(saksnummer);
        
        OverstyrFodselsvilkaaret overstyr = new OverstyrFodselsvilkaaret(overstyrer.valgtFagsak, overstyrer.valgtBehandling);
        overstyr.avvis(overstyrer.kodeverk.Avslagsårsak.get("FP_VK_1").getKode("Søker er far"));
        overstyr.setBegrunnelse("avvist");
        overstyrer.overstyr(overstyr);
        
        verifiserLikhet(overstyrer.valgtBehandling.behandlingsresultat.toString(), "AVSLÅTT", "Behandlingstatus");
        overstyrer.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.OVERSTYRING_AV_FØDSELSVILKÅRET));
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();
        
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "AVSLÅTT", "Behandlingstatus");
    }
    
    public void behandleTerminMorUtenTerminbekreftelse()  throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
    }
    
    @Test
    public void behandleTerminFarGodkjent()  throws Exception {
        TestscenarioDto testscenario = opprettScenario("61");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.terminFarEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaTerminBekreftelse.class)
            .antallBarn(1)
            .utstedtdato(LocalDate.now().minusMonths(1))
            .setTermindato(LocalDate.now().plusMonths(1));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTerminBekreftelse.class);
        
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "AVSLÅTT", "Behandlingstatus");
    }
    
    @Test
    public void settBehandlingPåVentOgGjenoppta() throws Exception {
        //Opprett scenario og søknad
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.terminMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        //Send inn søknad
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        
        saksbehandler.settBehandlingPåVent(LocalDate.now(), saksbehandler.kodeverk.Venteårsak.getKode("AVV_DOK"));
        verifiser(saksbehandler.valgtBehandling.erSattPåVent(), "Behandlingen er ikke satt på vent");
        
        saksbehandler.gjenopptaBehandling();
        verifiser(!saksbehandler.valgtBehandling.erSattPåVent(), "Behandlingen er satt på vent");
    }
    
}
