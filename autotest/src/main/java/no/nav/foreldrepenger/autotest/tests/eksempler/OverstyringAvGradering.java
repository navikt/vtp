package no.nav.foreldrepenger.autotest.tests.eksempler;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrUttaksperioder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.JournalRepositoryImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;

public class OverstyringAvGradering extends FpsakTestBase{

    public void skalKunneOverstyreGradering() throws Exception {
        TestscenarioRepositoryImpl instance = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        TestscenarioImpl testscenario = instance.opprettTestscenario(TestscenarioTemplateRepositoryImpl.getInstance().finn("50"));
        
        fordel.erLoggetInnUtenRolle();
        /*
        Søkand søkand = SøknadBuilder.nyFødselsøknad();
        søknad.leggTilPeriode(new periode().gradering(25))
        */
        long saksnummer = fordel.sendInnSøknad(null);
        
        /*
        Inntektsmelding = InntektsmeldingBuilder.fromSøknad(søkand);
        */
        fordel.sendInnInntektsmelding(null);
        
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.valgtBehandling.hentUttaksperiode(0).graderingInnvilget, "Gradering var ikke invilget. forventet invilget");
        
        saksbehandler.aksjonspunktBekreftelse(OverstyrUttaksperioder.class)
            .bekreftPeriodeGraderingErIkkeOppfylt(saksbehandler.valgtBehandling.hentUttaksperiode(0), new Kode());
        saksbehandler.bekreftAksjonspunktBekreftelse(OverstyrUttaksperioder.class);
        
        verifiser(!saksbehandler.valgtBehandling.hentUttaksperiode(0).graderingInnvilget, "Gradering var invilget. forventet ikke invilget");
        verifiserLikhet(saksbehandler.valgtBehandling.hentUttaksperiode(0).gradertAktivitet.prosentArbeid.longValue(), 75);
        verifiserLikhet(saksbehandler.valgtBehandling.hentUttaksperiode(0).gradertAktivitet.prosentArbeid.longValue(), 25);
    }
}
