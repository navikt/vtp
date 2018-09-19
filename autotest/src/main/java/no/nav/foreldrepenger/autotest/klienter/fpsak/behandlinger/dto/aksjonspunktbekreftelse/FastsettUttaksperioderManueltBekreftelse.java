package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriodeAktivitet;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@BekreftelseKode(kode="5071")
public class FastsettUttaksperioderManueltBekreftelse extends AksjonspunktBekreftelse {

    private List<UttakResultatPeriode> perioder = new ArrayList<>();
    
    public FastsettUttaksperioderManueltBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        
        for (UttakResultatPeriode uttakPeriode : behandling.uttakResultatPerioder.perioder) {
            uttakPeriode.begrunnelse = "Begrunnelse";
            LeggTilUttakPeriode(uttakPeriode);
        }
    }
    
    public void godkjennAllePerioder() {
        for (UttakResultatPeriode uttakResultatPeriode : perioder) {
            godkjennPeriode(uttakResultatPeriode, 100);
        }
    }
    
    
    
    public void godkjennPeriode(LocalDate fra, LocalDate til, int utbetalingsgrad) {
        UttakResultatPeriode periode = finnPeriode(fra, til);
        godkjennPeriode(periode, utbetalingsgrad);
        
    }
    
    public void godkjennPeriode(UttakResultatPeriode periode, int utbetalingsgrad) {
        periode.periodeResultatType = new Kode("PERIODE_RESULTAT_TYPE", "INNVILGET", "Innvilget");
        periode.periodeResultatÅrsak = new Kode("INNVILGET_AARSAK", "2001", "§14-6: Uttak er oppfylt");
        
        //Utsettelses perioder trenger ikke trekkdager. set dem til 0
        for (UttakResultatPeriodeAktivitet aktivitet : periode.aktiviteter) {
            aktivitet.utbetalingsgrad = BigDecimal.valueOf(utbetalingsgrad);
            if(!periode.utsettelseType.kode.equals("-")) {
                aktivitet.trekkdager = 0;
            }
        }
    }
    
    
    public void avvisPeriode(LocalDate fra, LocalDate til, int utbetalingsgrad, Kode årsak) {
        UttakResultatPeriode periode = finnPeriode(fra, til);
        avvisPeriode(periode, utbetalingsgrad, årsak);
    }
    
    public void avvisPeriode(UttakResultatPeriode periode, int utbetalingsgrad, Kode årsak) {
        periode.periodeResultatType = new Kode("PERIODE_RESULTAT_TYPE", "AVSLÅTT", "Avslått");
        periode.periodeResultatÅrsak = new Kode(null, "-", null);
        
        //HACK for manglende aktivitet i periode (set aktivitet til å trekke fra mødrekvoten)
        for (UttakResultatPeriodeAktivitet aktivitet : periode.aktiviteter) {
            aktivitet.utbetalingsgrad = BigDecimal.valueOf(utbetalingsgrad);
            aktivitet.trekkdager = 0;
            if(aktivitet.stønadskontoType == null || aktivitet.stønadskontoType.kode.equals("-")) {
                aktivitet.stønadskontoType = new Kode("STOENADSKONTOTYPE", "MØDREKVOTE", "Mødrekvote");
            }
        }
    }
    
    public void LeggTilUttakPeriode(UttakResultatPeriode uttakPeriode){
        perioder.add(uttakPeriode);
    }
    
    public UttakResultatPeriode finnPeriode(LocalDate fra, LocalDate til) {
        for (UttakResultatPeriode uttakPeriode : perioder) {
            if(uttakPeriode.fom.equals(fra) && uttakPeriode.tom.equals(til)) {
                return uttakPeriode;
            }
        }
        return null;
    }
    
}
