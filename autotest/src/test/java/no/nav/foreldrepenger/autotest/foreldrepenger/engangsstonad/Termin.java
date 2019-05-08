package no.nav.foreldrepenger.autotest.foreldrepenger.engangsstonad;

import java.time.LocalDate;

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
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderManglendeFodselBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTerminBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrFodselsvilkaaret;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Execution(ExecutionMode.CONCURRENT)
@Tag("fpsak")
@Tag("engangsstonad")
public class Termin extends EngangsstonadTestBase {

    @Test
    @DisplayName("Mor søker terming - godkjent")
    @Description("Mor søker terming - godkjent happy case")
    public void morSøkerTerminGodkjent() throws Exception {
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
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingstatus");

    }


    @Test
    @DisplayName("Mor søker terming men mangler dokumentasjon")
    @Description("Mor søker terming men mangler dokumentasjon og sender melding om manglende brev")
    public void morSøkerTerminManglerDokumentasjon() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.terminMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        saksbehandler.sendBrev("INNHEN", "Søker", "Trenger utstedt dato");
        saksbehandler.settBehandlingPåVent(LocalDate.now().plusDays(2), "AVV_DOK");

        //Todo mock brev
        //verifiser(saksbehandler.harDokument(""), "Behandling har ikke dokument");

        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.BREV_BESTILT);
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.BREV_SENDT);
    }

    @Test
    @DisplayName("Mor søker termin overstyrt vilkår")
    @Description("Mor søker termin overstyrt vilkår fødsel fra oppfylt til avvist")
    public void morSøkerTerminOvertyrt() throws Exception {
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
        overstyr.avvis(overstyrer.kodeverk.Avslagsårsak.get("FP_VK_1").getKode("1003" /* Søker er far */));
        overstyr.setBegrunnelse("avvist");
        overstyrer.overstyr(overstyr);

        verifiserLikhet(overstyrer.valgtBehandling.behandlingsresultat.toString(), "AVSLÅTT", "Behandlingstatus");
        overstyrer.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.OVERSTYRING_AV_FØDSELSVILKÅRET));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "AVSLÅTT", "Behandlingstatus");
    }

    public void behandleTerminMorUtenTerminbekreftelse() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
    }

    @Test
    @DisplayName("Far søker termin")
    @Description("Far søker termin avslått pga søker er far")
    public void farSøkerTermin() throws Exception {
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
    @DisplayName("Setter behandling på vent og gjennoptar og henlegger")
    @Description("Setter behandling på vent og gjennoptar og henlegger")
    public void settBehandlingPåVentOgGjenopptaOgHenlegg() throws Exception {
        //Opprett scenario og søknad
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.terminMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        //Send inn søknad
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.settBehandlingPåVent(LocalDate.now(), "AVV_DOK");
        verifiser(saksbehandler.valgtBehandling.erSattPåVent(), "Behandlingen er ikke satt på vent");

        saksbehandler.gjenopptaBehandling();
        verifiser(!saksbehandler.valgtBehandling.erSattPåVent(), "Behandlingen er satt på vent");
        
        saksbehandler.henleggBehandling(saksbehandler.kodeverk.BehandlingResultatType.getKode("HENLAGT_SØKNAD_TRUKKET"));
        verifiser(saksbehandler.valgtBehandling.erHenlagt(), "Behandlingen ble uventet ikke henlagt");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU", "behandlingsstatus");
    }
    
    @Test
    @DisplayName("Mor søker terming 25 dager etter fødsel")
    @Description("Mor søker terming 25 dager etter fødsel - Får aksjonpunkt om manglende fødsel - godkjent")
    public void morSøkerTermin25DagerTilbakeITid() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.terminMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent(), LocalDate.now().minusDays(26));

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
                .bekreftDokumentasjonForeligger(1, LocalDate.now().minusMonths(1));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingstatus");
        
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();
    }
    
}
