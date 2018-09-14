package no.nav.tjeneste.virksomhet.journal.modell;

import no.nav.foreldrepenger.fpmock2.testmodell.journal.JournalpostModell;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;

public class JournalpostBygger {

    public static Journalpost buildFrom(JournalpostModell modell){
        return new Journalpost();
    }
}
