package no.nav.foreldrepenger.fpmock.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import no.nav.foreldrepenger.vtp.kontrakter.DødfødselhendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.DødshendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.FødselshendelseDto;
import no.nav.foreldrepenger.vtp.server.api.pdl.PdlLeesahRestTjeneste;
import no.nav.foreldrepenger.vtp.testmodell.TestscenarioHenter;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.foreldrepenger.vtp.testmodell.util.VariabelContainer;
import no.nav.person.pdl.leesah.Endringstype;

@ExtendWith(MockitoExtension.class)
public class HendelseTest {

    private static TestscenarioRepository testScenarioRepository;
    private static TestscenarioHenter testscenarioHenter;

    @Spy
    static PdlLeesahRestTjeneste pdlLeesahRestTjeneste;

    @BeforeAll
    public static void setup() throws IOException {
        testScenarioRepository = new DelegatingTestscenarioRepository(
                TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        testscenarioHenter = TestscenarioHenter.getInstance();
        pdlLeesahRestTjeneste = new PdlLeesahRestTjeneste(testScenarioRepository);
    }

    @BeforeEach
    public void test() {
        doNothing().when(pdlLeesahRestTjeneste).sendHendelsePåKafka(Mockito.any());
    }

    @Test
    public void FødselshendelseTest() {
        var testscenarioObjekt = testscenarioHenter.hentScenario("1");
        var testscenarioJson = testscenarioObjekt == null ? "{}" : testscenarioHenter.toJson(testscenarioObjekt);
        var testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());
        var søkerIdent = testscenario.getPersonopplysninger().getSøker().getIdent();
        var annenpartIdent = testscenario.getPersonopplysninger().getAnnenPart().getIdent();

        var fødselshendelseDto = new FødselshendelseDto();
        var fødselsdato = LocalDate.now().plusDays(14);
        fødselshendelseDto.setFnrMor(søkerIdent);
        fødselshendelseDto.setFnrFar(annenpartIdent);
        fødselshendelseDto.setFødselsdato(fødselsdato);
        fødselshendelseDto.setEndringstype(Endringstype.OPPRETTET.name());

        assertEquals(testscenario.getPersonopplysninger().getFamilierelasjoner().size(), 3);
        assertEquals(testscenario.getPersonopplysninger().getFamilierelasjonerForAnnenPart().size(), 3);

        pdlLeesahRestTjeneste.leggTilHendelse(fødselshendelseDto);

        // Henter identen
        var barnIdent = hentUtIdentPåDetSisteBarneSomErRegistert(testscenario.getVariabelContainer());

        var personopplysninger = testScenarioRepository.getPersonIndeks().finnPersonopplysningerByIdent(barnIdent);
        var brukerModell = (BarnModell) testScenarioRepository.getPersonIndeks().finnByIdent(barnIdent);
        assertNotNull(personopplysninger);
        assertEquals(brukerModell.getFødselsdato(), fødselsdato);
        assertEquals(personopplysninger.getFamilierelasjoner().size(), 4);
        assertEquals(personopplysninger.getFamilierelasjonerForAnnenPart().size(), 4);
    }

    @Test
    public void DødshendelseTest() {
        var testscenarioObjekt = testscenarioHenter.hentScenario("1");
        var testscenarioJson = testscenarioObjekt == null ? "{}" : testscenarioHenter.toJson(testscenarioObjekt);
        var testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());
        var søkerIdent = testscenario.getPersonopplysninger().getSøker().getIdent();
        var dødsdato = LocalDate.now().minusDays(1);

        var dødshendelse = new DødshendelseDto();
        dødshendelse.setDoedsdato(dødsdato);
        dødshendelse.setEndringstype(Endringstype.OPPRETTET.name());
        dødshendelse.setFnr(søkerIdent);

        pdlLeesahRestTjeneste.leggTilHendelse(dødshendelse);

        verifiserDødsdatoErSattForFamilierelasjonsmodell(testscenario.getPersonopplysninger().getFamilierelasjoner(), søkerIdent);
        verifiserDødsdatoErSattForFamilierelasjonsmodell(testscenario.getPersonopplysninger().getFamilierelasjonerForAnnenPart(), søkerIdent);
        verifiserDødsdatoErSattForFamilierelasjonsmodell(testscenario.getPersonopplysninger().getFamilierelasjonerForBarnet(), søkerIdent);
    }


    @Test
    public void DødFødselshendelseTest() {
        var testscenarioObjekt = testscenarioHenter.hentScenario("1");
        var testscenarioJson = testscenarioObjekt == null ? "{}" : testscenarioHenter.toJson(testscenarioObjekt);
        var testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());
        var søkerIdent = testscenario.getPersonopplysninger().getSøker().getIdent();
        var dødsdato = LocalDate.now().minusDays(3);

        var dødfødselshendelse = new DødfødselhendelseDto();
        dødfødselshendelse.setDoedfoedselsdato(dødsdato);
        dødfødselshendelse.setEndringstype(Endringstype.OPPRETTET.name());
        dødfødselshendelse.setFnr(søkerIdent);

        assertEquals(testscenario.getPersonopplysninger().getFamilierelasjoner().size(), 3);
        assertEquals(testscenario.getPersonopplysninger().getFamilierelasjonerForAnnenPart().size(), 3);

        pdlLeesahRestTjeneste.leggTilHendelse(dødfødselshendelse);

        // Verifiserer riktig format på identen til barn
        var barnIdent = hentUtIdentPåDetSisteBarneSomErRegistert(testscenario.getVariabelContainer());
        assertEquals(barnIdent, dødsdato.format(DateTimeFormatter.ofPattern("ddMMyy")) + "00001");

        var personopplysninger = testScenarioRepository.getPersonIndeks().finnPersonopplysningerByIdent(barnIdent);
        var brukerModell = (BarnModell) testScenarioRepository.getPersonIndeks().finnByIdent(barnIdent);
        assertNotNull(personopplysninger);
        assertEquals(brukerModell.getFødselsdato(), dødsdato);
        assertEquals(personopplysninger.getFamilierelasjoner().size(), 4);
        assertEquals(personopplysninger.getFamilierelasjonerForAnnenPart().size(), 4);
    }


    private void verifiserDødsdatoErSattForFamilierelasjonsmodell(Collection<FamilierelasjonModell> familierelasjonModell,
                                                              String fnr) {
        List<PersonModell> personmodeller = familierelasjonModell.stream()
                .filter(fr -> fr.getTil().getIdent().equalsIgnoreCase(fnr))
                .map(fr -> (PersonModell) fr.getTil())
                .collect(Collectors.toList());

        for (PersonModell personModell: personmodeller) {
            assertNotNull(personModell.getDødsdato());
        }
    }

    private String hentUtIdentPåDetSisteBarneSomErRegistert(VariabelContainer variabelContainer) {
        var i = 0;
        while (variabelContainer.getVar("barn" + (i + 1)) != null) {
            i++;
        }
        return variabelContainer.getVar("barn" + i);
    }
}