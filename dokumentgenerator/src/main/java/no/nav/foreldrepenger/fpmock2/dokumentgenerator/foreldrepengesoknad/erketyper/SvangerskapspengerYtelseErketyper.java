package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;
import java.util.List;

import no.nav.vedtak.felles.xml.soeknad.felles.v3.Medlemskap;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Opptjening;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Svangerskapspenger;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Tilrettelegging;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.TilretteleggingListe;

public class SvangerskapspengerYtelseErketyper {


    public static Svangerskapspenger svangerskapspengerMedOpptjening(LocalDate termindato, Medlemskap medlemskap, Opptjening opptjening, List<Tilrettelegging> tilretteleggingListe) {
        Svangerskapspenger svangerskapspenger = new Svangerskapspenger();
        svangerskapspenger.setTermindato(termindato);
        svangerskapspenger.setMedlemskap(medlemskap);
        svangerskapspenger.setOpptjening(opptjening);
        if(tilretteleggingListe != null) {
            TilretteleggingListe tl = new TilretteleggingListe();
            tl.getTilrettelegging().addAll(tilretteleggingListe);
            svangerskapspenger.setTilretteleggingListe(tl);
        }
        return svangerskapspenger;
    }

}
