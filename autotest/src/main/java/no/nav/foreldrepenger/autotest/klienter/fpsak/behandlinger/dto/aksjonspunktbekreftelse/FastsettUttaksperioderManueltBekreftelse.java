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

    protected List<UttakResultatPeriode> perioder = new ArrayList<>();
    
    public FastsettUttaksperioderManueltBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        
        for (UttakResultatPeriode uttakPeriode : behandling.hentUttaksperioder()) {
            if (uttakPeriode.getManuellBehandlingÅrsak() != null && !uttakPeriode.getManuellBehandlingÅrsak().kode.equals("-")) {
                uttakPeriode.setBegrunnelse("Begrunnelse");
            }
            LeggTilUttakPeriode(uttakPeriode);
        }
    }
    
    public void godkjennAllePerioder() {
        for (UttakResultatPeriode uttakResultatPeriode : perioder) {
            godkjennPeriode(uttakResultatPeriode, 100);
            uttakResultatPeriode.setBegrunnelse("Begrunnelse autotest");
        }
    }
    
    public FastsettUttaksperioderManueltBekreftelse godkjennPeriode(LocalDate fra, LocalDate til, int utbetalingsgrad) {
        UttakResultatPeriode periode = finnPeriode(fra, til);
        godkjennPeriode(periode, utbetalingsgrad);
        return this;
    }

    public FastsettUttaksperioderManueltBekreftelse godkjennPeriode(LocalDate fra, LocalDate til, Kode periodeResultatÅrsak, int utbetalingsgrad) {
        UttakResultatPeriode periode = finnPeriode(fra, til);
        periode.setPeriodeResultatType(new Kode("PERIODE_RESULTAT_TYPE", "INNVILGET", "Innvilget"));
        periode.setPeriodeResultatÅrsak(periodeResultatÅrsak);
        periode.setOppholdÅrsak(new Kode("OPPHOLD_AARSAK_TYPE", "-", "Ikke satt eller valgt kode"));
        periode.setBegrunnelse("Vurdering");
        godkjennPeriode(periode, utbetalingsgrad);
        return this;
    }

    public FastsettUttaksperioderManueltBekreftelse godkjennPeriode(LocalDate fra, LocalDate til, int utbetalingsgrad,
                                                                    Kode periodeResultatÅrsak, boolean flerbarnsdager, boolean samtidigUttak) {
        UttakResultatPeriode periode = finnPeriode(fra, til);
        periode.setPeriodeResultatType(new Kode("PERIODE_RESULTAT_TYPE", "INNVILGET", "Innvilget"));
        periode.setPeriodeResultatÅrsak(periodeResultatÅrsak);
        periode.setOppholdÅrsak(new Kode("OPPHOLD_AARSAK_TYPE", "-", "Ikke satt eller valgt kode"));
        periode.setSamtidigUttak(samtidigUttak);
        periode.setFlerbarnsdager(flerbarnsdager);
        periode.setBegrunnelse("Vurdering");
        godkjennPeriode(periode, utbetalingsgrad);
        return this;
    }

    public FastsettUttaksperioderManueltBekreftelse godkjennPeriode(LocalDate fra, LocalDate til, int utbetalingsgrad,
                                                                    Kode periodeResultatÅrsak, boolean samtidigUttak, int samtidigUttakProsent) {
        UttakResultatPeriode periode = finnPeriode(fra, til);
        periode.setPeriodeResultatType(new Kode("PERIODE_RESULTAT_TYPE", "INNVILGET", "Innvilget"));
        periode.setPeriodeResultatÅrsak(periodeResultatÅrsak);
        periode.setOppholdÅrsak(new Kode("OPPHOLD_AARSAK_TYPE", "-", "Ikke satt eller valgt kode"));
        periode.setSamtidigUttak(samtidigUttak);
        periode.setSamtidigUttaksprosent(BigDecimal.valueOf(samtidigUttakProsent));
        periode.setBegrunnelse("Vurdering");
        godkjennPeriode(periode, utbetalingsgrad);
        return this;
    }
    
    public FastsettUttaksperioderManueltBekreftelse godkjennPeriode(LocalDate fra, LocalDate til, int utbetalingsgrad, Kode stønadskonto) {
        UttakResultatPeriode periode = finnPeriode(fra, til);
        godkjennPeriode(periode, utbetalingsgrad, stønadskonto);
        return this;
    }
    
    public void godkjennPeriode(UttakResultatPeriode periode, int utbetalingsgrad) {
        periode.setPeriodeResultatType(new Kode("PERIODE_RESULTAT_TYPE", "INNVILGET", "Innvilget"));
        periode.setPeriodeResultatÅrsak(new Kode("INNVILGET_AARSAK", "2001", "§14-6: Uttak er oppfylt"));
        periode.setOppholdÅrsak(new Kode("OPPHOLD_AARSAK_TYPE", "-", "Ikke satt eller valgt kode"));
        
        //Utsettelses perioder trenger ikke trekkdager. set dem til 0
        for (UttakResultatPeriodeAktivitet aktivitet : periode.getAktiviteter()) {
            aktivitet.setUtbetalingsgrad(BigDecimal.valueOf(utbetalingsgrad));
            if(!periode.getUtsettelseType().kode.equals("-")) {
                aktivitet.setTrekkdagerDesimaler(BigDecimal.ZERO);
            }
        }
    }

    public FastsettUttaksperioderManueltBekreftelse godkjennPeriodeMedGradering(UttakResultatPeriode periode, Kode periodeResultatÅrsak) {
        periode.setPeriodeResultatType(new Kode("PERIODE_RESULTAT_TYPE", "INNVILGET", "Innvilget"));
        periode.setPeriodeResultatÅrsak(periodeResultatÅrsak);
        periode.setOppholdÅrsak(new Kode("OPPHOLD_AARSAK_TYPE", "-", "Ikke satt eller valgt kode"));

        for (UttakResultatPeriodeAktivitet aktivitet : periode.getAktiviteter()) {
            BigDecimal andelArbeid = aktivitet.getProsentArbeid();
            aktivitet.setUtbetalingsgrad(BigDecimal.valueOf(100).subtract(andelArbeid));
        }
        return this;
    }
    
    public void godkjennPeriode(UttakResultatPeriode periode, int utbetalingsgrad, Kode stønadskonto) {
        periode.setPeriodeResultatType(new Kode("PERIODE_RESULTAT_TYPE", "INNVILGET", "Innvilget"));
        periode.setPeriodeResultatÅrsak(new Kode("INNVILGET_AARSAK", "2001", "§14-6: Uttak er oppfylt"));
        periode.setOppholdÅrsak(new Kode("OPPHOLD_AARSAK_TYPE", "-", "Ikke satt eller valgt kode"));
        periode.setStønadskonto(stønadskonto);
        
        //Utsettelses perioder trenger ikke trekkdager. set dem til 0
        for (UttakResultatPeriodeAktivitet aktivitet : periode.getAktiviteter()) {
            aktivitet.setUtbetalingsgrad(BigDecimal.valueOf(utbetalingsgrad));
            if(!periode.getUtsettelseType().kode.equals("-")) {
                aktivitet.setTrekkdagerDesimaler(BigDecimal.ZERO);
            }
        }
    }
    
    
    public void avvisPeriode(LocalDate fra, LocalDate til, int utbetalingsgrad) {
        UttakResultatPeriode periode = finnPeriode(fra, til);
        avvisPeriode(periode, utbetalingsgrad);
    }
    
    public void avvisPeriode(UttakResultatPeriode periode, int utbetalingsgrad) {
        periode.setPeriodeResultatType(new Kode("PERIODE_RESULTAT_TYPE", "AVSLÅTT", "Avslått"));
        periode.setPeriodeResultatÅrsak(Kode.lagBlankKode());
        
        //HACK for manglende aktivitet i periode (set aktivitet til å trekke fra mødrekvoten)
        for (UttakResultatPeriodeAktivitet aktivitet : periode.getAktiviteter()) {
            aktivitet.setUtbetalingsgrad(BigDecimal.valueOf(utbetalingsgrad));
            aktivitet.setTrekkdagerDesimaler(BigDecimal.ZERO);
            if(aktivitet.getStønadskontoType() == null || aktivitet.getStønadskontoType().kode.equals("-")) {
                aktivitet.setStønadskontoType(new Kode("STOENADSKONTOTYPE", "MØDREKVOTE", "Mødrekvote"));
            }
        }
    }
    
    public void LeggTilUttakPeriode(UttakResultatPeriode uttakPeriode){
        perioder.add(uttakPeriode);
    }
    
    public UttakResultatPeriode finnPeriode(LocalDate fra, LocalDate til) {
        for (UttakResultatPeriode uttakPeriode : perioder) {
            if(uttakPeriode.getFom().equals(fra) && uttakPeriode.getTom().equals(til)) {
                return uttakPeriode;
            }
        }
        return null;
    }
    
}
