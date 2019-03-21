package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.builders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Uttaksperiodetyper;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Uttaksperiode;

public class UttaksperiodeBuilder {
    private Uttaksperiode kladd = new Uttaksperiode();

    public UttaksperiodeBuilder medTidsperiode(LocalDate fom, LocalDate tom){
        this.kladd.setFom(fom);
        this.kladd.setTom(tom);
        return this;
    }
    public UttaksperiodeBuilder medFlerbarnsdager(boolean flerbarnsdager){
        this.kladd.setOenskerFlerbarnsdager(flerbarnsdager);
        return this;
    }
    public UttaksperiodeBuilder medSamtidigUttak(boolean samtidigUttak, BigDecimal samtidigUttakProsent ){
        this.kladd.setOenskerSamtidigUttak(samtidigUttak);
        this.kladd.setSamtidigUttakProsent(samtidigUttakProsent.doubleValue());
        return this;
    }
    public UttaksperiodeBuilder medStønadskontoType(String stønadskontotype) {
        Uttaksperiodetyper uttaksperiodetyper = new Uttaksperiodetyper();
        uttaksperiodetyper.setKode(stønadskontotype);
        this.kladd.setType(uttaksperiodetyper);
        return this;
    }
    public Uttaksperiode build(){
        Objects.requireNonNull(kladd.getFom(), "FOM kan ikke være null");
        Objects.requireNonNull(kladd.getFom(), "TOM kan ikke være null");
        Objects.requireNonNull(kladd.getType(), "TYPE kan ikke være null");
        return this.kladd;
    }
}
