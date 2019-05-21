package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5074")
public class FastsettUttakKontrollerOpplysningerOmMedlemskap extends AksjonspunktBekreftelse {

    protected List<UttakResultatPeriode> perioder = new ArrayList<>();

    public FastsettUttakKontrollerOpplysningerOmMedlemskap(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);

        for (UttakResultatPeriode uttakPeriode : behandling.hentUttaksperioder()) {
            uttakPeriode.setBegrunnelse("Begrunnelse");
            LeggTilUttakPeriode(uttakPeriode);
        }
    }

    public void LeggTilUttakPeriode(UttakResultatPeriode uttakPeriode){
        perioder.add(uttakPeriode);
    }
}
