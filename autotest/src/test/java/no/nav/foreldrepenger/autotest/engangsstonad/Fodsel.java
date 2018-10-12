package no.nav.foreldrepenger.autotest.engangsstonad;

import java.time.LocalDate;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderManglendeFodselBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarBrukerHarGyldigPeriodeBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("smoke")
public class Fodsel extends EngangsstonadTestBase{

    @Tag("utvikling")
    @Test
    public void behandleFødselMorGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonForeligger(1, LocalDate.now().minusMonths(1));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class)
            .setVurdering(hentKodeverk().MedlemskapManuellVurderingType.getKode("MEDLEM"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        beslutter.erLoggetInnMedRolle("Beslutter");
        beslutter.hentFagsak(saksnummer);
        
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.ikkeVentPåStatus = true;
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingstatus");
    }
    
    @Test
    public void behandleFødselMorAvvist() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonIkkeForeligger();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        beslutter.erLoggetInnMedRolle("beslutter");
        beslutter.hentFagsak(saksnummer);
        
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.ikkeVentPåStatus = true;
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        
        //verifiser at statusen er avvist
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "AVSLÅTT", "Behandlingstatus");
    }
    
    public void behandleFødselFarGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("60");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonForeligger(1, LocalDate.now().minusMonths(1));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        beslutter.erLoggetInnMedRolle("beslutter");
        beslutter.hentFagsak(saksnummer);
        
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.ikkeVentPåStatus = true;
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "AVSLÅTT", "Behandlingstatus");
    }
    
    public void behandleFødselMorOverstyrFødsel() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonForeligger(1, LocalDate.now().minusMonths(1));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class)
            .setVurdering(hentKodeverk().MedlemskapManuellVurderingType.getKode("MEDLEM"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class);
        
        overstyrer.erLoggetInnMedRolle("overstyrer");
        overstyrer.overstyr(VurderManglendeFodselBekreftelse.class);
        
        throw new RuntimeException("");
    }
    
    
    @Test
    public void behandleFødselMorFlereBarn() throws Exception {
        TestscenarioDto testscenario = opprettScenario("53");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonadFlereBarn(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonForeligger(1, LocalDate.now().minusMonths(1));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class)
            .setVurdering(hentKodeverk().MedlemskapManuellVurderingType.getKode("MEDLEM"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        beslutter.erLoggetInnMedRolle("beslutter");
        beslutter.hentFagsak(saksnummer);
        
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.ikkeVentPåStatus = true;
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingstatus");
        verifiserLikhet(beslutter.valgtBehandling.beregningResultatEngangsstonad.getBeregnetTilkjentYtelse(), 1);
    }
    
    @Test
    public void behandleFødselMorMedVerge() throws Exception {
        TestscenarioDto testscenario = opprettScenario("54");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
    }
    
    
}
