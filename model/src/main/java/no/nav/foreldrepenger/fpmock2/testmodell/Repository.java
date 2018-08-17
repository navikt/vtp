package no.nav.foreldrepenger.fpmock2.testmodell;

import java.util.Collection;

import no.nav.foreldrepenger.fpmock2.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;

public class Repository {

    private Collection<Scenario> scenarios;
    private Indeks indeks;
    private AdresseIndeks adresseIndeks;
    private EnheterIndeks enheterIndeks;

    public Repository(ScenarioMapper mapper) {
        this.scenarios = mapper.scenarios();
        this.indeks = mapper.getIndeks();
        this.enheterIndeks = mapper.getEnheterIndeks();
        this.adresseIndeks = mapper.getAdresseIndeks();
    }

    public AdresseIndeks getAdresseIndeks() {
        return adresseIndeks;
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
