package no.nav.foreldrepenger.fpmock.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import no.nav.foreldrepenger.vtp.kontrakter.hendelser.DødfødselhendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.hendelser.DødshendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.hendelser.FødselshendelseDto;
import no.nav.foreldrepenger.vtp.server.api.pdl.PdlLeesahRestTjeneste;
import no.nav.vtp.PersonBuilder;
import no.nav.vtp.person.PersonRepository;
import no.nav.person.pdl.leesah.Endringstype;

@Disabled
@ExtendWith(MockitoExtension.class)
class HendelseTest {

    @Spy
    PdlLeesahRestTjeneste pdlLeesahRestTjeneste = new PdlLeesahRestTjeneste();

    @BeforeEach
    void setupMock() {
        doNothing().when(pdlLeesahRestTjeneste).sendHendelsePåKafka(Mockito.any());
    }

    @Test
    void FødselshendelseTest() {
        var scenario = PersonBuilder.lagPersoner();
        PersonRepository.leggTilPersoner(scenario.allePersoner());
        var søkerIdent = scenario.søkerIdent();
        var annenpartIdent = scenario.annenPartIdent();
        var fødselsdato = LocalDate.now().plusDays(14);

        var søkerFør = PersonRepository.hentPerson(søkerIdent);
        var annenpartFør = PersonRepository.hentPerson(annenpartIdent);

        var barnRelasjonerSøkerFør = søkerFør.personopplysninger().familierelasjoner().size();
        var barnRelasjonerAnnenpartFør = annenpartFør.personopplysninger().familierelasjoner().size();

        var fødselshendelseDto = new FødselshendelseDto(Endringstype.OPPRETTET.name(), null, søkerIdent,
                annenpartIdent, null, fødselsdato);

        pdlLeesahRestTjeneste.leggTilHendelse(fødselshendelseDto, false);

        var søkerEtter = PersonRepository.hentPerson(søkerIdent);
        var annenpartEtter = PersonRepository.hentPerson(annenpartIdent);

        assertEquals(barnRelasjonerSøkerFør + 1, søkerEtter.personopplysninger().familierelasjoner().size());
        assertEquals(barnRelasjonerAnnenpartFør + 1, annenpartEtter.personopplysninger().familierelasjoner().size());
    }

    @Test
    void DødshendelseTest() {
        var scenario = PersonBuilder.lagPersoner();
        PersonRepository.leggTilPersoner(scenario.allePersoner());
        var søkerIdent = scenario.søkerIdent();
        var dødsdato = LocalDate.now().minusDays(1);

        var dødshendelse = new DødshendelseDto(Endringstype.OPPRETTET.name(), null, søkerIdent, dødsdato);

        pdlLeesahRestTjeneste.leggTilHendelse(dødshendelse, false);

        var person = PersonRepository.hentPerson(søkerIdent);
        assertNotNull(person.personopplysninger().dødsdato());
        assertEquals(dødsdato, person.personopplysninger().dødsdato());
    }


    @Test
    void DødFødselshendelseTest() {
        var scenario = PersonBuilder.lagPersoner();
        PersonRepository.leggTilPersoner(scenario.allePersoner());
        var søkerIdent = scenario.søkerIdent();
        var dødsdato = LocalDate.now().minusDays(3);

        var søkerFør = PersonRepository.hentPerson(søkerIdent);
        var barnRelasjonerFør = søkerFør.personopplysninger().familierelasjoner().size();

        var dødfødselshendelse =
                new DødfødselhendelseDto(Endringstype.OPPRETTET.name(), null, søkerIdent, dødsdato);

        pdlLeesahRestTjeneste.leggTilHendelse(dødfødselshendelse, false);

        var søkerEtter = PersonRepository.hentPerson(søkerIdent);
        assertEquals(barnRelasjonerFør + 1, søkerEtter.personopplysninger().familierelasjoner().size());

        // Verifiserer riktig format på identen til barnet
        var barnIdent = dødsdato.format(DateTimeFormatter.ofPattern("ddMMyy")) + "00001";
        var barnet = PersonRepository.hentPerson(barnIdent);
        assertNotNull(barnet);
        assertEquals(dødsdato, barnet.personopplysninger().fødselsdato());
    }
}
