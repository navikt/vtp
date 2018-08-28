package no.nav.foreldrepenger.fpmock2.testmodell;

import java.util.HashMap;
import java.util.Map;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.VirksomhetIndeks;

public class Scenario {

    private final String name;

    /** identity cache for dette scenario. Medfører at identer kan genereres dynamisk basert på lokal id referanse i scenarioet. */
    private final ScenarioIdenter identer;

    /** variable som kan injectes i json struktur. */
    private Map<String, String> vars = new HashMap<>();

    private AdresseIndeks adresseIndeks;

    private Personopplysninger personopplysninger;
    
    private InntektYtelse inntektYtelse = new InntektYtelse();

    private ScenarioVirksomheter scenarioVirksomheter;

    public Scenario(String name, ScenarioIdenter identer, VirksomhetIndeks virksomhetIndeks) {
        this.name = name;
        this.identer = identer;
        this.scenarioVirksomheter = new ScenarioVirksomheter(this.name, virksomhetIndeks);
        this.inntektYtelse.setIdenter(identer);
    }

    public String getNavn() {
        return name;
    }

    public ScenarioIdenter getIdenter() {
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

    public InntektYtelse getInntektYtelse() {
        return this.inntektYtelse;
    }
    
    public void leggTil(InntektYtelseModell iyModell) {
        this.inntektYtelse.leggTil(iyModell);
    }

    public ScenarioVirksomheter getVirksomheter() {
        return scenarioVirksomheter;
    }
}
