package no.nav.foreldrepenger.autotest.foreldrepenger.engangsstonad;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.base.EngangsstonadTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvForeldreansvarAndreLedd;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvOmsorgsvilkoret;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaOmsorgOgForeldreansvarBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Execution(ExecutionMode.CONCURRENT)
@Tag("fpsak")
@Tag("engangsstonad")
public class Omsorgsovertakelse extends EngangsstonadTestBase {

    @Test
    @DisplayName("Mor søker Omsorgsovertakelse - godkjent")
    @Description("Mor søker Omsorgsovertakelse - godkjent happy case")
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
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingstatus");
        beslutter.ventTilHistorikkinnslag(HistorikkInnslag.BREV_SENDT);
    }

    @Test
    @DisplayName("Mor søker Omsorgsovertakelse - avvist")
    @Description("Mor søker Omsorgsovertakelse - avvist fordi mor ikke er død")
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
                .bekreftAvvist(saksbehandler.kodeverk.Avslagsårsak.get("FP_VK_5").getKode("1009" /* Mor ikke død */));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderingAvOmsorgsvilkoret.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_OMSORGSVILKÅRET));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

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
    @Description("Far søker Omsorgsovertakelse - får godkjent aksjonspunkt og blir invilget")
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
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingstatus");
        beslutter.ventTilHistorikkinnslag(HistorikkInnslag.BREV_SENDT);
    }
    
    @Test
    @DisplayName("Far søker Foreldreansvar 2. ledd - godkjent")
    @Description("Far søker Foreldreansvar 2. ledd - får godkjent aksjonspunkt og blir invilget")
    public void farSøkerForeldreansvarGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("61");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.omsorgsovertakelseFarEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);


        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaOmsorgOgForeldreansvarBekreftelse.class)
                .setVilkårType(saksbehandler.kodeverk.OmsorgsovertakelseVilkårType.getKode("FP_VK_8"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaOmsorgOgForeldreansvarBekreftelse.class);

        
        saksbehandler.hentAksjonspunktbekreftelse(VurderingAvForeldreansvarAndreLedd.class)
                .bekreftGodkjent();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderingAvForeldreansvarAndreLedd.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_FORELDREANSVARSVILKÅRET_2_LEDD));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingstatus");
        beslutter.ventTilHistorikkinnslag(HistorikkInnslag.BREV_SENDT);
    }
}
