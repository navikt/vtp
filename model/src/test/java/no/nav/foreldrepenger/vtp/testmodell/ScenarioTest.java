package no.nav.foreldrepenger.vtp.testmodell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseType;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell.Rolle;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.GateadresseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Landkode;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

public class ScenarioTest {

    private static TestscenarioRepository testScenarioRepository;
    private static TestscenarioHenter testscenarioHenter;


    @BeforeAll
    public static void setup() throws IOException {
        testScenarioRepository = new DelegatingTestscenarioRepository(
                TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        testscenarioHenter = TestscenarioHenter.getInstance();

    }

    @Test
    public void testerInstansieringAvScenario() {
        var testscenarioObjekt = testscenarioHenter.hentScenario("1");
        var testscenarioJson = testscenarioObjekt == null ? "{}" : testscenarioHenter.toJson(testscenarioObjekt);
        var testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());
        assertEquals(1, testScenarioRepository.getTestscenarios().size());
        assertThat(testscenario.getId()).isNotNull();
        assertThat(testscenario.getTemplateNavn()).isEqualToIgnoringCase("1-for-enhetstester");

        var personopplysninger = testscenario.getPersonopplysninger();
        var søker = personopplysninger.getSøker();
        var annenpart = personopplysninger.getAnnenPart();
        assertThat(søker.getIdent()).isNotNull();
        assertThat(søker.getAktørIdent()).isNotNull();
        assertThat(annenpart.getIdent()).isNotNull();
        assertThat(annenpart.getAktørIdent()).isNotNull();
        avsjekkSpesifiktScenario(personopplysninger, søker);
        var fødselsdato = fødselsdatoBarn(testscenario);
        assertThat(fødselsdato).isNotNull();

        // Sjekker om personen er lagt inn i PersonIndeksen
        var søkerFraIndeks = testScenarioRepository.getPersonIndeks().finnByIdent(søker.getIdent());
        var annenpartFraIndeks = testScenarioRepository.getPersonIndeks().finnByIdent(annenpart.getIdent());
        assertThat(søkerFraIndeks).isEqualTo(søker);
        assertThat(annenpartFraIndeks).isEqualTo(annenpart);

        sjekkAdresseIndeks((TestscenarioImpl) testscenario);

        var søkerInntektYtelse = testscenario.getSøkerInntektYtelse();
        assertThat(søkerInntektYtelse).isNotNull();
        assertFalse(søkerInntektYtelse.getArenaModell().saker().isEmpty());
        assertTrue(søkerInntektYtelse.getInfotrygdModell().getYtelser().isEmpty());
        assertFalse(søkerInntektYtelse.getInfotrygdModell().getGrunnlag().isEmpty());
        assertFalse(søkerInntektYtelse.gettRexModell().foreldrepenger().isEmpty());
        assertTrue(søkerInntektYtelse.gettRexModell().svangerskapspenger().isEmpty());
        assertFalse(søkerInntektYtelse.gettRexModell().sykepenger().isEmpty());
        assertTrue(søkerInntektYtelse.gettRexModell().barnsykdom().isEmpty());
        assertTrue(søkerInntektYtelse.getInntektskomponentModell().inntektsperioder().isEmpty());
        assertTrue(søkerInntektYtelse.getInntektskomponentModell().frilansarbeidsforholdperioder().isEmpty());
        assertTrue(søkerInntektYtelse.getArbeidsforholdModell().getArbeidsforhold().isEmpty());
        assertTrue(søkerInntektYtelse.getSigrunModell().inntektsår().isEmpty());
        assertTrue(søkerInntektYtelse.getOmsorgspengerModell().getRammemeldinger().getAleneOmOmsorgen().isEmpty());
        assertTrue(søkerInntektYtelse.getOmsorgspengerModell().getRammemeldinger().getOverføringerGitt().isEmpty());
        assertTrue(søkerInntektYtelse.getOmsorgspengerModell().getRammemeldinger().getOverføringerFått().isEmpty());

        var annenpartInntektYtelse = testscenario.getAnnenpartInntektYtelse();
        assertThat(annenpartInntektYtelse).isNotNull();
        assertTrue(annenpartInntektYtelse.getArenaModell().saker().isEmpty());
        assertTrue(annenpartInntektYtelse.getInfotrygdModell().getYtelser().isEmpty());
        assertTrue(annenpartInntektYtelse.getInfotrygdModell().getGrunnlag().isEmpty());
        assertTrue(annenpartInntektYtelse.gettRexModell().foreldrepenger().isEmpty());
        assertTrue(annenpartInntektYtelse.gettRexModell().svangerskapspenger().isEmpty());
        assertTrue(annenpartInntektYtelse.gettRexModell().sykepenger().isEmpty());
        assertTrue(annenpartInntektYtelse.gettRexModell().barnsykdom().isEmpty());
        assertFalse(annenpartInntektYtelse.getInntektskomponentModell().inntektsperioder().isEmpty());
        assertFalse(annenpartInntektYtelse.getInntektskomponentModell().frilansarbeidsforholdperioder().isEmpty());
        assertFalse(annenpartInntektYtelse.getArbeidsforholdModell().getArbeidsforhold().isEmpty());
        assertFalse(annenpartInntektYtelse.getSigrunModell().inntektsår().isEmpty());
        assertTrue(annenpartInntektYtelse.getOmsorgspengerModell().getRammemeldinger().getAleneOmOmsorgen().isEmpty());
        assertTrue(annenpartInntektYtelse.getOmsorgspengerModell().getRammemeldinger().getOverføringerGitt().isEmpty());
        assertFalse(annenpartInntektYtelse.getOmsorgspengerModell().getRammemeldinger().getOverføringerFått().isEmpty());

        // Sjekker oppretting og sletting av scenario
        testScenarioRepository.slettScenario(testscenario.getId());
        assertEquals(0, testScenarioRepository.getTestscenarios().size());

    }

