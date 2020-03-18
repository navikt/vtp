package no.nav.foreldrepenger.vtp.testmodell.dokument.modell;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.BrukerType;

public class JournalpostBruker {
    private String ident;
    private BrukerType brukerType;


    public JournalpostBruker(String ident, BrukerType brukerType) {
        this.ident = ident;
        this.brukerType = brukerType;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public BrukerType getBrukerType() {
        return brukerType;
    }

    public void setBrukerType(BrukerType brukerType) {
        this.brukerType = brukerType;
    }
}
