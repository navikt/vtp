package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;
import java.util.List;

import no.nav.vedtak.felles.xml.soeknad.felles.v3.Medlemskap;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Opptjening;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Svangerskapspenger;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Tilrettelegging;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.TilretteleggingListe;

public class SvangerskapspengerYtelseErketyper {


    public static Svangerskapspenger svangerskapspenger(LocalDate termindato, Medlemskap medlemskap,  List<Tilrettelegging> tilretteleggingListe) {
        Svangerskapspenger svangerskapspenger = new Svangerskapspenger();
        svangerskapspenger.setTermindato(termindato);
        svangerskapspenger.setMedlemskap(medlemskap);
        if(tilretteleggingListe != null) {
            TilretteleggingListe tl = new TilretteleggingListe();
            tl.getTilrettelegging().addAll(tilretteleggingListe);
            svangerskapspenger.setTilretteleggingListe(tl);
        }
        return svangerskapspenger;
    }

}
