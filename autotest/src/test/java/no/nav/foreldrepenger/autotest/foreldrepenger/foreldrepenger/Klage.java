package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KlageFormkravBekreftelse.KlageFormkravKa;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KlageFormkravBekreftelse.KlageFormkravNfp;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvKlageBekreftelse.VurderingAvKlageNfpBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvKlageBekreftelse.VurderingAvKlageNkBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.util.AllureHelper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("smoke")
@Tag("foreldrepenger")
public class Klage extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Klage med Medhold Ugunst NFP")
    @Description("Sender inn klage på førstegangsbehandling. Bekrefter medhold i Ugunst. Beslutter og avslutter.")
    public void klageMedholUgunstNFP() throws Exception {
        // opprette førstegangsbehandling til vedtak
        TestscenarioDto testscenario = opprettScenario("50");
        long saksnummer = opprettForstegangsbehandling(testscenario);

        // Motta og behandle klage NFP
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Aktoer.Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        AllureHelper.debugLoggBehandlingsliste(saksbehandler.behandlinger);
        klagebehandler.ventTilSakHarBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.velgBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        String vedtaksId = "";
        for (Behandling behandling : klagebehandler.behandlinger) {
            if (behandling.type.kode.equals("BT-002")) {
                Integer id = behandling.id;
                vedtaksId = id.toString(); }
        }
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .godkjennAlleFormkrav(vedtaksId)
                .setBegrunnelse("blabla");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravNfp.class);
        String fritekstBrev = "Fritektst til brev fra NFP.";
        String begrunnelse = "Begrunnelse NFP.";
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftMedholdUGunst("ULIK_VURDERING")
                .fritekstBrev(fritekstBrev)
                .setBegrunnelse(begrunnelse);
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NFP));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserKlageVurderingOmgjoer(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getKlageVurderingOmgjoer(), "UGUNST_MEDHOLD_I_KLAGE");
        verifiserBehandlingsresultat(beslutter.valgtBehandling.behandlingsresultat.toString(), "KLAGE_MEDHOLD");
        verifiserFritekst(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getBegrunnelse(), begrunnelse);
        verifiserFritekst(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getFritekstTilBrev(), fritekstBrev);
        verifiserLikhet(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getKlageMedholdArsak(), "ULIK_VURDERING", "Årsak");
        verifiserBehandlingsstatus(beslutter.valgtBehandling.status.kode, "AVSLU");
    }

    @Test
    public void hjemsendeKA() throws Exception {
        // opprette førstegangsbehandling til vedtak
        TestscenarioDto testscenario = opprettScenario("50");
        long saksnummer = opprettForstegangsbehandling(testscenario);

        // Motta og behandle klage NFP
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Aktoer.Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.velgBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        String vedtaksId = "";
        for (Behandling behandling : klagebehandler.behandlinger) {
            if (behandling.type.kode.equals("BT-002")) {
                Integer id = behandling.id;
                vedtaksId = id.toString(); }
        }
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .godkjennAlleFormkrav(vedtaksId)
                .setBegrunnelse("blabla");
        debugLoggBehandling(klagebehandler.valgtBehandling);
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravNfp.class);
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftStadfestet()
                .fritekstBrev("Fritekst brev fra nfp")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        verifiserBehandlingsresultat(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET");

        // KA
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravKa.class)
                .godkjennAlleFormkrav(vedtaksId)
                .setBegrunnelse("blabla begrunnelse");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravKa.class);
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNkBekreftelse.class)
                .bekreftHjemsende()
                .fritekstBrev("Fritekst brev fra KA")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNkBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserBehandlingsresultat(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "HJEMSENDE_UTEN_OPPHEVE");
        verifiserBehandlingsstatus(beslutter.valgtBehandling.status.kode, "AVSLU");
    }

    @Test
    public void stadfesteKA() throws Exception {
        // opprette førstegangsbehandling til vedtak
        TestscenarioDto testscenario = opprettScenario("50");
        long saksnummer = opprettForstegangsbehandling(testscenario);

        // Motta og behandle klage NFP
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Aktoer.Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.velgBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        String vedtaksId = "";
        for (Behandling behandling : klagebehandler.behandlinger) {
            if (behandling.type.kode.equals("BT-002")) {
                Integer id = behandling.id;
                vedtaksId = id.toString(); }
        }
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .godkjennAlleFormkrav(vedtaksId)
                .setBegrunnelse("Begrunnelse NFP.");
        debugLoggBehandling(klagebehandler.valgtBehandling);
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravNfp.class);
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftStadfestet()
                .fritekstBrev("Fritekst brev fra nfp")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        verifiserBehandlingsresultat(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET");

        // KA
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravKa.class)
                .godkjennAlleFormkrav(vedtaksId)
                .setBegrunnelse("blabla begrunnelse");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravKa.class);
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNkBekreftelse.class)
                .bekreftStadfestet()
                .fritekstBrev("Fritekst brev fra KA")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNkBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserBehandlingsresultat(beslutter.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET");
        verifiserKlageVurdering(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNK().getKlageVurdering(), "STADFESTE_YTELSESVEDTAK");
        verifiserBehandlingsstatus(beslutter.valgtBehandling.status.kode, "AVSLU");
    }

    @Test
    public void medholdDelvisGunstKA() throws Exception {
        // opprette førstegangsbehandling til vedtak
        TestscenarioDto testscenario = opprettScenario("50");
        long saksnummer = opprettForstegangsbehandling(testscenario);

        // Motta og behandle klage NFP
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Aktoer.Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.velgBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        String vedtaksId = "";
        for (Behandling behandling : klagebehandler.behandlinger) {
            if (behandling.type.kode.equals("BT-002")) {
                Integer id = behandling.id;
                vedtaksId = id.toString(); }
        }
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .godkjennAlleFormkrav(vedtaksId)
                .setBegrunnelse("blabla");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravNfp.class);
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftStadfestet()
                .fritekstBrev("Fritekst brev fra nfp")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        verifiserBehandlingsresultat(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET");

        // KA
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravKa.class)
                .godkjennAlleFormkrav(vedtaksId)
                .setBegrunnelse("blabla begrunnelse");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravKa.class);
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNkBekreftelse.class)
                .bekreftMedholdDelvisGunst("ULIK_VURDERING")
                .fritekstBrev("Fritekst brev fra KA")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNkBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.ventTilSakHarBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);

        verifiserBehandlingsresultat(beslutter.valgtBehandling.behandlingsresultat.toString(), "KLAGE_MEDHOLD");
        verifiserKlageVurderingOmgjoer(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNK().getKlageVurderingOmgjoer(), "DELVIS_MEDHOLD_I_KLAGE");
        verifiserLikhet(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNK().getKlageMedholdArsak(), "ULIK_VURDERING");
        verifiserBehandlingsstatus(beslutter.valgtBehandling.status.kode, "AVSLU");
    }

    @Test
    public void avvisFormkravNFP() throws Exception {
        // opprette førstegangsbehandling til vedtak
        TestscenarioDto testscenario = opprettScenario("50");
        long saksnummer = opprettForstegangsbehandling(testscenario);

        // Motta og behandle klage NFP
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Aktoer.Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.velgBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        String vedtaksId = "";
        for (Behandling behandling : klagebehandler.behandlinger) {
            if (behandling.type.kode.equals("BT-002")) {
                Integer id = behandling.id;
                vedtaksId = id.toString(); }
        }
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .klageErIkkeKonkret(vedtaksId)
                .setBegrunnelse("blabla");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravNfp.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .setBegrunnelse("Godkjent");
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserBehandlingsresultat(beslutter.valgtBehandling.behandlingsresultat.toString(), "KLAGE_AVVIST");
        verifiserLikhet(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getKlageAvvistArsak(), "IKKE_KONKRET", "Avvist årsak");
        verifiserBehandlingsstatus(beslutter.valgtBehandling.status.kode, "AVSLU");
    }

    @Step("Klage: oppretter førstegangsbehandling")
    private long opprettForstegangsbehandling(TestscenarioDto testscenario) throws Exception {
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        AllureHelper.debugLoggBehandlingsliste(saksbehandler.behandlinger);
        saksbehandler.velgBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Førstegangsbehandling"));
        saksbehandler.ventTilØkonomioppdragFerdigstilles();
        AllureHelper.debugLoggBehandlingsliste(saksbehandler.behandlinger);

        return saksnummer;
    }

    private void verifiserFritekst(String verdiFaktisk, String verdiForventet) {
        verifiserLikhet(verdiFaktisk, verdiForventet, "Fritekst");
    }

    private void verifiserKlageVurderingOmgjoer(String verdiFaktisk, String verdiForventet) {
        verifiserLikhet(verdiFaktisk, verdiForventet, "KlageVurderingOmgjoer");
    }

    private void verifiserBehandlingsstatus(String verdiFaktisk, String verdiForventet) {
        verifiserLikhet(verdiFaktisk, verdiForventet, "Behandlingsstatus");
    }

    private void verifiserBehandlingsresultat(String verdiFaktisk, String verdiForventet) {
        verifiserLikhet(verdiFaktisk, verdiForventet, "Behandlingsresultat");
    }

    private void verifiserKlageVurdering(String verdiFaktisk, String verdiForventet) {
        verifiserLikhet(verdiFaktisk, verdiForventet, "KlageVurdering");
    }
}