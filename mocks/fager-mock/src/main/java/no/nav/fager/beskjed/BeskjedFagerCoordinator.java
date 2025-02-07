package no.nav.fager.beskjed;

import no.nav.fager.NyBeskjedResultat;

public interface BeskjedFagerCoordinator {

    NyBeskjedResultat opprettBeskjed(String grupperingsid, String merkelapp, String virksomhetsnummer, String tittel, String lenke);
}
