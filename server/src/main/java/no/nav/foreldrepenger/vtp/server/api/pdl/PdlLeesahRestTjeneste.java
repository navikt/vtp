package no.nav.foreldrepenger.vtp.server.api.pdl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.avro.generic.GenericRecordBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.kontrakter.DødfødselhendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.DødshendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.FamilierelasjonHendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.FødselshendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.PersonhendelseDto;
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
        return Response.status(201).entity(String.format("{\"success\": \"Personhendelse opprettet\"}")).build();
    }

    private void produserFødselshendelse(FødselshendelseDto fødselshendelseDto) {
        GenericRecordBuilder personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        personhendelse.set("hendelseId", UUID.randomUUID().toString());
        personhendelse.set("personidenter", List.of(fødselshendelseDto.getFnrBarn(), testscenarioRepository.getPersonIndeks().finnByIdent(fødselshendelseDto.getFnrBarn()).getAktørIdent()));
        personhendelse.set("master", "Freg");
        personhendelse.set("opprettet", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set("opplysningstype", "FOEDSEL_V1");
        personhendelse.set("endringstype", Endringstype.valueOf(fødselshendelseDto.getEndringstype()));
        if (fødselshendelseDto.getTidligereHendelseId() != null) {
            personhendelse.set("tidligereHendelseId", fødselshendelseDto.getTidligereHendelseId());
        }

        if (!Endringstype.ANNULLERT.toString().equals(fødselshendelseDto.getEndringstype())) {
            GenericRecordBuilder fødsel = new GenericRecordBuilder(Foedsel.SCHEMA$);
            fødsel.set("foedselsdato", oversettLocalDateTilAvroFormat(fødselshendelseDto.getFødselsdato()));
            personhendelse.set("foedsel", fødsel.build());
        }

        localKafkaProducer.sendMelding("aapen-person-pdl-leesah-v1-vtp", personhendelse.build());
    }

    private void produserDødshendelse(DødshendelseDto dødshendelseDto) {
        GenericRecordBuilder personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        personhendelse.set("hendelseId", UUID.randomUUID().toString());
        personhendelse.set("personidenter", List.of(dødshendelseDto.getFnr(), testscenarioRepository.getPersonIndeks().finnByIdent(dødshendelseDto.getFnr()).getAktørIdent()));
        personhendelse.set("master", "Freg");
        personhendelse.set("opprettet", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set("opplysningstype", "DOEDSFALL_V1");
        personhendelse.set("endringstype", Endringstype.valueOf(dødshendelseDto.getEndringstype()));
        if (dødshendelseDto.getTidligereHendelseId() != null) {
            personhendelse.set("tidligereHendelseId", dødshendelseDto.getTidligereHendelseId());
        }

        if (!Endringstype.ANNULLERT.toString().equals(dødshendelseDto.getEndringstype())) {
            GenericRecordBuilder dødsfall = new GenericRecordBuilder(Doedsfall.SCHEMA$);
            dødsfall.set("doedsdato", oversettLocalDateTilAvroFormat(dødshendelseDto.getDoedsdato()));
            personhendelse.set("doedsfall", dødsfall.build());
        }

        localKafkaProducer.sendMelding("aapen-person-pdl-leesah-v1-vtp", personhendelse.build());
    }

    private void produserDødfødselshendelse(DødfødselhendelseDto dødfødselhendelseDto) {
        GenericRecordBuilder personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        personhendelse.set("hendelseId", UUID.randomUUID().toString());
        personhendelse.set("personidenter", List.of(dødfødselhendelseDto.getFnr(), testscenarioRepository.getPersonIndeks().finnByIdent(dødfødselhendelseDto.getFnr()).getAktørIdent()));
        personhendelse.set("master", "Freg");
        personhendelse.set("opprettet", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set("opplysningstype", "DOEDFOEDT_BARN_V1");
        personhendelse.set("endringstype", Endringstype.valueOf(dødfødselhendelseDto.getEndringstype()));
        if (dødfødselhendelseDto.getTidligereHendelseId() != null) {
            personhendelse.set("tidligereHendelseId", dødfødselhendelseDto.getTidligereHendelseId());
        }

        if (!Endringstype.ANNULLERT.toString().equals(dødfødselhendelseDto.getEndringstype())) {
            GenericRecordBuilder dødfødtBarn = new GenericRecordBuilder(DoedfoedtBarn.SCHEMA$);
            dødfødtBarn.set("dato", oversettLocalDateTilAvroFormat(dødfødselhendelseDto.getDoedfoedselsdato()));
            personhendelse.set("doedfoedtBarn", dødfødtBarn.build());
        }

        localKafkaProducer.sendMelding("aapen-person-pdl-leesah-v1-vtp", personhendelse.build());
    }

    private void produserFamilierelasjonHendelse(FamilierelasjonHendelseDto familierelasjonHendelseDto) {
        GenericRecordBuilder personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        personhendelse.set("hendelseId", UUID.randomUUID().toString());
        personhendelse.set("personidenter", List.of(familierelasjonHendelseDto.getFnr(), testscenarioRepository.getPersonIndeks().finnByIdent(familierelasjonHendelseDto.getFnr()).getAktørIdent()));
        personhendelse.set("master", "Freg");
        personhendelse.set("opprettet", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set("opplysningstype", "FAMILIERELASJON_V1");
        personhendelse.set("endringstype", Endringstype.valueOf(familierelasjonHendelseDto.getEndringstype()));

        if (!Endringstype.ANNULLERT.toString().equals(familierelasjonHendelseDto.getEndringstype())) {
            GenericRecordBuilder familierelasjon = new GenericRecordBuilder(Familierelasjon.SCHEMA$);
            familierelasjon.set("relatertPersonsIdent", familierelasjonHendelseDto.getRelatertPersonsFnr());
            familierelasjon.set("relatertPersonsRolle", familierelasjonHendelseDto.getRelatertPersonsRolle());
            familierelasjon.set("minRolleForPerson", familierelasjonHendelseDto.getMinRolleForPerson());
            personhendelse.set("familierelasjon", familierelasjon.build());
        }

        localKafkaProducer.sendMelding("aapen-person-pdl-leesah-v1-vtp", personhendelse.build());
    }

    private int oversettLocalDateTilAvroFormat(LocalDate localDate) {
        // Avro krever at dato angis som antall dager siden epoch som int
        return Long.valueOf(localDate.toEpochDay()).intValue();
    }
}
