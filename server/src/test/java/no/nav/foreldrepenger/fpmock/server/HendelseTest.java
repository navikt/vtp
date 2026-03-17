package no.nav.foreldrepenger.fpmock.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import no.nav.foreldrepenger.vtp.kontrakter.DødfødselhendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.DødshendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.FødselshendelseDto;
import no.nav.foreldrepenger.vtp.server.api.pdl.PdlLeesahRestTjeneste;
import no.nav.vtp.PersonBuilder;
import no.nav.vtp.person.PersonRepository;
import no.nav.person.pdl.leesah.Endringstype;

@Disabled
@ExtendWith(MockitoExtension.class)
class HendelseTest {

    private static PersonRepository personRepository;

    @Spy
    static PdlLeesahRestTjeneste pdlLeesahRestTjeneste;

    @BeforeAll
    static void setup() {
        personRepository = new PersonRepository();
        personRepository.leggTilPersoner(PersonBuilder.lagPersoner());
        pdlLeesahRestTjeneste = new PdlLeesahRestTjeneste();
    }

    @BeforeEach
    void test() {
        doNothing().when(pdlLeesahRestTjeneste).sendHendelsePåKafka(Mockito.any());
    }

    @Test
    void FødselshendelseTest() {
        var søkerIdent = PersonBuilder.SØKER_IDENT;
        var annenpartIdent = PersonBuilder.ANNEN_PART_IDENT;
        var fødselsdato = LocalDate.now().plusDays(14);

        var søkerFør = personRepository.hentPerson(søkerIdent);
        var annenpartFør = personRepository.hentPerson(annenpartIdent);

        var barnRelasjonerSøkerFør = søkerFør.personopplysninger().familierelasjoner().size();
        var barnRelasjonerAnnenpartFør = annenpartFør.personopplysninger().familierelasjoner().size();

        var fødselshendelseDto = new FødselshendelseDto(Endringstype.OPPRETTET.name(), null, søkerIdent,
                annenpartIdent, null, fødselsdato);

        pdlLeesahRestTjeneste.leggTilHendelse(fødselshendelseDto, false);

        var søkerEtter = personRepository.hentPerson(søkerIdent);
        var annenpartEtter = personRepository.hentPerson(annenpartIdent);

        assertEquals(barnRelasjonerSøkerFør + 1, søkerEtter.personopplysninger().familierelasjoner().size());
        assertEquals(barnRelasjonerAnnenpartFør + 1, annenpartEtter.personopplysninger().familierelasjoner().size());
    }

    @Test
    void DødshendelseTest() {
        var søkerIdent = PersonBuilder.SØKER_IDENT;
        var dødsdato = LocalDate.now().minusDays(1);

        var dødshendelse = new DødshendelseDto(Endringstype.OPPRETTET.name(), null, søkerIdent, dødsdato);

        pdlLeesahRestTjeneste.leggTilHendelse(dødshendelse, false);

        var person = personRepository.hentPerson(søkerIdent);
        assertNotNull(person.personopplysninger().dødsdato());
        assertEquals(dødsdato, person.personopplysninger().dødsdato());
    }


    @Test
    void DødFødselshendelseTest() {
        var søkerIdent = PersonBuilder.SØKER_IDENT;
        var dødsdato = LocalDate.now().minusDays(3);

        var søkerFør = personRepository.hentPerson(søkerIdent);
        var barnRelasjonerFør = søkerFør.personopplysninger().familierelasjoner().size();

        var dødfødselshendelse =
                new DødfødselhendelseDto(Endringstype.OPPRETTET.name(), null, søkerIdent, dødsdato);

        pdlLeesahRestTjeneste.leggTilHendelse(dødfødselshendelse, false);

        var søkerEtter = personRepository.hentPerson(søkerIdent);
        assertEquals(barnRelasjonerFør + 1, søkerEtter.personopplysninger().familierelasjoner().size());

        // Verifiserer riktig format på identen til barnet
        var barnIdent = dødsdato.format(DateTimeFormatter.ofPattern("ddMMyy")) + "00001";
        var barnet = personRepository.hentPerson(barnIdent);
        assertNotNull(barnet);
        assertEquals(dødsdato, barnet.personopplysninger().fødselsdato());
    }
}
