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
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderEktefellesBarnBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaAdopsjonsdokumentasjonBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("fpsak")
@Tag("engangsstonad")
public class Revurdering extends EngangsstonadTestBase {

    @Test
    @DisplayName("Manuelt opprettet revurdering")
    @Description("Manuelt opprettet revurdering etter avsluttet behandling med utsendt varsel")
    public void manueltOpprettetRevurderingSendVarsel() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.adopsjonMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);

        AvklarFaktaAdopsjonsdokumentasjonBekreftelse bekreftelse1 = saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaAdopsjonsdokumentasjonBekreftelse.class);
        bekreftelse1.setBarnetsAnkomstTilNorgeDato(LocalDate.now());
        VurderEktefellesBarnBekreftelse bekreftelse2 = saksbehandler.hentAksjonspunktbekreftelse(VurderEktefellesBarnBekreftelse.class);
        bekreftelse2.bekreftBarnErIkkeEktefellesBarn();
        saksbehandler.bekreftAksjonspunktbekreftelserer(bekreftelse1, bekreftelse2);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);


        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_OM_ADOPSJON_GJELDER_EKTEFELLES_BARN));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "INNVILGET", "Behandlingsresultat");

        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.BREV_SENDT);
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

        saksbehandler.opprettBehandlingRevurdering("RE-FEFAKTA");
        saksbehandler.velgRevurderingBehandling();

        saksbehandler.hentAksjonspunktbekreftelse(VarselOmRevurderingBekreftelse.class)
                .bekreftSendVarsel(saksbehandler.kodeverk.Venteårsak.getKode("UTV_FRIST"), "Send brev");
        saksbehandler.bekreftAksjonspunktBekreftelse(VarselOmRevurderingBekreftelse.class);

        saksbehandler.harHistorikkinnslag(HistorikkInnslag.REVURD_OPPR);
        saksbehandler.harHistorikkinnslag(HistorikkInnslag.BREV_BESTILT);
        saksbehandler.harHistorikkinnslag(HistorikkInnslag.BEH_VENT);

        verifiser(saksbehandler.valgtBehandling.erSattPåVent(), "Behandlingen er ikke satt på vent etter varsel for revurdering");
    }
}
