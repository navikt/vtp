package no.nav.foreldrepenger.fpmock.server;

import static org.mockito.Mockito.doNothing;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
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
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
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
        fødselshendelseDto.setFnrMor(søkerIdent);
        fødselshendelseDto.setFnrFar(annenpartIdent);
        fødselshendelseDto.setFødselsdato(LocalDate.now().plusDays(14));
        fødselshendelseDto.setEndringstype(Endringstype.OPPRETTET.name());

        Assert.assertEquals(testscenario.getPersonopplysninger().getFamilierelasjoner().size(), 3);
        Assert.assertEquals(testscenario.getPersonopplysninger().getFamilierelasjonerForAnnenPart().size(), 3);

        pdlLeesahRestTjeneste.leggTilHendelse(fødselshendelseDto);

        Assert.assertEquals(testscenario.getPersonopplysninger().getFamilierelasjoner().size(), 4);
        Assert.assertEquals(testscenario.getPersonopplysninger().getFamilierelasjonerForAnnenPart().size(), 4);
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

        Assert.assertEquals(testscenario.getPersonopplysninger().getFamilierelasjoner().size(), 3);
        Assert.assertEquals(testscenario.getPersonopplysninger().getFamilierelasjonerForAnnenPart().size(), 3);

        pdlLeesahRestTjeneste.leggTilHendelse(dødfødselshendelse);

        Assert.assertEquals(testscenario.getPersonopplysninger().getFamilierelasjoner().size(), 4);
        Assert.assertEquals(testscenario.getPersonopplysninger().getFamilierelasjonerForAnnenPart().size(), 4);
    }


        private void verifiserDødsdatoErSattForFamilierelasjonsmodell(Collection<FamilierelasjonModell> familierelasjonModell,
                                                                  String fnr) {
        List<PersonModell> personmodeller = familierelasjonModell.stream()
                .filter(fr -> fr.getTil().getIdent().equalsIgnoreCase(fnr))
                .map(fr -> (PersonModell) fr.getTil())
                .collect(Collectors.toList());

        for (PersonModell personModell: personmodeller) {
            Assert.assertNotNull(personModell.getDødsdato());
        }
    }
}
