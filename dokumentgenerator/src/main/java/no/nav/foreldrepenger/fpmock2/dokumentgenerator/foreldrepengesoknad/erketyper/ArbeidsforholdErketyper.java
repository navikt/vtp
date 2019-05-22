package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Frilanser;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.PrivatArbeidsgiver;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.SelvstendigNæringsdrivende;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Virksomhet;

public class ArbeidsforholdErketyper {



    public Virksomhet virksomhet(){
        return new Virksomhet();
    }

    public PrivatArbeidsgiver privatArbeidsgiver(){
        return new PrivatArbeidsgiver();
    }

    public SelvstendigNæringsdrivende selvstendigNæringsdrivende(){
        return new SelvstendigNæringsdrivende();
    }

    public Frilanser frilanser() {
        return new Frilanser();
    }



}
