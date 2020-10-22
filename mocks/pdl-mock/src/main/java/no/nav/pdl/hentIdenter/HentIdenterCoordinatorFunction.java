package no.nav.pdl.hentIdenter;

import static java.util.List.of;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.IdentGruppe;
import no.nav.pdl.IdentInformasjon;
import no.nav.pdl.Identliste;

public class HentIdenterCoordinatorFunction {
    public static HentIdenterCoordinator opprettCoordinator(TestscenarioBuilderRepository scenarioRepo) {
        // Skal oversette fra ident til Identliste?
        return ident -> {
            try {
                BrukerModell brukerModell = scenarioRepo.getPersonIndeks().finnByIdent(ident);

                return new Identliste(
                        of(
                                new IdentInformasjon(brukerModell.getIdent(), IdentGruppe.FOLKEREGISTERIDENT, false),
                                new IdentInformasjon(brukerModell.getAktørIdent(), IdentGruppe.AKTORID, false)
                        )
                );
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }
}
