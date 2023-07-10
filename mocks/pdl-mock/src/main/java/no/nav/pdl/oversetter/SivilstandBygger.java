package no.nav.pdl.oversetter;

import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pdl.Person;
import no.nav.pdl.Sivilstand;
import no.nav.pdl.Sivilstandstype;

public class SivilstandBygger {

    private SivilstandBygger() {
    }

    public static void leggTilSivilstand(Person personPdl, PersonModell personModell, Personopplysninger personopplysningerModell) {
        var sivilstandTPS = personModell.getSivilstand();
        var sivilstandtypeTPS = sivilstandTPS.getSivilstandType().name();

        var sivilstandPDL = new Sivilstand();
        sivilstandPDL.setType(Sivilstandstype.valueOf(SivilstandKode.valueOf(sivilstandtypeTPS).getSivilstandPDL()));

        if (personopplysningerModell != null && personopplysningerModell.getAnnenPart() != null
                && !List.of(Sivilstandstype.UGIFT, Sivilstandstype.UOPPGITT).contains(sivilstandPDL.getType())) {
            sivilstandPDL.setRelatertVedSivilstand(personopplysningerModell.getAnnenPart().getIdent());
        }
        personPdl.setSivilstand(List.of(sivilstandPDL));
    }

    private enum SivilstandKode {
        ENKE("ENKE_ELLER_ENKEMANN"),
        GIFT("GIFT"),
        GJPA("GJENLEVENDE_PARTNER"),
        GLAD("UOPPGITT"),
        REPA("REGISTRERT_PARTNER"),
        EPA("SEPARERT_PARTNER"),
        SEPR("SEPARERT"),
        SKIL("SKILT"),
        SKPA("SKILT_PARTNER"),
        UGIF("UGIFT");

        private final String sivilstandPDL;

        SivilstandKode(String sivilstandPDL) {
            this.sivilstandPDL = sivilstandPDL;
        }

        private String getSivilstandPDL() {
            return sivilstandPDL;
        }
    }
}
