package no.nav.foreldrepenger.autotest.foreldrepenger.engangsstonad;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.*;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KlageFormkravBekreftelse.KlageFormkravNfp;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KlageFormkravBekreftelse.KlageFormkravKa;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvKlageBekreftelse.VurderingAvKlageNfpBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderingAvKlageBekreftelse.VurderingAvKlageNkBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

@Tag("pending")
@Tag("smoke")
@Tag("engangsstonad")
public class Klage extends EngangsstonadTestBase {

    @Test
    public void klageMedholdNFP() throws  Exception {
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
                .bekreftMedholdGunst("PROSESSUELL_FEIL")
                .fritekstBrev("Fritektst til brev fra klagebehandler.")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        verifiserLikhet(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_MEDHOLD", "behandlingsresultat");
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NFP));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserLikhet(beslutter.valgtBehandling.status.kode, "AVSLU", "behandlingstatus");
    }

    @Test
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
        verifiserLikhet(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET", "Behandlingsresultat");

        // KA - klage kommer rett til KA uten totrinnsbehanling. Kan fortsette med samme klagebehandler.
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravKa.class)
                .godkjennAlleFormkrav(vedtaksId)
                .setBegrunnelse("blabla begrunnelse");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravKa.class);
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNkBekreftelse.class)
                .bekreftOpphevet("NYE_OPPLYSNINGER")
                .fritekstBrev("Fritekst brev fra KA")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNkBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        verifiserLikhet(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_OPPHEVET", "Behandlingsresultat");

        // TODO (MV): Følg opp når fritekst til brev er rettet
        /*beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NK));
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();
        beslutter.ventTilHistorikkinnslag("Brev sendt");*/
    }

    @Test
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
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        verifiserLikhet(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET", "Behandlingsresultat");

        // Behandle klage - KA
        klagebehandler.hentAksjonspunktbekreftelse(KlageFormkravKa.class)
                .klageErIkkeKonkret(vedtaksId)
                .setBegrunnelse("Begrunnelse formkrav");
        klagebehandler.bekreftAksjonspunktBekreftelse(KlageFormkravKa.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.VURDERING_AV_FORMKRAV_KLAGE_KA));
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class);
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "KLAGE_AVVIST", "Behandlingsresultat");
        beslutter.ventTilHistorikkinnslag("Brev sendt");
    }

    @Test
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
                .bekreftMedholdGunst("NYE_OPPLYSNINGER")
                .fritekstBrev("Fritekst brev nfp")
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        verifiserLikhet(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_MEDHOLD", "Behandlingsresultat");

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.ventTilSakHarBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .avvisAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NFP), beslutter.kodeverk.BehandlingÅrsakType.getKode("RE-LOV"))
                .setBegrunnelse("Avvist av beslutter");
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);

        klagebehandler.erLoggetInnMedRolle(Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);
        klagebehandler.velgBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftMedholdDelvisGunst("NYE_OPPLYSNINGER")
                .fritekstBrev("Fritekst brev nr 2.")
                .setBegrunnelse("Fordi.");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.ventTilSakHarBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NFP));
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class);
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "KLAGE_MEDHOLD", "Behandlingsresultat");
        beslutter.ventTilHistorikkinnslag("Brev sendt");
    }

    private void opprettForstegangssoknadVedtak(long saksnummer)throws Exception{
        // Opprette førstegangssøknad engangsstønad
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
                .bekreftDokumentasjonForeligger(1, LocalDate.now().minusDays(15)); //minusMonths(1)?
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.ikkeVentPåStatus = true;
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();

        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingstatus");
        beslutter.ventTilHistorikkinnslag("Brev sendt");
    }
}
