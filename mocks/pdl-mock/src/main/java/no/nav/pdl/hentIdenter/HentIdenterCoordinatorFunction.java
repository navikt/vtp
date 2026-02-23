package no.nav.pdl.hentIdenter;

import java.util.ArrayList;

import no.nav.pdl.IdentGruppe;
import no.nav.pdl.IdentInformasjon;
import no.nav.pdl.Identliste;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.ident.PersonIdent;

public class HentIdenterCoordinatorFunction {

    private HentIdenterCoordinatorFunction() {
    }

    public static HentIdenterCoordinator opprettCoordinator(PersonRepository personRepository) {
        return (ident, grupper)  -> {
            var person = personRepository.hentPerson(ident);

            var identInformasjonsliste = new ArrayList<IdentInformasjon>();
            if (grupper == null || grupper.contains(IdentGruppe.FOLKEREGISTERIDENT.name())) {
                identInformasjonsliste.add(new IdentInformasjon(person.personopplysninger().identifikator().value(), IdentGruppe.FOLKEREGISTERIDENT, false));
            }
            if (grupper == null || grupper.contains(IdentGruppe.AKTORID.name())) {
                identInformasjonsliste.add(new IdentInformasjon(((PersonIdent) person.personopplysninger().identifikator()).aktørId(), IdentGruppe.AKTORID, false));
            }

            return new Identliste(identInformasjonsliste);

        };
    }
}
