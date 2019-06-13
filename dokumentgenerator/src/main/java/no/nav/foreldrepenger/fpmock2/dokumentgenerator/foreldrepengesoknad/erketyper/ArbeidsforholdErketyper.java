package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;


import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Frilanser;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.PrivatArbeidsgiver;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.SelvstendigNæringsdrivende;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Virksomhet;

public class ArbeidsforholdErketyper {



    public static Virksomhet virksomhet(String identifikator){
        Virksomhet virksomhet = new Virksomhet();
        virksomhet.setIdentifikator(identifikator);
        return virksomhet;
    }

    public static PrivatArbeidsgiver privatArbeidsgiver(){
        return new PrivatArbeidsgiver();
    }

    public static SelvstendigNæringsdrivende selvstendigNæringsdrivende(){
        return new SelvstendigNæringsdrivende();
    }

    public static Frilanser frilanser() {
        return new Frilanser();
    }



}
