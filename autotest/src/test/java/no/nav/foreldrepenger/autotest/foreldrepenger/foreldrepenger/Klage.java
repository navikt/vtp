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
import no.nav.foreldrepenger.autotest.base.ForeldrepengerTestBase;
import no.nav.foreldrepenger.autotest.domain.foreldrepenger.Felles;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KlageFormkravBekreftelse.KlageFormkravKa;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KlageFormkravBekreftelse.KlageFormkravNfp;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvKlageBekreftelse.VurderingAvKlageNfpBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvKlageBekreftelse.VurderingAvKlageNkBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import no.nav.foreldrepenger.autotest.util.AllureHelper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto.ExpectRequestDto;
import no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto.ExpectResultDto;
import no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto.ExpectTokenDto;
import no.nav.foreldrepenger.autotest.util.AllureHelper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.felles.ExpectPredicate;
import no.nav.foreldrepenger.fpmock2.felles.ExpectRepository.Mock;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("fpsak")
@Tag("foreldrepenger")
public class Klage extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Klage med Medhold Ugunst NFP")
    @Description("Sender inn klage på førstegangsbehandling. Bekrefter medhold i Ugunst hos NFP. Beslutter og avslutter.")
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
        klagebehandler.ventTilSakHarBehandling("Klage");
        klagebehandler.velgBehandling("Klage");

        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .godkjennAlleFormkrav()
                .setBegrunnelse("blabla");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravNfp.class);
        String fritekstBrev = "Fritektst til brev fra NFP.";
        String begrunnelse = "Begrunnelse NFP.";
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftMedholdUGunst("ULIK_VURDERING")
                .fritekstBrev(fritekstBrev)
                .setBegrunnelse(begrunnelse);
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);

        //Mellomlager og tilbakestiller
        verifiserLikhet(klagebehandler.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NFP).getStatus().kode,
                "UTFO", "Vurdering av klage");
        klagebehandler.mellomlagreOgGjennåpneKlage();
        verifiserLikhet(klagebehandler.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NFP).getStatus().kode,
                "OPPR", "Vurdering av klage");

        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftMedholdUGunst("ULIK_VURDERING")
                .fritekstBrev(fritekstBrev)
                .setBegrunnelse(begrunnelse);
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);

        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling("Klage");
        
        ExpectTokenDto expectXml = expectKlient.createExpectation(new ExpectRequestDto(Mock.DOKUMENTPRODUKSJON.toString(), "produserIkkeredigerbartDokument", new ExpectPredicate("aktør", testscenario.getPersonopplysninger().getSøkerIdent())));
        
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NFP));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserKlageVurderingOmgjoer(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getKlageVurderingOmgjoer(), "UGUNST_MEDHOLD_I_KLAGE");
        verifiserBehandlingsresultat(beslutter.valgtBehandling.behandlingsresultat.toString(), "KLAGE_MEDHOLD");
        verifiserFritekst(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getBegrunnelse(), begrunnelse);
        verifiserFritekst(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getFritekstTilBrev(), fritekstBrev);
        verifiserLikhet(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNFP().getKlageMedholdArsak(), "ULIK_VURDERING", "Årsak");
        verifiserBehandlingsstatus(beslutter.valgtBehandling.status.kode, "AVSLU");
        
        ExpectResultDto result = expectKlient.checkExpectation(expectXml);
        Felles fellesxml = Felles.fromFile(result.getResultData());
        fellesxml.valider();
        
    }

    @Test
    @DisplayName("Klage med hjemsende av KA")
    @Description("Sender inn klage på førstegangsbehandling. NFP sender videre til KA. KA bekrefter hjemsende. Beslutter og avslutter.")
    public void hjemsendeKA() throws Exception {
        // opprette førstegangsbehandling til vedtak
        TestscenarioDto testscenario = opprettScenario("50");
        long saksnummer = opprettForstegangsbehandling(testscenario);

        // Motta og behandle klage NFP
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Aktoer.Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling("Klage");
        klagebehandler.velgBehandling("Klage");

        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .godkjennAlleFormkrav()
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
                .godkjennAlleFormkrav()
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
        beslutter.velgBehandling("Klage");
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserBehandlingsresultat(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "HJEMSENDE_UTEN_OPPHEVE");
        verifiserBehandlingsstatus(beslutter.getBehandlingsstatus(), "AVSLU");
    }

    @Test
    @DisplayName("Klage med stadfestet av KA")
    @Description("Sender inn klage på førstegangsbehandling. NFP sender videre til KA. KA bekrefter stadfestet. Beslutter og avslutter.")
    public void stadfesteKA() throws Exception {
        // opprette førstegangsbehandling til vedtak
        TestscenarioDto testscenario = opprettScenario("50");
        long saksnummer = opprettForstegangsbehandling(testscenario);

        // Motta og behandle klage NFP
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Aktoer.Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling("Klage");
        klagebehandler.velgBehandling("Klage");

        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .godkjennAlleFormkrav()
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
                .godkjennAlleFormkrav()
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
        beslutter.velgBehandling("Klage");
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserBehandlingsresultat(beslutter.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET");
        verifiserKlageVurdering(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNK().getKlageVurdering(), "STADFESTE_YTELSESVEDTAK");
        verifiserBehandlingsstatus(beslutter.valgtBehandling.status.kode, "AVSLU");
    }

    @Test
    @DisplayName("Klage med Medhold Delvis Gunst KA")
    @Description("Sender inn klage på førstegangsbehandling. NFP sender videre til KA. KA bekrefter medhold med delvis gunst. Beslutter og avslutter.")
    public void medholdDelvisGunstKA() throws Exception {
        // opprette førstegangsbehandling til vedtak
        TestscenarioDto testscenario = opprettScenario("50");
        long saksnummer = opprettForstegangsbehandling(testscenario);

        // Motta og behandle klage NFP
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Aktoer.Rolle.KLAGEBEHANDLER);
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

        // KA
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravKa.class)
                .godkjennAlleFormkrav()
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
        beslutter.ventTilSakHarBehandling("Klage");
        beslutter.velgBehandling("Klage");
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);

        verifiserBehandlingsresultat(beslutter.valgtBehandling.behandlingsresultat.toString(), "KLAGE_MEDHOLD");
        verifiserKlageVurderingOmgjoer(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNK().getKlageVurderingOmgjoer(), "DELVIS_MEDHOLD_I_KLAGE");
        verifiserLikhet(beslutter.valgtBehandling.klagevurdering.getKlageVurderingResultatNK().getKlageMedholdArsak(), "ULIK_VURDERING");
        verifiserBehandlingsstatus(beslutter.valgtBehandling.status.kode, "AVSLU");
    }

    @Test
    @DisplayName("Klage avvist i formkrav av NFP")
    @Description("Sender inn klage på førstegangsbehandling. NFP avslår formkrav (ikke konkret). Beslutter og avslutter.")
    public void avvisFormkravNFP() throws Exception {
        // opprette førstegangsbehandling til vedtak
        TestscenarioDto testscenario = opprettScenario("50");
        long saksnummer = opprettForstegangsbehandling(testscenario);

        // Motta og behandle klage NFP
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Aktoer.Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling("Klage");
        klagebehandler.velgBehandling("Klage");

        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravNfp.class)
                .klageErIkkeKonkret()
                .setBegrunnelse("blabla");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravNfp.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling("Klage");
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .setBegrunnelse("Godkjent");
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserBehandlingsresultat(beslutter.valgtBehandling.behandlingsresultat.toString(), "KLAGE_AVVIST");
        verifiserInneholder(beslutter.valgtBehandling.klagevurdering.getKlageFormkravResultatNFP().getAvvistArsaker(), new Kode("KLAGE_AVVIST_AARSAK", "IKKE_KONKRET"));
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
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilAvsluttetSak();
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
