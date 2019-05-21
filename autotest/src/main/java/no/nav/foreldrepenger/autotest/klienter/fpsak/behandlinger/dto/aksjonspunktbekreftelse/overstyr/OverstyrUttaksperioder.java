package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr;

import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@BekreftelseKode(kode="6008")
public class OverstyrUttaksperioder extends OverstyringsBekreftelse{

    protected List<UttakResultatPeriode> perioder;
    
    public OverstyrUttaksperioder(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        
        perioder = behandling.hentUttaksperioder();
    }
    
    public void bekreftPeriodeErOppfylt(UttakResultatPeriode periode, Kode årsak) {
        periode.setPeriodeResultatType(new Kode("PERIODE_RESULTAT_TYPE", "INNVILGET", "Innvilget"));
        periode.setPeriodeResultatÅrsak(årsak);
    }
    
    public void bekreftPeriodeErIkkeOppfylt(UttakResultatPeriode periode, Kode årsak) {
        periode.setPeriodeResultatType(new Kode("PERIODE_RESULTAT_TYPE", "AVSLÅTT", "Avslått"));
        periode.setPeriodeResultatÅrsak(årsak);
    }
    
    public void bekreftPeriodeGraderingErOppfylt(UttakResultatPeriode periode) {
        periode.setGraderingAvslagÅrsak(Kode.lagBlankKode());
        periode.setGraderingInnvilget(true);
    }
    
    public void bekreftPeriodeGraderingErIkkeOppfylt(UttakResultatPeriode periode, Kode årsak) {
        periode.setGraderingAvslagÅrsak(årsak);
        periode.setGraderingInnvilget(false);
    }
    
}
