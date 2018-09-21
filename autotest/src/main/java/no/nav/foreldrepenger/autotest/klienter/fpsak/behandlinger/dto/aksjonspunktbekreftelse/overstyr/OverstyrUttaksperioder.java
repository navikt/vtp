package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr;

import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@BekreftelseKode(kode="6008")
public class OverstyrUttaksperioder extends OverstyringsBekreftelse{

    List<UttakResultatPeriode> perioder;
    
    public OverstyrUttaksperioder(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        
        perioder = behandling.hentUttaksperioder();
    }
    
    public void bekreftPeriodeErOppfylt(UttakResultatPeriode periode, Kode årsak) {
        periode.periodeResultatType = new Kode("PERIODE_RESULTAT_TYPE", "INNVILGET", "Innvilget");
        periode.periodeResultatÅrsak = årsak;
    }
    
    public void bekreftPeriodeErIkkeOppfylt(UttakResultatPeriode periode, Kode årsak) {
        periode.periodeResultatType = new Kode("PERIODE_RESULTAT_TYPE", "AVSLÅTT", "Avslått");
        periode.periodeResultatÅrsak = årsak;
    }
    
    public void bekreftPeriodeGraderingErOppfylt(UttakResultatPeriode periode) {
        periode.graderingAvslagÅrsak = Kode.lagBlankKode();
        periode.graderingInnvilget = true;
    }
    
    public void bekreftPeriodeGraderingErIkkeOppfylt(UttakResultatPeriode periode, Kode årsak) {
        periode.graderingAvslagÅrsak = årsak;
        periode.graderingInnvilget = false;
    }
    
}
