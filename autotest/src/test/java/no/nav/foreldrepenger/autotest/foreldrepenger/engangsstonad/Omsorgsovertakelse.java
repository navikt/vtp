package no.nav.foreldrepenger.autotest.foreldrepenger.engangsstonad;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvOmsorgsvilkoret;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaOmsorgOgForeldreansvarBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("smoke")
@Tag("engangsstonad")
public class Omsorgsovertakelse extends EngangsstonadTestBase{

    @Test
    @DisplayName("Mor søker Omsorgsovertakelse - godkjent")
    public void MorSøkerOmsorgsovertakelseGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.omsorgsovertakelseMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaOmsorgOgForeldreansvarBekreftelse.class)
            .setVilkårType(saksbehandler.kodeverk.OmsorgsovertakelseVilkårType.getKode("FP_VK_5"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaOmsorgOgForeldreansvarBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderingAvOmsorgsvilkoret.class)
            .bekreftGodkjent();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderingAvOmsorgsvilkoret.class);
    
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_OMSORGSVILKÅRET));
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();
        
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingstatus");
        beslutter.ventTilHistorikkinnslag("Brev sendt");
    }
    
    @Test
    @DisplayName("Mor søker Omsorgsovertakelse - avvist")
    public void morSøkerOmsorgsovertakelseAvvist() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.omsorgsovertakelseMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaOmsorgOgForeldreansvarBekreftelse.class)
            .setVilkårType(saksbehandler.kodeverk.OmsorgsovertakelseVilkårType.getKode("FP_VK_5"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaOmsorgOgForeldreansvarBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderingAvOmsorgsvilkoret.class)
            .bekreftAvvist(saksbehandler.kodeverk.Avslagsårsak.get("FP_VK_5").getKode("1009"));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderingAvOmsorgsvilkoret.class);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_OMSORGSVILKÅRET));
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();
        
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "AVSLÅTT", "Behandlingstatus");
    }
    
    @Test
    @Disabled("TODO hvorfor")
    public void behenadleOmsorgsovertakelseMorOverstyrt() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.omsorgsovertakelseMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaOmsorgOgForeldreansvarBekreftelse.class)
            .setVilkårType(saksbehandler.kodeverk.OmsorgsovertakelseVilkårType.getKode("FP_VK_5"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaOmsorgOgForeldreansvarBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderingAvOmsorgsvilkoret.class)
            .bekreftAvvist(saksbehandler.kodeverk.Avslagsårsak.get("FP_VK_33").getKode("1018"));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderingAvOmsorgsvilkoret.class);
        
        //TODO bør gå til beslutter
    }

    @Test
    @DisplayName("Far søker Omsorgsovertakelse - godkjent")
    public void farSøkerOmsorgsovertakelseGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("61");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.omsorgsovertakelseFarEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaOmsorgOgForeldreansvarBekreftelse.class)
            .setVilkårType(saksbehandler.kodeverk.OmsorgsovertakelseVilkårType.getKode("FP_VK_5"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaOmsorgOgForeldreansvarBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderingAvOmsorgsvilkoret.class)
            .bekreftGodkjent();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderingAvOmsorgsvilkoret.class);
    
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_OMSORGSVILKÅRET));
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();
        
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingstatus");
        beslutter.ventTilHistorikkinnslag("Brev sendt");
    }
}
