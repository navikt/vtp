package no.nav.foreldrepenger.vtp.server.api.pdl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.kontrakter.DødfødselhendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.DødshendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.FamilierelasjonHendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.FødselshendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.PersonhendelseDto;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.person.pdl.leesah.Endringstype;
import no.nav.person.pdl.leesah.Personhendelse;
import no.nav.person.pdl.leesah.doedfoedtbarn.DoedfoedtBarn;
import no.nav.person.pdl.leesah.doedsfall.Doedsfall;
import no.nav.person.pdl.leesah.familierelasjon.Familierelasjon;
import no.nav.person.pdl.leesah.foedsel.Foedsel;

@Api(tags = "Legge hendelser på PDL topic")
@Path("/api/pdl/leesah")
public class PdlLeesahRestTjeneste {

    @Context
    private LocalKafkaProducer localKafkaProducer;

    @Context
    private TestscenarioRepository testscenarioRepository;

    public PdlLeesahRestTjeneste() {
    }

    public PdlLeesahRestTjeneste(TestscenarioRepository testscenarioRepository) {
        this.testscenarioRepository = testscenarioRepository;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Legg til hendelse")
    public Response leggTilHendelse(PersonhendelseDto personhendelseDto) {
        try {
            if (personhendelseDto instanceof FødselshendelseDto) {
                produserFødselshendelse((FødselshendelseDto) personhendelseDto);
            } else if (personhendelseDto instanceof DødshendelseDto) {
                produserDødshendelse((DødshendelseDto) personhendelseDto);
            } else if (personhendelseDto instanceof DødfødselhendelseDto) {
                produserDødfødselshendelse((DødfødselhendelseDto) personhendelseDto);
            } else if (personhendelseDto instanceof FamilierelasjonHendelseDto) {
                produserFamilierelasjonHendelse((FamilierelasjonHendelseDto) personhendelseDto);
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"Ukjent hendelsestype\"}").build();
            }

        } catch (RuntimeException re) {
            re.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(String.format("{\"error\": \"%s\"}",re.getMessage())).build();
        }
        return Response.status(201).entity("{\"success\": \"Personhendelse opprettet\"}").build();
    }

