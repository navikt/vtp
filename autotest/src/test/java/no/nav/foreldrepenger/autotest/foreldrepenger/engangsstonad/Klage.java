package no.nav.foreldrepenger.autotest.foreldrepenger.engangsstonad;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.*;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarBrukerHarGyldigPeriodeBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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

        // Motta og behandle klage
        // TODO: MV: Endre test naar formkrav er implementert i FPSAK
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.velgBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftMedhold("PROSESSUELL_FEIL")
                .setVedtaksdatoPaklagdBehandling(LocalDate.now())
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        verifiserLikhet(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_MEDHOLD", "behandlingsresultat");
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
    }

    @Test
    public void klageStadfesteAvKA() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        opprettForstegangssoknadVedtak(saksnummer);

        //TODO: MV: Endre når formkrav er implementert i FPSAK
        // Motta og behandle klage - NFP
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.velgBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftStadfestet()
                .setVedtaksdatoPaklagdBehandling(LocalDate.now())
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        verifiserLikhet(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET", "Behandlingsresultat");

        // KA - klage kommer rett til KA uten totrinnsbehanling. Kan fortsette med samme klagebehandler.
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNkBekreftelse.class)
                .bekreftStadfestet()
                .setVedtaksdatoPaklagdBehandling(LocalDate.now())
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNkBekreftelse.class);
        verifiserLikhet(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET", "Behandlingsresultat");
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        // Skriver ingenting i fritekst til brev. Skal dette gaa?
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        // vent til brev sendt?
        // vent til behandlingsstatus "Avslu"?

    }

    @Test
    public void klageAvslaattAvKA() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        opprettForstegangssoknadVedtak(saksnummer);

        //TODO: MV: Endre når formkrav er implementert i FPSAK
        // Motta og behandle klage - NFP
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.velgBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftStadfestet()
                .setVedtaksdatoPaklagdBehandling(LocalDate.now())
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        verifiserLikhet(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET", "Behandlingsresultat");

        // Behandle klage - KA
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNkBekreftelse.class)
                .bekreftAvvist("KLAGE_UGYLDIG")
                .setVedtaksdatoPaklagdBehandling(LocalDate.now())
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNkBekreftelse.class);
        klagebehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(sakId);
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Klage"));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        //saksbehandler.ventTilHistorikkinnslag("Brev sendt");
        //saksbehandler.ventTilBehandlingsstatus("AVSLU");
        // vent til brev sendt?
        // vent til behandlingsstatus "Avslu"?
    }

    @Test
    public void hoppeTilbakeStegNfp() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        opprettForstegangssoknadVedtak(saksnummer);

        //TODO: MV: Endre når formkrav er implementert i FPSAK
        // Motta og behandle klage - NFP
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long sakId = fordel.sendInnKlage(null, testscenario, saksnummer);
        klagebehandler.erLoggetInnMedRolle(Rolle.KLAGEBEHANDLER);
        klagebehandler.hentFagsak(sakId);

        klagebehandler.ventTilSakHarBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.velgBehandling(klagebehandler.kodeverk.BehandlingType.getKode("Klage"));
        klagebehandler.hentAksjonspunktbekreftelse(VurderingAvKlageNfpBekreftelse.class)
                .bekreftStadfestet()
                .setVedtaksdatoPaklagdBehandling(LocalDate.now())
                .setBegrunnelse("Fordi");
        klagebehandler.bekreftAksjonspunktBekreftelse(VurderingAvKlageNfpBekreftelse.class);
        verifiserLikhet(klagebehandler.valgtBehandling.behandlingsresultat.toString(), "KLAGE_YTELSESVEDTAK_STADFESTET", "Behandlingsresultat");
        //

    }

    public void opprettForstegangssoknadVedtak(long saksnummer)throws Exception{
        // Opprette førstegangssøknad engangsstønad
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
                .bekreftDokumentasjonForeligger(1, LocalDate.now().minusDays(15));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class)
                .setVurdering(hentKodeverk().MedlemskapManuellVurderingType.getKode("MEDLEM"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.ikkeVentPåStatus = true;
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);

        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingstatus");
        saksbehandler.ventTilHistorikkinnslag("Brev sendt");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
    }
}
