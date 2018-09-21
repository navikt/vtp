package no.nav.foreldrepenger.autotest.tests.eksempler;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrUttaksperioder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;

public class OverstyringAvGradering extends FpsakTestBase{

    public void skalKunneOverstyreGradering() throws Exception {
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
