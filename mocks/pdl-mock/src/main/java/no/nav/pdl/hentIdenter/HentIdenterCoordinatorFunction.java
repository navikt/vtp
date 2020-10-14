package no.nav.pdl.hentIdenter;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.IdentGruppe;
import no.nav.pdl.IdentInformasjon;
import no.nav.pdl.Identliste;

import static java.util.List.of;

public class HentIdenterCoordinatorFunction {
    public static HentIdenterCoordinator opprettCoordinator(TestscenarioBuilderRepository scenarioRepo) {
        // Skal oversette fra ident til Identliste?
        return ident -> {
            BrukerModell brukerModell = scenarioRepo.getPersonIndeks().finnByIdent(ident);
            if (brukerModell == null) {
                throw new IllegalArgumentException("Fant ikke brukermodell for ident " + ident);
            }

            return new Identliste(
                    of(
                            new IdentInformasjon(brukerModell.getIdent(), IdentGruppe.FOLKEREGISTERIDENT, false),
                            new IdentInformasjon(brukerModell.getAktørIdent(), IdentGruppe.AKTORID, false)
                    )
            );
        };
    }
}
