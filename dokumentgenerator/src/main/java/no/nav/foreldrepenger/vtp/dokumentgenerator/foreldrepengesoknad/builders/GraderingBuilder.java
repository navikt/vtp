package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders;

import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Uttaksperiodetyper;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Gradering;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Virksomhet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class GraderingBuilder {
    private Gradering kladd = new Gradering();

    public GraderingBuilder medGraderingArbeidstaker (String arbeidsgiverIdentifikator, Integer arbeidstidsprosent ) {
        Virksomhet virksomhet = new Virksomhet();
        virksomhet.setIdentifikator(arbeidsgiverIdentifikator);
        this.kladd.setArbeidsgiver(virksomhet);
        this.kladd.setArbeidsforholdSomSkalGraderes(true);
        this.kladd.setArbeidtidProsent(arbeidstidsprosent.doubleValue());
        this.kladd.setErArbeidstaker(true);
        this.kladd.setErFrilanser(false);
        this.kladd.setErSelvstNæringsdrivende(false);
        return this;
    }
    public GraderingBuilder medGraderingFL(Integer arbeidstidsprosent) {
        this.kladd.setArbeidsforholdSomSkalGraderes(true);
        this.kladd.setArbeidtidProsent(arbeidstidsprosent.doubleValue());
        this.kladd.setErArbeidstaker(false);
        this.kladd.setErFrilanser(true);
        this.kladd.setErSelvstNæringsdrivende(false);
        return this;
    }
    public GraderingBuilder medGraderingSN(Integer arbeidstidsprosent) {
        this.kladd.setArbeidsforholdSomSkalGraderes(true);
        this.kladd.setArbeidtidProsent(arbeidstidsprosent.doubleValue());
        this.kladd.setErArbeidstaker(false);
        this.kladd.setErFrilanser(false);
        this.kladd.setErSelvstNæringsdrivende(true);
        return this;
    }
    public GraderingBuilder medTidsperiode(LocalDate fom, LocalDate tom){
        this.kladd.setFom(fom);
        this.kladd.setTom(tom);
        return this;
    }
    public GraderingBuilder medFlerbarnsdager(boolean flerbarnsdager){
        this.kladd.setOenskerFlerbarnsdager(flerbarnsdager);
        return this;
    }
    public GraderingBuilder medSamtidigUttak(boolean samtidigUttak, BigDecimal samtidigUttakProsent ){
        this.kladd.setOenskerSamtidigUttak(samtidigUttak);
        this.kladd.setSamtidigUttakProsent(samtidigUttakProsent.doubleValue());
        return this;
    }
    public GraderingBuilder medStønadskontoType(String stønadskontotype) {
        Uttaksperiodetyper uttaksperiodetyper = new Uttaksperiodetyper();
        uttaksperiodetyper.setKode(stønadskontotype);
        this.kladd.setType(uttaksperiodetyper);
        return this;
    }
    public Gradering build(){
        Objects.requireNonNull(kladd.getFom(), "FOM kan ikke være null");
        Objects.requireNonNull(kladd.getFom(), "TOM kan ikke være null");
        Objects.requireNonNull(kladd.getType(), "TYPE kan ikke være null");
        Objects.requireNonNull(kladd.getArbeidtidProsent(), "Arbeidtidsprosent kan ikke være null");
        return this.kladd;
    }

}
