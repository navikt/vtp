package no.nav.foreldrepenger.fpmock2.testmodell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;

public class Indeks {
    private static final Logger log = LoggerFactory.getLogger(Indeks.class);
    private final List<Scenario> scenarios = new ArrayList<>();
    private final Map<String, ScenarioIdenter> identer = new HashMap<>();

    private PersonIndeks personIndeks = new PersonIndeks();
    
    private InntektYtelseIndeks inntektYtelseIndeks = new InntektYtelseIndeks();

    public void indekser(Scenario scenario) {
        scenarios.add(scenario);
        Personopplysninger personopplysninger = scenario.getPersonopplysninger();
        if (personopplysninger == null) {
            log.warn("Scenario mangler innhold:" + scenario.getNavn());
        } else {
            personIndeks.leggTil(personopplysninger.getSøker());
            personIndeks.leggTil(personopplysninger.getAnnenPart());
            personIndeks.indekserFamilierelasjonBrukere(personopplysninger.getFamilierelasjoner());
            
            personIndeks.indekserPersonopplysningerByIdent(personopplysninger);
        }
        
        InntektYtelse inntektYtelse = scenario.getInntektYtelse();
        inntektYtelse.getModeller().forEach(iy -> inntektYtelseIndeks.leggTil(iy));
    }
    
    public ScenarioIdenter getIdenter(String scenarioNavn) {
        return identer.computeIfAbsent(scenarioNavn, n -> new ScenarioIdenter(n));
    }

    public Collection<Scenario> getScenarios() {
        return scenarios;
    }

    public PersonIndeks getPersonIndeks() {
        return personIndeks;
    }

    public Optional<InntektYtelseModell> getInntektYtelseModell(String ident) {
        return inntektYtelseIndeks.getModellForIdent(ident);
    }
}
