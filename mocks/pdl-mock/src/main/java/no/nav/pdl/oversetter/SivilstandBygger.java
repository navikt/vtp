package no.nav.pdl.oversetter;

import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SivilstandModell;
import no.nav.pdl.Person;
import no.nav.pdl.Sivilstand;
import no.nav.pdl.Sivilstandstype;

public class SivilstandBygger {

    public static void leggTilSivilstand(Person personPdl, PersonModell personModell, Personopplysninger personopplysningerModell) {
        SivilstandModell sivilstandTPS = personModell.getSivilstand();
        String sivilstandtypeTPS = sivilstandTPS.getSivilstandType().name();

        Sivilstand sivilstandPDL = new Sivilstand();
        sivilstandPDL.setType(Sivilstandstype.valueOf(SivilstandKoder.tilSivilstandPDL(sivilstandtypeTPS)));
        if (sivilstandPDL.getType() != Sivilstandstype.UGIFT || sivilstandPDL.getType() != Sivilstandstype.UOPPGITT) {
            sivilstandPDL.setRelatertVedSivilstand(personopplysningerModell.getAnnenPart().getIdent());
        }
        personPdl.setSivilstand(List.of(sivilstandPDL));
    }
}
