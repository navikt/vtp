package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Uttaksperiodetyper;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Uttaksperiode;

public class UttaksperiodeBuilder {
    private Uttaksperiode kladd;

    public UttaksperiodeBuilder(String stønadskontoType, LocalDate fom, LocalDate tom) {
        kladd = new Uttaksperiode();
        medTidsperiode(fom, tom);
        medStønadskontoType(stønadskontoType);
    }

    private UttaksperiodeBuilder medTidsperiode(LocalDate fom, LocalDate tom){
        this.kladd.setFom(fom);
        this.kladd.setTom(tom);
        return this;
    }
    private UttaksperiodeBuilder medStønadskontoType(String stønadskontotype) {
        Uttaksperiodetyper uttaksperiodetyper = new Uttaksperiodetyper();
        uttaksperiodetyper.setKode(stønadskontotype);
        this.kladd.setType(uttaksperiodetyper);
        return this;
    }

    public UttaksperiodeBuilder medFlerbarnsdager(){
        this.kladd.setOenskerFlerbarnsdager(true);
        return this;
    }
    public UttaksperiodeBuilder medSamtidigUttak(BigDecimal samtidigUttakProsent ){
        this.kladd.setOenskerSamtidigUttak(true);
        this.kladd.setSamtidigUttakProsent(samtidigUttakProsent.doubleValue());
        return this;
    }
    public Uttaksperiode build(){
        return this.kladd;
    }
}
