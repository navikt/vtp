package no.nav.foreldrepenger.autotest.foreldrepenger.engangsstonad;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.base.EngangsstonadTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VarselOmRevurderingBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderManglendeFodselBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarBrukerHarGyldigPeriodeBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaVergeBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrBeregning;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrFodselsvilkaaret;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("fpsak")
@Tag("engangsstonad")
public class Fodsel extends EngangsstonadTestBase {

    @Test
    @DisplayName("Mor søker fødsel - godkjent")
    @Description("Mor søker fødsel - godkjent happy case")
    public void morSøkerFødselGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        saksbehandler.ventTilAvsluttetBehandling();

        verifiserLikhet(saksbehandler.valgtBehandling.getBehandlingsresultat().toString(), "INNVILGET", "Behandlingstatus");
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.BREV_SENDT);
    }

    @Test
    @DisplayName("Mor søker fødsel - avvist")
    @Description("Mor søker fødsel - avvist fordi dokumentasjon mangler og barn er ikke registrert i tps")
    public void morSøkerFødselAvvist() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonIkkeForeligger();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        //verifiser at statusen er avvist
        verifiserLikhet(beslutter.valgtBehandling.getBehandlingsresultat().toString(), "AVSLÅTT", "Behandlingstatus");
    }

    @Test
    @DisplayName("Far søker registrert fødsel")
    @Description("Far søker registrert fødsel og blir avvist fordi far søker")
    public void farSøkerFødselRegistrert() throws Exception {
        TestscenarioDto testscenario = opprettScenario("60");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.ventTilAvsluttetBehandling();
        
        verifiserLikhet(saksbehandler.valgtBehandling.getBehandlingsresultat().toString(), "AVSLÅTT", "Behandlingstatus");
    }

    @Test
    @DisplayName("Mor søker fødsel overstyrt vilkår")
    @Description("Mor søker fødsel overstyrt vilkår adopsjon fra godkjent til avslått")
    public void morSøkerFødselOverstyrt() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonForeligger(1, LocalDate.now().minusMonths(1));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);
        
        verifiserLikhet(saksbehandler.valgtBehandling.getBehandlingsresultat().toString(), "INNVILGET", "behandlingsresultat");

        overstyrer.erLoggetInnMedRolle(Rolle.OVERSTYRER);
        overstyrer.hentFagsak(saksnummer);

        OverstyrFodselsvilkaaret overstyr = new OverstyrFodselsvilkaaret(overstyrer.valgtFagsak, overstyrer.valgtBehandling);
        overstyr.avvis(overstyrer.kodeverk.Avslagsårsak.get("FP_VK_1").getKode("1003" /*Søker er far */));
        overstyr.setBegrunnelse("avvist");
        overstyrer.overstyr(overstyr);

        verifiserLikhet(overstyrer.valgtBehandling.getBehandlingsresultat().toString(), "AVSLÅTT", "Behandlingstatus");
        overstyrer.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.OVERSTYRING_AV_FØDSELSVILKÅRET));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();
    }
    
    @Test
    @DisplayName("Mor søker fødsel - beregning overstyrt")
    @Description("Mor søker fødsel - beregning overstyrt fra ett beløp til 10 kroner")
    public void morSøkerFødselBeregningOverstyrt() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        verifiserLikhet(saksbehandler.valgtBehandling.getBehandlingsresultat().toString(), "INNVILGET", "Behandlingstatus");
        
        //Overstyr beregning
        overstyrer.erLoggetInnMedRolle(Rolle.OVERSTYRER);
        overstyrer.hentFagsak(saksnummer);
        overstyrer.opprettBehandlingRevurdering("RE-PRSSL");
        overstyrer.velgRevurderingBehandling();
        overstyrer.hentAksjonspunktbekreftelse(VarselOmRevurderingBekreftelse.class).bekreftIkkeSendVarsel();
        overstyrer.bekreftAksjonspunktBekreftelse(VarselOmRevurderingBekreftelse.class);
        overstyrer.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        overstyrer.overstyr(new OverstyrBeregning(saksbehandler.valgtFagsak, saksbehandler.valgtBehandling, 10));
        verifiserLikhet(10, overstyrer.valgtBehandling.getBeregningResultatEngangsstonad().getBeregnetTilkjentYtelse());
        overstyrer.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        beslutter.velgRevurderingBehandling();
        
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.OVERSTYRING_AV_BEREGNING));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();
    }
    

    @Test
    @DisplayName("Mor søker fødsel med flere barn")
    @Description("Mor søker fødsel med flere barn - happy case flere barn")
    public void morSøkerFødselFlereBarn() throws Exception {
        TestscenarioDto testscenario = opprettScenario("53");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonadFlereBarn(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
                .bekreftDokumentasjonForeligger(2, LocalDate.now().minusMonths(1));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class)
                .setVurdering(hentKodeverk().MedlemskapManuellVurderingType.getKode("MEDLEM"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        verifiserLikhet(beslutter.valgtBehandling.getBehandlingsresultat().toString(), "INNVILGET", "Behandlingstatus");
        verifiserLikhet(beslutter.valgtBehandling.getBeregningResultatEngangsstonad().getBeregnetTilkjentYtelse(), 2 * SATS_2019);
    }

    @Test
    @DisplayName("Mor søker fødsel med verge")
    @Description("Mor søker fødsel med verge - skal få aksjonspunkt om registrering av verge når man er under 18")
    public void morSøkerFødselMedVerge() throws Exception {
        TestscenarioDto testscenario = opprettScenario("54");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaVergeBekreftelse.class)
                .bekreftSøkerErKontaktperson()
                .bekreftSøkerErIkkeUnderTvungenForvaltning()
                .setVerge(testscenario.getPersonopplysninger().getAnnenpartIdent());
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaVergeBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
                .bekreftDokumentasjonForeligger(1, LocalDate.now().minusMonths(1));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class)
                .setVurdering(hentKodeverk().MedlemskapManuellVurderingType.getKode("MEDLEM"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        verifiserLikhet(beslutter.valgtBehandling.getBehandlingsresultat().toString(), "INNVILGET", "Behandlingstatus");
    }

    @Test
    @DisplayName("Mor søker uregistrert fødsel mindre enn 14 dager etter fødsel")
    @Description("Mor søker uregistrert fødsel mindre enn 14 dager etter fødsel. Behandlingen skal bli satt på vent")
    public void morSøkerUregistrertFødselMindreEnn14DagerEtter() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonadIGår(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        
        verifiser(saksbehandler.valgtBehandling.erSattPåVent(), "behandling er ikke satt på vent");
    }
    
    @Test
    @DisplayName("Medmor søker fødsel")
    @Description("Medmor søker fødsel - søkand blir avslått fordi søker er medmor")
    public void medmorSøkerFødsel() throws Exception {
        TestscenarioDto testscenario = opprettScenario("90");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakMedmorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        
        verifiserLikhet(saksbehandler.valgtBehandling.getBehandlingsresultat().toString(), "AVSLÅTT", "Behandlingstatus");
        saksbehandler.ventTilAvsluttetBehandling();

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
    }
    
}
