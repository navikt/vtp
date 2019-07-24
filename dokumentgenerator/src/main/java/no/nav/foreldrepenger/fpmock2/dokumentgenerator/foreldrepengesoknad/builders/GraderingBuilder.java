package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.builders;

import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Gradering;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Virksomhet;

import java.util.Objects;

public class GraderingBuilder extends UttaksperiodeBuilder {
    private Gradering kladd = new Gradering();

    public GraderingBuilder medArbeidstaker (String arbeidsgiverIdentifikator, Integer arbeidstidsprosent ) {
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
    public GraderingBuilder medSNFL(boolean erFL, boolean erSN) {
        this.kladd.setArbeidsforholdSomSkalGraderes(true);
        this.kladd.setErArbeidstaker(false);
        this.kladd.setErFrilanser(erFL);
        this.kladd.setErSelvstNæringsdrivende(erSN);
        return this;

    }
    public Gradering build(){
        super.build();
        Objects.requireNonNull(kladd.getArbeidtidProsent(), "Arbeidtidsprosent kan ikke være null");
        return this.kladd;
    }

}
