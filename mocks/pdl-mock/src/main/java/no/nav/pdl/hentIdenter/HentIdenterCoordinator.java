package no.nav.pdl.hentIdenter;

import java.util.List;

import no.nav.pdl.Identliste;

public interface HentIdenterCoordinator {
    Identliste hentIdenter(String ident, List<String> grupper);
}
