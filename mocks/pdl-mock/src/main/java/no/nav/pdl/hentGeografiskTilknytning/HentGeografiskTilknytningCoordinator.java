package no.nav.pdl.hentGeografiskTilknytning;


import no.nav.pdl.GeografiskTilknytning;

public interface HentGeografiskTilknytningCoordinator {
    GeografiskTilknytning hentGeografiskTilknytning(String ident);
}
