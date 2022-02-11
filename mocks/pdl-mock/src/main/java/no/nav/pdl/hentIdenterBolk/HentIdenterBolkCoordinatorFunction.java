package no.nav.pdl.hentIdenterBolk;

import static java.util.List.of;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.HentIdenterBolkResult;
import no.nav.pdl.IdentGruppe;
import no.nav.pdl.IdentInformasjon;

public class HentIdenterBolkCoordinatorFunction {

    private HentIdenterBolkCoordinatorFunction() {
    }

    public static HentIdenterBolkCoordinator opprettCoordinator(TestscenarioBuilderRepository testRepo) {
        // Skal oversette fra til List<HentIdenterBolkResult>
        return identer -> identer.stream()
                .map(ident -> {
                            BrukerModell brukerModell = testRepo.getPersonIndeks().finnByIdent(ident);
                            String folkeregisterIdent = brukerModell.getIdent();
                            String aktørIdent = brukerModell.getAktørIdent();

                            return new HentIdenterBolkResult.Builder()
                                    .setIdent(ident)
                                    .setIdenter(
                                            of(
                                                    new IdentInformasjon(folkeregisterIdent, IdentGruppe.FOLKEREGISTERIDENT, false),
                                                    new IdentInformasjon(aktørIdent, IdentGruppe.AKTORID, false)
                                            )
                                    )
                                    .build();
                        }
                )
                .toList();


    }
}
