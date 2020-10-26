package no.nav.pdl.hentIdenterBolk;

import java.util.List;

import no.nav.pdl.HentIdenterBolkResult;

public interface HentIdenterBolkCoordinator {
    List<HentIdenterBolkResult> hentIdenterBolk(List<String> identer);
}
