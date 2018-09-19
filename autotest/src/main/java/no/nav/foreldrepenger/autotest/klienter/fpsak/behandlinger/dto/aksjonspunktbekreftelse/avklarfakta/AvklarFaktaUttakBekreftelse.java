package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.KontrollerFaktaPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakDokumentasjon;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@BekreftelseKode(kode="5070")
public class AvklarFaktaUttakBekreftelse extends AksjonspunktBekreftelse {

    public List<BekreftetUttakPeriode> bekreftedePerioder = new ArrayList<>();
    public List<BekreftetUttakPeriode> slettedePerioder = new ArrayList<>();
    
    public AvklarFaktaUttakBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        
        for (KontrollerFaktaPeriode periode : behandling.kontrollerFaktaData.perioder) {
            BekreftetUttakPeriode bekreftetUttakPeriode = new BekreftetUttakPeriode(periode.fom,
                    periode.tom,
                    periode.arbeidstidsprosent,
                    periode.begrunnelse,
                    periode);
            
            periode.begrunnelse = "Vurdert av autotest";
            periode.bekreftet = true;
            
            bekreftedePerioder.add(bekreftetUttakPeriode);
        }
    }
    
    public void godkjennPeriode(LocalDate fra, LocalDate til, Kode godkjenningskode) {
        BekreftetUttakPeriode periode = finnUttaksperiode(fra, til);
        
        periode.bekreftetPeriode.bekreftet = true;
        periode.bekreftetPeriode.resultat = godkjenningskode;
        periode.bekreftetPeriode.begrunnelse = "Godkjent av autotest";
        periode.bekreftetPeriode.dokumentertePerioder.add(new UttakDokumentasjon(fra, til, null));
    }
    
    public void delvisGodkjennPeriode(LocalDate fra, LocalDate til, LocalDate godkjentFra, LocalDate godkjentTil, Kode godkjenningskode) {
        BekreftetUttakPeriode periode = finnUttaksperiode(fra, til);
        
        periode.bekreftetPeriode.bekreftet = true;
        periode.bekreftetPeriode.resultat = godkjenningskode;
        periode.bekreftetPeriode.begrunnelse = "Godkjent av autotest";
        
        periode.bekreftetPeriode.fom = godkjentFra;
        periode.bekreftetPeriode.tom = godkjentTil;
        periode.bekreftetPeriode.dokumentertePerioder.add(new UttakDokumentasjon(godkjentFra, godkjentTil, null));
    }
    
    public void slettPeriode(LocalDate fra, LocalDate til) {
        BekreftetUttakPeriode periode = finnUttaksperiode(fra, til);
        
        slettedePerioder.add(periode);
        bekreftedePerioder.remove(periode);
        periode.bekreftetPeriode.begrunnelse = "Slettet av autotest";
    }
    
    public void avvisPeriode(LocalDate fra, LocalDate til, Kode avvisKode) {
        BekreftetUttakPeriode periode = finnUttaksperiode(fra, til);
        
        periode.bekreftetPeriode.bekreftet = false;
        periode.bekreftetPeriode.resultat = avvisKode;
        periode.bekreftetPeriode.begrunnelse = "Avvist av autotest";
        periode.bekreftetPeriode.dokumentertePerioder.clear();
    }
    
    private BekreftetUttakPeriode finnUttaksperiode(LocalDate fra, LocalDate til) {
        for (BekreftetUttakPeriode periode : bekreftedePerioder) {
            if(periode.orginalFom.equals(fra) && periode.orginalTom.equals(til)) {
                return periode;
            }
        }
        return null;
    }
    
    public class BekreftetUttakPeriode{
        
        LocalDate orginalFom;
        LocalDate orginalTom;
        BigDecimal originalArbeidstidsprosent;
        String originalBegrunnelse;
        KontrollerFaktaPeriode bekreftetPeriode;
        
        
        public BekreftetUttakPeriode(LocalDate orginalFom, LocalDate orginalTom, BigDecimal originalArbeidstidsprosent,
                String originalBegrunnelse, KontrollerFaktaPeriode bekreftetPeriode) {
            super();
            this.orginalFom = orginalFom;
            this.orginalTom = orginalTom;
            this.originalArbeidstidsprosent = originalArbeidstidsprosent;
            this.originalBegrunnelse = originalBegrunnelse;
            this.bekreftetPeriode = bekreftetPeriode;
        }
    }

}
