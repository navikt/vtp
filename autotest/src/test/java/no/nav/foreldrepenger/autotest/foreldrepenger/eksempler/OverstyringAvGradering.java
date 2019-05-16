package no.nav.foreldrepenger.autotest.foreldrepenger.eksempler;

import no.nav.foreldrepenger.autotest.base.FpsakTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrUttaksperioder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

public class OverstyringAvGradering extends FpsakTestBase {

    public void skalKunneOverstyreGradering() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.terminMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        //søknad.leggTilPeriode(new periode().gradering(25))

        fordel.erLoggetInnUtenRolle();
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);


        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.valgtBehandling.hentUttaksperiode(0).getGraderingInnvilget(), "Gradering var ikke invilget. forventet invilget");

        saksbehandler.aksjonspunktBekreftelse(OverstyrUttaksperioder.class)
                .bekreftPeriodeGraderingErIkkeOppfylt(saksbehandler.valgtBehandling.hentUttaksperiode(0), Kode.lagBlankKode());
        saksbehandler.bekreftAksjonspunktBekreftelse(OverstyrUttaksperioder.class);

        verifiser(!saksbehandler.valgtBehandling.hentUttaksperiode(0).getGraderingInnvilget(), "Gradering var invilget. forventet ikke invilget");
        verifiserLikhet(saksbehandler.valgtBehandling.hentUttaksperiode(0).getGradertArbeidsprosent(), 75);
        verifiserLikhet(saksbehandler.valgtBehandling.hentUttaksperiode(0).getGradertArbeidsprosent(), 25);
    }
}
