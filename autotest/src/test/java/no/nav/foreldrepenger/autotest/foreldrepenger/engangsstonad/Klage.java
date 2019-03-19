package no.nav.foreldrepenger.autotest.foreldrepenger.engangsstonad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.base.EngangsstonadTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KlageFormkravBekreftelse.KlageFormkravKa;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KlageFormkravBekreftelse.KlageFormkravNfp;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvKlageBekreftelse.VurderingAvKlageNfpBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvKlageBekreftelse.VurderingAvKlageNkBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("fpsak")
@Tag("engangsstonad")
public class Klage extends EngangsstonadTestBase {

    @Test
    @DisplayName("Behandle klage via NFP - medhold")
    public void klageMedholdNFP() throws Exception {
        // Opprette førstegangssøknad engangsstønad
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        opprettForstegangssoknadVedtak(saksnummer);

        // Motta og behandle klage NFP
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling("Klage");
        klagebehandler.velgBehandling("Klage");

        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .godkjennAlleFormkrav()
                .setBegrunnelse("blabla");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravNfp.class);
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftMedholdGunst("PROSESSUELL_FEIL")
                .fritekstBrev("Fritektst til brev fra klagebehandler.")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        verifiserBehandlingsresultat(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_MEDHOLD");
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling("Klage");
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NFP));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserBehandlingsstatus(beslutter.valgtBehandling.status.kode, "AVSLU");

    }
    
    @Test
    @DisplayName("Behandle klage via NFP - påklaget vedtak opphevet")
    public void klageOppheveAvKA() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        opprettForstegangssoknadVedtak(saksnummer);

        // Motta og behandle klage - NFP
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling("Klage");
        klagebehandler.velgBehandling("Klage");

        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .godkjennAlleFormkrav()
                .setBegrunnelse("blabla");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravNfp.class);
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftStadfestet()
                .fritekstBrev("Fritekst brev fra nfp")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        verifiserBehandlingsresultat(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET");

        // KA - klage kommer rett til KA uten totrinnsbehanling. Kan fortsette med samme klagebehandler.
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravKa.class)
                .godkjennAlleFormkrav()
                .setBegrunnelse("blabla begrunnelse");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravKa.class);
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNkBekreftelse.class)
                .bekreftOpphevet("NYE_OPPLYSNINGER")
                .fritekstBrev("Fritekst brev fra KA")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNkBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        verifiserBehandlingsresultat(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_OPPHEVET");

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling("Klage");
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NK));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

    }
    
    @Test
    @DisplayName("Behandle klage via NFP - påklaget vedtak omgjort/medhold")
    public void klageOmgjortAvKA() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        opprettForstegangssoknadVedtak(saksnummer);

        // Motta og behandle klage - NFP
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling("Klage");
        klagebehandler.velgBehandling("Klage");

        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .godkjennAlleFormkrav()
                .setBegrunnelse("blabla");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravNfp.class);
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftStadfestet()
                .fritekstBrev("Fritekst brev fra nfp")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        verifiserBehandlingsresultat(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET");

        // KA - klage kommer rett til KA uten totrinnsbehanling. Kan fortsette med samme klagebehandler.
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravKa.class)
                .godkjennAlleFormkrav()
                .setBegrunnelse("blabla begrunnelse");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravKa.class);
        
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNkBekreftelse.class)
            .bekreftMedholdGunst("NYE_OPPLYSNINGER")
            .fritekstBrev("Brev");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNkBekreftelse.class);
        
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling("Klage");
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NK));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();
    }

    @Test
    @DisplayName("Behandle klage via KA - avslag")
    public void klageAvslaattAvKA() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        opprettForstegangssoknadVedtak(saksnummer);

        // Motta og behandle klage - NFP
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling("Klage");
        klagebehandler.velgBehandling("Klage");

        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .godkjennAlleFormkrav()
                .setBegrunnelse("blabla");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravNfp.class);
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftStadfestet()
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        verifiserBehandlingsresultat(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET");

        // Behandle klage - KA
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravKa.class)
                .klageErIkkeKonkret()
                .setBegrunnelse("Begrunnelse formkrav");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravKa.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling("Klage");
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.VURDERING_AV_FORMKRAV_KLAGE_KA));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserBehandlingsresultat(beslutter.valgtBehandling.behandlingsresultat.toString(), "KLAGE_AVVIST");
    }

    @Test
    @DisplayName("Behandle klage via NFP - avvist av beslutter")
    public void avvistAvBelutterNFP() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        opprettForstegangssoknadVedtak(saksnummer);

        // Motta og behandle klage - NFP
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling("Klage");
        klagebehandler.velgBehandling("Klage");

        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .godkjennAlleFormkrav()
                .setBegrunnelse("blabla");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravNfp.class);
        String fritekstbrev1 = "Fritekst brev nfp.";
        String begrunnelse1 = "Fordi.";
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftMedholdGunst("NYE_OPPLYSNINGER")
                .fritekstBrev(fritekstbrev1)
                .setBegrunnelse(begrunnelse1);
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        verifiserLikhet(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_MEDHOLD", "Behandlingsresultat");
        verifiserKlageVurderingOmgjoer(klagebehandler.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getKlageVurderingOmgjoer(), "GUNST_MEDHOLD_I_KLAGE");

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.ventTilSakHarBehandling("Klage");
        beslutter.velgBehandling("Klage");

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .avvisAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NFP), beslutter.kodeverk.BehandlingÅrsakType.getKode("RE-LOV"))
                .setBegrunnelse("Avvist av beslutter");
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);

        klagebehandler.erLoggetInnMedRolle(Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);
        klagebehandler.velgBehandling("Klage");
        verifiserFritekst(klagebehandler.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getFritekstTilBrev(), fritekstbrev1);
        verifiserFritekst(klagebehandler.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getBegrunnelse(), begrunnelse1);
        String fritekstbrev2 = "Fritekst brev nr 2 .";
        String begrunnelse2 = "Fordi.";
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftMedholdDelvisGunst("NYE_OPPLYSNINGER")
                .fritekstBrev(fritekstbrev2)
                .setBegrunnelse(begrunnelse2);
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.ventTilSakHarBehandling("Klage");
        beslutter.velgBehandling("Klage");
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NFP));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserBehandlingsresultat(beslutter.valgtBehandling.behandlingsresultat.toString(), "KLAGE_MEDHOLD");
        verifiserFritekst(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getFritekstTilBrev(), fritekstbrev2);
        verifiserFritekst(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getBegrunnelse(), begrunnelse2);
        verifiserKlageVurderingOmgjoer(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getKlageVurderingOmgjoer(), "DELVIS_MEDHOLD_I_KLAGE");
        verifiserBehandlingsstatus(beslutter.valgtBehandling.status.navn, "Avsluttet");
    }

    private void opprettForstegangssoknadVedtak(long saksnummer) throws Exception {
        // Opprette førstegangssøknad engangsstønad
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        verifiserBehandlingsresultat(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "INNVILGET");
        saksbehandler.ventTilHistorikkinnslag("Brev sendt");
        
        saksbehandler.ventTilAvsluttetBehandling();
    }

    private void verifiserBehandlingsresultat(String verdiFaktisk, String verdiForventet) {
        verifiserLikhet(verdiFaktisk, verdiForventet, "Behandlingsresultat");
    }

    private void verifiserBehandlingsstatus(String verdiFaktisk, String verdiForventet) {
        verifiserLikhet(verdiFaktisk, verdiForventet, "Behandlingsstatus");
    }

    private void verifiserFritekst(String verdiFaktisk, String verdiForventet) {
        verifiserLikhet(verdiFaktisk, verdiForventet, "Fritekst");
    }

    private void verifiserKlageVurderingOmgjoer(String verdiFaktisk, String verdiForventet) {
        verifiserLikhet(verdiFaktisk, verdiForventet, "KlageVurderingOmgjoer");
    }
}
