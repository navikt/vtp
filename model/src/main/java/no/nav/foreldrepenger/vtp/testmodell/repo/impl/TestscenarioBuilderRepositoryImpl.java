package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import no.nav.foreldrepenger.vtp.testmodell.util.TestdataUtil;
import no.nav.foreldrepenger.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioImpl;

public abstract class TestscenarioBuilderRepositoryImpl implements TestscenarioBuilderRepository {

    private static final Logger log = LoggerFactory.getLogger(TestscenarioBuilderRepositoryImpl.class);

    private final BasisdataProvider basisdata;
    private final Map<String, Testscenario> scenarios = new ConcurrentHashMap<>(); // not ordered for front-end
    private final Map<String, LokalIdentIndeks> identer = new ConcurrentHashMap<>();
    private PersonIndeks personIndeks = new PersonIndeks();
    private InntektYtelseIndeks inntektYtelseIndeks = new InntektYtelseIndeks();
    private OrganisasjonIndeks organisasjonIndeks = new OrganisasjonIndeks();

    @Override
    public Optional<OrganisasjonModell> getOrganisasjon(String orgnr) {
        return organisasjonIndeks.getModellForIdent(orgnr);
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
        Personopplysninger personopplysninger = testScenario.getPersonopplysninger();
        if (personopplysninger == null) {
            log.warn("TestscenarioImpl mangler innhold:" + testScenario);
        } else {
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
                personIndeks.leggTil(annenPart);
            }
            personIndeks.indekserFamilierelasjonBrukere(personopplysninger.getFamilierelasjoner());

            personIndeks.indekserPersonopplysningerByIdent(personopplysninger);
            testScenario.getPersonligArbeidsgivere().forEach(p -> personIndeks.leggTil(p));

            inntektYtelseIndeks.leggTil(personopplysninger.getSøker().getIdent(), testScenario.getSøkerInntektYtelse());
            if (personopplysninger.getAnnenPart() != null) {
                inntektYtelseIndeks.leggTil(personopplysninger.getAnnenPart().getIdent(), testScenario.getAnnenpartInntektYtelse());
            }
        }

        OrganisasjonModeller organisasjonModeller = testScenario.getOrganisasjonModeller();
        if (organisasjonModeller != null) {
            List<OrganisasjonModell> modeller = organisasjonModeller.getModeller();
            organisasjonIndeks.leggTil(modeller);
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
    public Boolean endreTestscenario(Testscenario testscenario) {
        return null;
    }

}