    private void produserFødselshendelse(FødselshendelseDto fødselshendelseDto) {
        var barnIdent = registererNyttBarnPåForeldre(fødselshendelseDto);
        GenericRecordBuilder personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        personhendelse.set("hendelseId", UUID.randomUUID().toString());
        personhendelse.set("personidenter", List.of(barnIdent, testscenarioRepository.getPersonIndeks().finnByIdent(barnIdent).getAktørIdent()));
        personhendelse.set("master", "Freg");
        personhendelse.set("opprettet", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set("opplysningstype", "FOEDSEL_V1");
        personhendelse.set("endringstype", Endringstype.valueOf(fødselshendelseDto.endringstype()));
        if (fødselshendelseDto.tidligereHendelseId() != null) {
            personhendelse.set("tidligereHendelseId", fødselshendelseDto.tidligereHendelseId());
        }

        if (!Endringstype.ANNULLERT.toString().equals(fødselshendelseDto.endringstype())) {
            GenericRecordBuilder fødsel = new GenericRecordBuilder(Foedsel.SCHEMA$);
            fødsel.set("foedselsdato", oversettLocalDateTilAvroFormat(fødselshendelseDto.fødselsdato()));
            personhendelse.set("foedsel", fødsel.build());
        }

        sendHendelsePåKafka(personhendelse.build());
    }

    public void sendHendelsePåKafka(GenericData.Record record) {
        localKafkaProducer.sendMelding("aapen-person-pdl-leesah-v1-vtp", record);
    }

    private void produserDødshendelse(DødshendelseDto dødshendelseDto) {
        registrerDødshendelse(dødshendelseDto);
        GenericRecordBuilder personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        personhendelse.set("hendelseId", UUID.randomUUID().toString());
        personhendelse.set("personidenter", List.of(dødshendelseDto.fnr(), testscenarioRepository.getPersonIndeks().finnByIdent(dødshendelseDto.fnr()).getAktørIdent()));
        personhendelse.set("master", "Freg");
        personhendelse.set("opprettet", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set("opplysningstype", "DOEDSFALL_V1");
        personhendelse.set("endringstype", Endringstype.valueOf(dødshendelseDto.endringstype()));
        if (dødshendelseDto.tidligereHendelseId() != null) {
            personhendelse.set("tidligereHendelseId", dødshendelseDto.tidligereHendelseId());
        }

        if (!Endringstype.ANNULLERT.toString().equals(dødshendelseDto.endringstype())) {
            GenericRecordBuilder dødsfall = new GenericRecordBuilder(Doedsfall.SCHEMA$);
            dødsfall.set("doedsdato", oversettLocalDateTilAvroFormat(dødshendelseDto.doedsdato()));
            personhendelse.set("doedsfall", dødsfall.build());
        }

        sendHendelsePåKafka(personhendelse.build());
    }

    private void produserDødfødselshendelse(DødfødselhendelseDto dødfødselhendelseDto) {
        registererDødfødselsHendelse(dødfødselhendelseDto);
        GenericRecordBuilder personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        personhendelse.set("hendelseId", UUID.randomUUID().toString());
        personhendelse.set("personidenter", List.of(dødfødselhendelseDto.fnr(), testscenarioRepository.getPersonIndeks().finnByIdent(dødfødselhendelseDto.fnr()).getAktørIdent()));
        personhendelse.set("master", "Freg");
        personhendelse.set("opprettet", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set("opplysningstype", "DOEDFOEDT_BARN_V1");
        personhendelse.set("endringstype", Endringstype.valueOf(dødfødselhendelseDto.endringstype()));
        if (dødfødselhendelseDto.tidligereHendelseId() != null) {
            personhendelse.set("tidligereHendelseId", dødfødselhendelseDto.tidligereHendelseId());
        }

        if (!Endringstype.ANNULLERT.toString().equals(dødfødselhendelseDto.endringstype())) {
            GenericRecordBuilder dødfødtBarn = new GenericRecordBuilder(DoedfoedtBarn.SCHEMA$);
            dødfødtBarn.set("dato", oversettLocalDateTilAvroFormat(dødfødselhendelseDto.doedfoedselsdato()));
            personhendelse.set("doedfoedtBarn", dødfødtBarn.build());
        }

        sendHendelsePåKafka(personhendelse.build());
    }

    private void produserFamilierelasjonHendelse(FamilierelasjonHendelseDto familierelasjonHendelseDto) {
        GenericRecordBuilder personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        personhendelse.set("hendelseId", UUID.randomUUID().toString());
        personhendelse.set("personidenter", List.of(familierelasjonHendelseDto.fnr(), testscenarioRepository.getPersonIndeks().finnByIdent(familierelasjonHendelseDto.fnr()).getAktørIdent()));
        personhendelse.set("master", "Freg");
        personhendelse.set("opprettet", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set("opplysningstype", "FAMILIERELASJON_V1");
        personhendelse.set("endringstype", Endringstype.valueOf(familierelasjonHendelseDto.endringstype()));

        if (!Endringstype.ANNULLERT.toString().equals(familierelasjonHendelseDto.endringstype())) {
            GenericRecordBuilder familierelasjon = new GenericRecordBuilder(Familierelasjon.SCHEMA$);
            familierelasjon.set("relatertPersonsIdent", familierelasjonHendelseDto.relatertPersonsFnr());
            familierelasjon.set("relatertPersonsRolle", familierelasjonHendelseDto.relatertPersonsRolle());
            familierelasjon.set("minRolleForPerson", familierelasjonHendelseDto.minRolleForPerson());
            personhendelse.set("familierelasjon", familierelasjon.build());
        }

        sendHendelsePåKafka(personhendelse.build());
    }

    private int oversettLocalDateTilAvroFormat(LocalDate localDate) {
        // Avro krever at dato angis som antall dager siden epoch som int
        return Long.valueOf(localDate.toEpochDay()).intValue();
    }

    private String registererNyttBarnPåForeldre(FødselshendelseDto fødselshendelseDto) {
        var personIndeks = testscenarioRepository.getPersonIndeks();
        Personopplysninger personopplysninger;
        personopplysninger = personIndeks.finnPersonopplysningerByIdent(fødselshendelseDto.fnrMor());
        if (personopplysninger == null) {
            personopplysninger = personIndeks.finnPersonopplysningerByIdent(fødselshendelseDto.fnrFar());
        }

        BarnModell barnModell = new BarnModell("Tester Testersonsdotter", fødselshendelseDto.fødselsdato());
        var barnIdent = personopplysninger.leggTilBarn(barnModell);

        indekserFamilierelasjonerIPersonIndeksen(personIndeks, personopplysninger);

        return barnIdent;
    }

    private String registererDødfødselsHendelse(DødfødselhendelseDto dødfødselhendelseDto) {
        var personIndeks = testscenarioRepository.getPersonIndeks();
        Personopplysninger personopplysninger;
        personopplysninger = personIndeks.finnPersonopplysningerByIdent(dødfødselhendelseDto.fnr());

        BarnModell barnModell = new BarnModell("Tester Testersonsdotter", dødfødselhendelseDto.doedfoedselsdato());
        var barnIdent= personopplysninger.leggTilDødfødsel(barnModell);

        indekserFamilierelasjonerIPersonIndeksen(personIndeks, personopplysninger);
        return barnIdent;
    }

    private void registrerDødshendelse(DødshendelseDto dødshendelseDto) {
        var personIndeks = testscenarioRepository.getPersonIndeks();
        Personopplysninger personopplysninger;
        personopplysninger = personIndeks.finnPersonopplysningerByIdent(dødshendelseDto.fnr());
        setDødsdatoerIIndeksene(personopplysninger, dødshendelseDto);
    }

    private void setDødsdatoerIIndeksene(Personopplysninger personopplysninger, DødshendelseDto dødshendelseDto){
        if (dødshendelseDto.fnr().equalsIgnoreCase(personopplysninger.getSøker().getIdent())) {
            personopplysninger.getSøker().setDødsdato(dødshendelseDto.doedsdato());
        } else if (dødshendelseDto.fnr().equalsIgnoreCase(personopplysninger.getAnnenPart().getIdent())) {
            personopplysninger.getAnnenPart().setDødsdato(dødshendelseDto.doedsdato());
        }
        setDødsdatoForFamilirelasjoner(personopplysninger.getFamilierelasjoner(), dødshendelseDto);
        setDødsdatoForFamilirelasjoner(personopplysninger.getFamilierelasjonerForAnnenPart(), dødshendelseDto);
        setDødsdatoForFamilirelasjoner(personopplysninger.getFamilierelasjonerForBarnet(), dødshendelseDto);
    }

    private void setDødsdatoForFamilirelasjoner(Collection<FamilierelasjonModell> familierelasjonModell, DødshendelseDto dødshendelseDto) {
        familierelasjonModell.stream()
                .filter(fr -> fr.getTil().getIdent().equalsIgnoreCase(dødshendelseDto.fnr()))
                .map(fr -> (PersonModell) fr.getTil())
                .forEach(personModell -> personModell.setDødsdato(dødshendelseDto.doedsdato()));
    }

    private void indekserFamilierelasjonerIPersonIndeksen(PersonIndeks personIndeks, Personopplysninger personopplysninger) {
        personIndeks.indekserFamilierelasjonBrukere(personopplysninger.getSøker(), personopplysninger.getFamilierelasjoner());
        personIndeks.indekserFamilierelasjonBrukere(personopplysninger.getSøker(), personopplysninger.getFamilierelasjonerForAnnenPart());
        personIndeks.indekserPersonopplysningerByIdent(personopplysninger);
    }
}
