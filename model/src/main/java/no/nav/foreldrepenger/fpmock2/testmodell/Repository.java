package no.nav.foreldrepenger.fpmock2.testmodell;

import java.util.Collection;

import no.nav.foreldrepenger.fpmock2.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.VirksomhetIndeks;

public class Repository {

    private Collection<Scenario> scenarios;
    private Indeks indeks;
    private AdresseIndeks adresseIndeks;
    private EnheterIndeks enheterIndeks;
    private VirksomhetIndeks virksomheter;

    public Repository(ScenarioMapper mapper) {
        this.scenarios = mapper.scenarios();
        this.indeks = mapper.getIndeks();
        this.enheterIndeks = mapper.getEnheterIndeks();
        this.adresseIndeks = mapper.getAdresseIndeks();
        this.virksomheter = mapper.getVirksomheter();
    }

    public AdresseIndeks getAdresseIndeks() {
        return adresseIndeks;
    }
    
    public VirksomhetIndeks getVirksomheter() {
        return virksomheter;
    }

    public EnheterIndeks getEnheterIndeks() {
        return enheterIndeks;
    }

    public Indeks getIndeks() {
        return indeks;
    }

    public Collection<Scenario> getScenarios() {
        return scenarios;
    }

}
