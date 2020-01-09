package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.ytelse;

import no.nav.vedtak.felles.xml.soeknad.felles.v3.Medlemskap;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Opptjening;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Svangerskapspenger;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Tilrettelegging;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.TilretteleggingListe;

import java.time.LocalDate;
import java.util.List;
@Deprecated
public class SvangerskapspengerYtelseBuilder {
    private Svangerskapspenger kladd;

    public SvangerskapspengerYtelseBuilder (LocalDate termindato, Medlemskap medlemskap,
                                            List<Tilrettelegging> tilretteleggingListe) {
        kladd = new Svangerskapspenger();
        kladd.setTermindato(termindato);
        kladd.setMedlemskap(medlemskap);
        if(tilretteleggingListe != null) {
            TilretteleggingListe tl = new TilretteleggingListe();
            tl.getTilrettelegging().addAll(tilretteleggingListe);
            kladd.setTilretteleggingListe(tl);
        }
    }
    public SvangerskapspengerYtelseBuilder medSpesiellOpptjening(Opptjening opptjening) {
        kladd.setOpptjening(opptjening);
        return this;
    }
    public SvangerskapspengerYtelseBuilder medMedlemskap(Medlemskap medlemskap) {
        kladd.setMedlemskap(medlemskap);
        return this;
    }
    public SvangerskapspengerYtelseBuilder medFødseldato(LocalDate fødselsdato) {
        kladd.setFødselsdato(fødselsdato);
        return this;
    }
    public Svangerskapspenger build() {
        return kladd;
    }

}
