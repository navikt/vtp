package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.medlem.Medlemskapsperiode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@BekreftelseKode(kode="5021")
public class AvklarBrukerHarGyldigPeriode extends AksjonspunktBekreftelse{

    protected ManuellVurderingType manuellVurderingType;
    protected List<Periode> periods = new ArrayList<>();
    
    public AvklarBrukerHarGyldigPeriode(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        
        for(Medlemskapsperiode periode : behandling.medlem.medlemskapPerioder){
            periods.add(new Periode(periode.beslutningsdato,
                                    periode.dekningType.navn,
                                    periode.fom,
                                    periode.tom,
                                    periode.medlemskapType.navn));
        }
    }
    
    public void setVurdering(Kode kode) {
        setVurdering(kode.kode);
    }
    
    public void setVurdering(String kode) {
        manuellVurderingType = new ManuellVurderingType(kode);
    }

    class ManuellVurderingType{
        String kode;
        
        public ManuellVurderingType(String kode) {
            this.kode = kode;
        }
    }
    
    class Periode{
        Object beslutningsdato;
        String dekning;
        LocalDate fom;
        LocalDate tom;
        String status;
        
        public Periode(Object beslutningsdato, String dekning, LocalDate fom, LocalDate tom, String status){
            this.beslutningsdato = beslutningsdato;
            this.dekning = dekning;
            this.fom = fom;
            this.tom = tom;
            this.status = status;
        }
    }
}
