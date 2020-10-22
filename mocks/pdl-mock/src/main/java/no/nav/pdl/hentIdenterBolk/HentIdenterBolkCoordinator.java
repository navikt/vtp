package no.nav.pdl.hentIdenterBolk;

import no.nav.pdl.HentIdenterBolkResult;

public interface HentIdenterBolkCoordinator {
    HentIdenterBolkResult hentIdenterBolk(String [] identer, String [] grupper, boolean historikk);
}
