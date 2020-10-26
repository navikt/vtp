package no.nav.pdl.hentIdenterBolk;

import static java.util.List.of;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.HentIdenterBolkResult;
import no.nav.pdl.IdentGruppe;
import no.nav.pdl.IdentInformasjon;

public class HentIdenterBolkCoordinatorFunction {
    public static HentIdenterBolkCoordinator opprettCoordinator(TestscenarioBuilderRepository testRepo) {

        // Skal oversette fra til List<HentIdenterBolkResult>
        return identer -> {
            List<HentIdenterBolkResult> collect = identer.stream()
                    .map(ident -> {
                                LokalIdentIndeks identer1 = testRepo.getIdenter(ident);
                                Map<String, String> alleIdenter = identer1.getAlleIdenter();

                                BrukerModell brukerModell = testRepo.getPersonIndeks().finnByIdent(ident);
                                String folkeregisterIdent = brukerModell.getIdent();
                                String aktørIdent = brukerModell.getAktørIdent();

                                List<IdentInformasjon> identInformasjonListe =
                                        of(
                                                new IdentInformasjon(folkeregisterIdent, IdentGruppe.FOLKEREGISTERIDENT, false),
                                                new IdentInformasjon(aktørIdent, IdentGruppe.AKTORID, false)

                                        );
                                return new HentIdenterBolkResult.Builder()
                                        .setIdent(ident)
                                        .setIdenter(identInformasjonListe)
                                        .build();
                            }
                    )
                    .collect(Collectors.toList());

            return collect;
        };


    }
}