    private boolean avsjekkSpesifiktScenario(Personopplysninger pers, SøkerModell søker) {
        boolean avsjekketEttScenario;
        avsjekketEttScenario = true;

        assertThat(søker.getIdent()).isNotNull();

        var familierelasjon = pers.getFamilierelasjoner(Rolle.BARN).findFirst().get();
        assertThat(familierelasjon.getTil()).isNotNull();
        assertThat(familierelasjon.getTil()).isInstanceOf(BarnModell.class);
        // sjekk variable er satt inn
        assertThat(((BarnModell) familierelasjon.getTil()).getFornavn()).isEqualTo("Dole");

        assertThat(søker.getAdresser()).hasSize(1);
        // hentet fra adressekatalog ikke fra personopplysninger.json
        var bostedsAdresseOpt = søker.getAdresse(AdresseType.BOSTEDSADRESSE);
        assertThat(bostedsAdresseOpt).isPresent();
        var gateadresse = (GateadresseModell) bostedsAdresseOpt.get();
        assertThat(gateadresse.getGatenavn()).isEqualTo("Haugesund ally");
        assertThat(gateadresse.getFom()).isEqualTo(LocalDate.now().minusYears(1));
        return avsjekketEttScenario;
    }

    private void sjekkAdresseIndeks(TestscenarioImpl sc) {
        assertThat(sc.getAdresseIndeks()).isNotNull();
        assertThat(sc.getAdresseIndeks().finn(AdresseType.BOSTEDSADRESSE, Landkode.NOR)).isNotNull();
        assertThat(sc.getAdresseIndeks().finn(AdresseType.MIDLERTIDIG_POSTADRESSE, Landkode.NOR)).isNotNull();
        assertThat(sc.getAdresseIndeks().finn(AdresseType.MIDLERTIDIG_POSTADRESSE, Landkode.USA)).isNotNull();
        assertThat(sc.getAdresseIndeks().finn(AdresseType.POSTADRESSE, Landkode.NOR)).isNotNull();
    }

    private Optional<LocalDate> fødselsdatoBarn(Testscenario testscenario) {
        return testscenario.getPersonopplysninger().getFamilierelasjoner()
                .stream()
                .filter(modell -> modell.getTil() instanceof BarnModell)
                .map(modell -> ((BarnModell) modell.getTil()))
                .map(PersonModell::getFødselsdato)
                .findFirst();
    }
}
