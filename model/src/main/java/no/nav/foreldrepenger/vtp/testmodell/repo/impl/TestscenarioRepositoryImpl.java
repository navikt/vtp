package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.util.FiktivtNavn;

/** Indeks av alle testdata instanser. */
public class TestscenarioRepositoryImpl extends TestscenarioBuilderRepositoryImpl implements TestscenarioRepository {

    private static TestscenarioRepositoryImpl instance;

    public static synchronized TestscenarioRepositoryImpl getInstance(BasisdataProvider basisdata) {
        if(instance == null){
            instance = new TestscenarioRepositoryImpl(basisdata);
        }
        return instance;
    }

    private TestscenarioRepositoryImpl(BasisdataProvider basisdata) {
        super(basisdata);
    }

    @Override
    public TestscenarioImpl opprettTestscenario(String testscenarioJson, Map<String, String> variables) {
        String unikTestscenarioId = UUID.randomUUID().toString();
        TestscenarioFraJsonMapper mapper = new TestscenarioFraJsonMapper(this);
        return mapper.lagTestscenarioFraJsonString(testscenarioJson, unikTestscenarioId, variables);
    }

    @Override
    public TestscenarioImpl oppdaterTestscenario(String id, String testscenarioJson, Map<String, String> variables) {
        TestscenarioFraJsonMapper mapper = new TestscenarioFraJsonMapper(this);
        return mapper.lagTestscenarioFraJsonString(testscenarioJson, id, variables);
    }

    @Override
    public TestscenarioImpl opprettTestscenario(Personopplysninger personopplysninger, InntektYtelseModell inntektytelseSøker,
                                                InntektYtelseModell inntektytelseAnnenpart, OrganisasjonModeller organisasjonsmodeller) {
        var testscenario = opprettTestscenarioFra(personopplysninger, inntektytelseSøker, inntektytelseAnnenpart, organisasjonsmodeller);
        this.indekser(testscenario);
        return testscenario;
    }

    @Override
    public TestscenarioImpl opprettTestscenario(Personopplysninger personopplysninger, InntektYtelseModell inntektytelseSøker,
                                                InntektYtelseModell inntektytelseAnnenpart, OrganisasjonModeller organisasjonsmodeller,
                                                PersonArbeidsgiver privatArbeidsgiver) {
        var testscenario = opprettTestscenarioFra(personopplysninger, inntektytelseSøker, inntektytelseAnnenpart, organisasjonsmodeller);

        var adresseIndeks = instance.getBasisdata().getAdresseIndeks();
        var lokalIdentIndeks = testscenario.getIdenter();
        var variabelContainer = testscenario.getVariabelContainer();
        var personIndeks = this.getPersonIndeks();
        privatArbeidsgiver.setIdenter(lokalIdentIndeks);
        privatArbeidsgiver.setVars(variabelContainer);
        privatArbeidsgiver.setAdresseIndeks(adresseIndeks);
        privatArbeidsgiver.setLokalIdentFraId();
        var fiktivtNavn = FiktivtNavn.getRandomFemaleName();
        privatArbeidsgiver.setFornavn(fiktivtNavn.getFornavn());
        privatArbeidsgiver.setEtternavn(fiktivtNavn.getEtternavn());
        personIndeks.leggTil(privatArbeidsgiver);

        this.indekser(testscenario);
        return testscenario;
    }


    private static TestscenarioImpl opprettTestscenarioFra(Personopplysninger personopplysninger, InntektYtelseModell søker,
                                                           InntektYtelseModell annenpart, OrganisasjonModeller organisasjonsmodeller) {
        var testscenarioImpl = new TestscenarioImpl("PLACEHOLDER", UUID.randomUUID().toString(), instance);
        var adresseIndeks = instance.getBasisdata().getAdresseIndeks();
        var lokalIdentIndeks = testscenarioImpl.getIdenter();
        var variabelContainer = testscenarioImpl.getVariabelContainer();

        personopplysninger.setIdenter(lokalIdentIndeks);
        personopplysninger.setVars(variabelContainer);
        personopplysninger.setAdresseIndeks(adresseIndeks);
        setLokalIdentForPersoner(personopplysninger);
        testscenarioImpl.setAdresseIndeks(adresseIndeks);
        testscenarioImpl.setPersonopplysninger(personopplysninger);
        testscenarioImpl.setSøkerInntektYtelse(søker);
        testscenarioImpl.setAnnenpartInntektYtelse(annenpart);

        if (organisasjonsmodeller != null && organisasjonsmodeller.getModeller() != null) {
            organisasjonsmodeller.getModeller().forEach(testscenarioImpl::leggTil);
        }
        return testscenarioImpl;
    }

    private static void setLokalIdentForPersoner(Personopplysninger personopplysninger) {
        personopplysninger.getSøker().setLokalIdentFraId();
        var annenPart = personopplysninger.getAnnenPart();
        if (annenPart != null) {
            annenPart.setLokalIdentFraId();
        }

        var relasjonBarn = Stream.concat(
                        personopplysninger.getFamilierelasjoner().stream(), personopplysninger.getFamilierelasjonerForAnnenPart().stream())
                .filter(f -> FamilierelasjonModell.Rolle.BARN.equals(f.getRolle()))
                .toList();

        for (var barnRelasjon: relasjonBarn) {
            barnRelasjon.getTil().setLokalIdentFraId();
        }
    }
}
