package no.nav.pdl.hentIdenter;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerIdent;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.IdentGruppe;
import no.nav.pdl.IdentInformasjon;
import no.nav.pdl.Identliste;
import no.nav.pdl.oversetter.FamilieRelasjonBygger;
import no.nav.pdl.oversetter.PersonAdapter;

import java.util.List;

import static java.util.List.of;

public class HentIdenterCoordinatorFunction {
    public static HentIdenterCoordinator opprettCoordinator(TestscenarioBuilderRepository scenarioRepo) {
        // Skal oversette fra ident til Identliste?

        return ident -> {
            var personModell = (PersonModell) scenarioRepo.getPersonIndeks().finnByIdent(ident);
            var personPdl = PersonAdapter.oversettPerson(personModell);

            var personopplysningerModell = scenarioRepo.getPersonIndeks().finnPersonopplysningerByIdent(ident);
            var aktørIdent = personModell.getAktørIdent();
            personPdl = FamilieRelasjonBygger.byggFamilierelasjoner(aktørIdent, personopplysningerModell, personPdl);


            BrukerModell brukerModell = scenarioRepo.getPersonIndeks().finnByIdent(ident);

            if (brukerModell == null) {
                throw new IllegalArgumentException("Fant ikke identene for ident " + ident);
            }


            IdentInformasjon identFolkeregister = new IdentInformasjon(brukerModell.getIdent(), IdentGruppe.FOLKEREGISTERIDENT, false);
            IdentInformasjon identAktørId = new IdentInformasjon(brukerModell.getAktørIdent(), IdentGruppe.AKTORID, false);

            return new Identliste(
                    of(
                            identFolkeregister,
                            identAktørId
                    )
            );
        };
    }
}
