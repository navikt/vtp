package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AnnenPartModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonNavn;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.vtp.testmodell.util.TestdataUtil;

public abstract class TestscenarioBuilderRepositoryImpl implements TestscenarioBuilderRepository {

    private static final Logger LOG = LoggerFactory.getLogger(TestscenarioBuilderRepositoryImpl.class);

    private final BasisdataProvider basisdata;
    private final Map<String, Testscenario> scenarios = new ConcurrentHashMap<>();
    private final Map<String, LokalIdentIndeks> identer = new ConcurrentHashMap<>();
    private PersonIndeks personIndeks = new PersonIndeks();
    private InntektYtelseIndeks inntektYtelseIndeks = new InntektYtelseIndeks();
    private OrganisasjonIndeks organisasjonIndeks = new OrganisasjonIndeks();

    @Override
    public Optional<OrganisasjonModell> getOrganisasjon(String orgnr) {
        return organisasjonIndeks.getModellForIdent(orgnr);
    }

    @Override
    public Set<String> hentAlleOrganisasjonsnummer() {
        return organisasjonIndeks.hentAlleOrganisasjonsnummer();
    }

    protected TestscenarioBuilderRepositoryImpl(BasisdataProvider basisdata) {
        this.basisdata = basisdata;
    }

    @Override
    public Map<String, Testscenario> getTestscenarios() {
        return scenarios;
    }

    @Override
    public Testscenario getTestscenario(String id) {
        return scenarios.get(id);
    }

    @Override
    public BasisdataProvider getBasisdata() {
        return basisdata;
    }

    @Override
    public EnheterIndeks getEnheterIndeks() {
        return getBasisdata().getEnheterIndeks();
    }

    public void indekser(TestscenarioImpl testScenario) {
        scenarios.put(testScenario.getId(), testScenario);
        var personopplysninger = testScenario.getPersonopplysninger();
        if (personopplysninger != null) {
            indekserPersonopplysninger(personopplysninger);
            inntektYtelseIndeks.leggTil(personopplysninger.getSøker().getIdent(), testScenario.getSøkerInntektYtelse());
            if (personopplysninger.getAnnenPart() != null) {
                inntektYtelseIndeks.leggTil(personopplysninger.getAnnenPart().getIdent(), testScenario.getAnnenpartInntektYtelse());
            }
        } else {
            LOG.warn("Scenario har ikke personopplysninger om bruker! Scenario har følgende innhold: {}", testScenario);
        }

        var organisasjonModeller = testScenario.getOrganisasjonModeller();
        if (organisasjonModeller != null) {
            List<OrganisasjonModell> modeller = organisasjonModeller.getModeller();
            organisasjonIndeks.leggTil(modeller);
        }
        testScenario.getPersonligArbeidsgivere().forEach(p -> personIndeks.leggTil(p));
    }

    @Override
    public void indekserPersonopplysninger(Personopplysninger personopplysninger) {
        SøkerModell søker = personopplysninger.getSøker();
        PersonNavn sokerNavn = TestdataUtil.getSokerName(søker);
        søker.setFornavn(sokerNavn.getFornavn());
        søker.setEtternavn(sokerNavn.getEtternavn());
        personIndeks.leggTil(søker);

        AnnenPartModell annenPart = personopplysninger.getAnnenPart();
        if(annenPart != null){
            PersonNavn annenPartNavn = TestdataUtil.getAnnenPartName(søker, annenPart);
            annenPart.setFornavn(annenPartNavn.getFornavn());
            annenPart.setEtternavn(annenPartNavn.getEtternavn());
            leggTilAdresseHvisIkkeSatt(søker, annenPart);
            personIndeks.leggTil(annenPart);
        }

        leggTilAdresseHvisIkkeSatt(søker, personopplysninger.getFamilierelasjoner());
        personIndeks.indekserFamilierelasjonBrukere(personopplysninger.getFamilierelasjoner());

        personIndeks.indekserPersonopplysningerByIdent(personopplysninger);
    }

    private void leggTilAdresseHvisIkkeSatt(SøkerModell søker, Collection<FamilierelasjonModell> familierelasjonModeller) {
        for (FamilierelasjonModell familierelasjonModell : familierelasjonModeller) {
            if (familierelasjonModell.getTil() instanceof PersonModell personModell) {
                leggTilAdresseHvisIkkeSatt(søker, personModell);
            }
        }
    }

    private void leggTilAdresseHvisIkkeSatt(SøkerModell søker, PersonModell modell) {
        if (modell.getAdresser().isEmpty()) {
            modell.setAdresser(søker.getAdresser());
        }
    }


    @Override
    public LokalIdentIndeks getIdenter(String unikScenarioId) {
        return identer.computeIfAbsent(unikScenarioId, n -> new LokalIdentIndeks(n, basisdata.getIdentGenerator()));
    }

    @Override
    public PersonIndeks getPersonIndeks() {
        return personIndeks;
    }

    @Override
    public Optional<InntektYtelseModell> getInntektYtelseModell(String ident) {
        return inntektYtelseIndeks.getModellForIdent(ident);
    }

    @Override
    public Optional<InntektYtelseModell> getInntektYtelseModellFraAktørId(String aktørId) {
        return inntektYtelseIndeks.getModellForIdent(aktørId.substring(aktørId.length() - 11));
    }

    @Override
    public Boolean slettScenario(String id) {
        return scenarios.remove(id) != null;
    }

    @Override
    public Boolean endreTestscenario(String id, Testscenario testscenario) {
        scenarios.replace(id, testscenario);
        return true;
    }

}
