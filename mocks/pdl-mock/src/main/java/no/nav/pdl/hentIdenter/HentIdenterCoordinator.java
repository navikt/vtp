package no.nav.pdl.hentIdenter;

import java.util.ArrayList;

import no.nav.pdl.Identliste;

public interface HentIdenterCoordinator {
    Identliste hentIdenter(String ident, ArrayList<String> grupper);
}
