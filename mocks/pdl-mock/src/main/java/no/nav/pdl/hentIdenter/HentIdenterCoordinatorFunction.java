package no.nav.pdl.hentIdenter;

import java.util.ArrayList;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.IdentGruppe;
import no.nav.pdl.IdentInformasjon;
import no.nav.pdl.Identliste;

public class HentIdenterCoordinatorFunction {
    public static HentIdenterCoordinator opprettCoordinator(TestscenarioBuilderRepository scenarioRepo) {
        // Skal oversette fra ident til Identliste?
        return (ident, grupper)  -> {
            try {
                BrukerModell brukerModell = scenarioRepo.getPersonIndeks().finnByIdent(ident);

                ArrayList<IdentInformasjon> identInformasjonsliste = new ArrayList<>();

                if (grupper == null || grupper.contains(IdentGruppe.FOLKEREGISTERIDENT.name())) {
                    identInformasjonsliste.add(new IdentInformasjon(brukerModell.getIdent(), IdentGruppe.FOLKEREGISTERIDENT, false));
                }

                if (grupper == null || grupper.contains(IdentGruppe.AKTORID.name())) {
                    identInformasjonsliste.add(new IdentInformasjon(brukerModell.getAktørIdent(), IdentGruppe.AKTORID, false));
                }

                return new Identliste(identInformasjonsliste);

            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }
}
