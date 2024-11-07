package no.nav.fager.oppgave;

import no.nav.fager.NyOppgaveResultat;
import no.nav.fager.OppgaveUtfoertVellykket;
import no.nav.fager.OppgaveUtgaattVellykket;

public interface OppgaveFagerCoordinator {

    NyOppgaveResultat opprettOppgave(String grupperingsid,
                                     String merkelapp,
                                     String virksomhetsnummer,
                                     String tekst,
                                     String lenke);

    OppgaveUtfoertVellykket utførttOppgave(String id);

    OppgaveUtgaattVellykket utgåttOppgave(String id);

    enum Tilstand {
        NY,
        UTFOERT,
        UTGAATT
    }
}
