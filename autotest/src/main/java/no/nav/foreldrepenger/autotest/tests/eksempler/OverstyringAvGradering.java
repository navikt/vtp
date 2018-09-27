package no.nav.foreldrepenger.autotest.tests.eksempler;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrUttaksperioder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

public class OverstyringAvGradering extends FpsakTestBase{

    public void skalKunneOverstyreGradering() throws Exception {
        TestscenarioImpl testscenario = testscenarioRepository.opprettTestscenario(TestscenarioTemplateRepositoryImpl.getInstance().finn("50"));
        Soeknad søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøker().getIdent());
        //søknad.leggTilPeriode(new periode().gradering(25))
        
        fordel.erLoggetInnUtenRolle();
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario);
        
        /*
        Inntektsmelding = InntektsmeldingBuilder.fromSøknad(søkand);
        fordel.sendInnInntektsmelding(null, "1000104117747");
        */
        
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.valgtBehandling.hentUttaksperiode(0).getGraderingInnvilget(), "Gradering var ikke invilget. forventet invilget");
        
        saksbehandler.aksjonspunktBekreftelse(OverstyrUttaksperioder.class)
            .bekreftPeriodeGraderingErIkkeOppfylt(saksbehandler.valgtBehandling.hentUttaksperiode(0), new Kode());
        saksbehandler.bekreftAksjonspunktBekreftelse(OverstyrUttaksperioder.class);
        
        verifiser(!saksbehandler.valgtBehandling.hentUttaksperiode(0).getGraderingInnvilget(), "Gradering var invilget. forventet ikke invilget");
        verifiserLikhet(saksbehandler.valgtBehandling.hentUttaksperiode(0).getGradertArbeidsprosent(), 75);
        verifiserLikhet(saksbehandler.valgtBehandling.hentUttaksperiode(0).getGradertArbeidsprosent(), 25);
    }
}
