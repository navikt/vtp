package no.nav.foreldrepenger.fpmock2.testmodell;

import java.util.HashMap;
import java.util.Map;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Identer;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;

public class Scenario {

    private final String name;

    /** identity cache for dette scenario. Medfører at identer kan genereres dynamisk basert på lokal id referanse i scenarioet. */
    private final Identer identer;

    /** variable som kan injectes i json struktur. */
    private Map<String, String> vars = new HashMap<>();

    private AdresseIndeks adresseIndeks = new AdresseIndeks();

    private Personopplysninger personopplysninger;

    public Scenario(String name, Identer identer) {
        this.name = name;
        this.identer = identer;
    }

    public String getNavn() {
        return name;
    }

    public Identer getIdenter() {
        return identer;
    }

    public Map<String, String> getVars() {
        return vars;
    }

    public AdresseIndeks getAdresseIndeks() {
        return adresseIndeks;
    }

    public void setVars(Map<String, String> map) {
        this.vars = map;
    }

    public void setAdresseIndeks(AdresseIndeks adresseIndeks) {
        this.adresseIndeks = adresseIndeks;
    }

    public void setPersonopplysninger(Personopplysninger personopplysninger) {
        this.personopplysninger = personopplysninger;
        personopplysninger.setIdenter(identer);
    }

    public Personopplysninger getPersonopplysninger() {
        return this.personopplysninger;
    }
}
