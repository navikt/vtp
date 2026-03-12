package no.nav.foreldrepenger.vtp.server.api.pdl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.kontrakter.DødfødselhendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.DødshendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.FamilierelasjonHendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.ForelderBarnRelasjonHendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.FødselshendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.PersonhendelseDto;
import no.nav.foreldrepenger.vtp.testmodell.identer.FiktiveFnr;
import no.nav.person.pdl.leesah.Endringstype;
import no.nav.person.pdl.leesah.Personhendelse;
import no.nav.person.pdl.leesah.doedfoedtbarn.DoedfoedtBarn;
import no.nav.person.pdl.leesah.doedsfall.Doedsfall;
import no.nav.person.pdl.leesah.familierelasjon.Familierelasjon;
import no.nav.person.pdl.leesah.foedselsdato.Foedselsdato;
import no.nav.person.pdl.leesah.forelderbarnrelasjon.ForelderBarnRelasjon;
import no.nav.vtp.Person;
import no.nav.vtp.PersonRepository;
import no.nav.vtp.ident.PersonIdent;
import no.nav.vtp.personopplysninger.Kjønn;
import no.nav.vtp.personopplysninger.Navn;
import no.nav.vtp.personopplysninger.Rolle;
import no.nav.vtp.personopplysninger.Språk;

@Path("/api/pdl/leesah")
public class PdlLeesahRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(PdlLeesahRestTjeneste.class);
    private static final String HENDELSE_ID = "hendelseId";
    private static final String PERSONIDENTER = "personidenter";
    private static final String MASTER_FIELD = "master";
    private static final String OPPRETTET = "opprettet";
    private static final String OPPLYSNINGSTYPE = "opplysningstype";
    private static final String ENDRINGSTYPE = "endringstype";
    private static final String TIDLIGERE_HENDELSE_ID = "tidligereHendelseId";
    private static final String LEESAH_TOPIC = Optional.ofNullable(System.getenv("KAFKA_PDL_LEESAH_TOPIC")).orElse("aapen-person-pdl-leesah-v1-vtp");

    @Context
    private LocalKafkaProducer localKafkaProducer;

    @Context
    private PersonRepository personRepository;

    public PdlLeesahRestTjeneste() {
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response leggTilHendelse(PersonhendelseDto personhendelseDto,
                                    @QueryParam("publiserForelderBarnRelasjonMedFoedselshendelser") boolean publiserForelderBarnRelasjonMedFoedselshendelser) {
        try {
            switch (personhendelseDto) {
                case FødselshendelseDto fødselshendelseDto ->
                        produserFødselshendelse(fødselshendelseDto, publiserForelderBarnRelasjonMedFoedselshendelser);
                case DødshendelseDto dødshendelseDto -> produserDødshendelse(dødshendelseDto);
                case DødfødselhendelseDto dødfødselhendelseDto -> produserDødfødselshendelse(dødfødselhendelseDto);
                case FamilierelasjonHendelseDto familierelasjonHendelseDto ->
                        produserFamilierelasjonHendelse(familierelasjonHendelseDto);
                case ForelderBarnRelasjonHendelseDto forelderBarnRelasjonHendelseDto ->
                        produserForelderBarnRelasjonHendelse(forelderBarnRelasjonHendelseDto);
                case null, default -> {
                    return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"Ukjent hendelsestype\"}").build();
                }
            }

        } catch (RuntimeException e) {
            LOG.warn("Noe gikk galt når vi prøvde å legge til hendels på PDL topic", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(String.format("{\"error\": \"%s\"}", e.getMessage())).build();
        }
        return Response.status(201).entity("{\"success\": \"Personhendelse opprettet\"}").build();
    }

    private void produserForelderBarnRelasjonHendelse(ForelderBarnRelasjonHendelseDto forelderBarnRelasjonHendelseDto) {
        if (forelderBarnRelasjonHendelseDto == null) {
            LOG.warn("ForelderBarnRelasjonHendelseDto er null, kan ikke produsere hendelse");
            return;
        }

        GenericRecordBuilder personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        personhendelse.set(HENDELSE_ID, UUID.randomUUID().toString());
        personhendelse.set(MASTER_FIELD, "Freg");
        personhendelse.set(OPPRETTET, LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set(OPPLYSNINGSTYPE, "FORELDERBARNRELASJON_V1");
        personhendelse.set(ENDRINGSTYPE, Endringstype.valueOf(forelderBarnRelasjonHendelseDto.endringstype()));

        var personIdent = new PersonIdent(forelderBarnRelasjonHendelseDto.fnr());
        personhendelse.set(PERSONIDENTER, List.of(personIdent.fnr(), personIdent.aktørId()));

        if (!Endringstype.ANNULLERT.toString().equals(forelderBarnRelasjonHendelseDto.endringstype())) {
            GenericRecordBuilder forelderBarnRelasjon = new GenericRecordBuilder(ForelderBarnRelasjon.SCHEMA$);
            String relatertPersonsFnr = forelderBarnRelasjonHendelseDto.relatertPersonsFnr();
            forelderBarnRelasjon.set("relatertPersonsIdent", relatertPersonsFnr);
            forelderBarnRelasjon.set("relatertPersonsRolle", forelderBarnRelasjonHendelseDto.relatertPersonsRolle());
            forelderBarnRelasjon.set("minRolleForPerson", forelderBarnRelasjonHendelseDto.minRolleForPerson());
            personhendelse.set("forelderBarnRelasjon", forelderBarnRelasjon.build());
        }

        LOG.info(
                "Publiserer FORELDERBARNRELASJON_V1 hendelse med minRolle: {}, minFnr: {}, relatertPersonRolle: {}, relatertPersonFnr: {}",
                forelderBarnRelasjonHendelseDto.minRolleForPerson(), forelderBarnRelasjonHendelseDto.fnr(),
                forelderBarnRelasjonHendelseDto.relatertPersonsRolle(), forelderBarnRelasjonHendelseDto.relatertPersonsFnr());
        sendHendelsePåKafka(personhendelse.build());
    }

    private void produserFødselshendelse(FødselshendelseDto fødselshendelseDto, boolean publiserForelderBarnRelasjonMedFoedselshendelser) {
        var barnIdent = opprettNyttBarnOgOppdaterFamilierelasjoner(fødselshendelseDto);
        GenericRecordBuilder personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        personhendelse.set(HENDELSE_ID, UUID.randomUUID().toString());
        personhendelse.set(PERSONIDENTER, List.of(barnIdent.fnr(), barnIdent.aktørId()));
        personhendelse.set(MASTER_FIELD, "Freg");
        personhendelse.set(OPPRETTET, LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set(OPPLYSNINGSTYPE, "FOEDSELSDATO_V1");
        personhendelse.set(ENDRINGSTYPE, Endringstype.valueOf(fødselshendelseDto.endringstype()));
        if (fødselshendelseDto.tidligereHendelseId() != null) {
            personhendelse.set(TIDLIGERE_HENDELSE_ID, fødselshendelseDto.tidligereHendelseId());
        }

        if (!Endringstype.ANNULLERT.toString().equals(fødselshendelseDto.endringstype())) {
            GenericRecordBuilder fødselsdato = new GenericRecordBuilder(Foedselsdato.SCHEMA$);
            fødselsdato.set("foedselsdato", oversettLocalDateTilAvroFormat(fødselshendelseDto.fødselsdato()));
            personhendelse.set("foedselsdato", fødselsdato.build());
        }

        LOG.info("Publiserer FOEDSELSDATO_V1 på kafka for barn med ident {}, født: {}", barnIdent.fnr(), fødselshendelseDto.fødselsdato());
        sendHendelsePåKafka(personhendelse.build());

        if (publiserForelderBarnRelasjonMedFoedselshendelser) {
            produserForelderBarnRelasjon(fødselshendelseDto, barnIdent.fnr());
        }
    }

    private void produserForelderBarnRelasjon(FødselshendelseDto fødselshendelseDto, String barnIdent) {
        List<ForelderBarnRelasjonHendelseDto> forelderBarnRelasjonHendelseDtos = new ArrayList<>();
        if (fødselshendelseDto.fnrMor() != null) {
            // Legg til relasjoner for mor
            leggTilForelderBarnRelasjon(forelderBarnRelasjonHendelseDtos, fødselshendelseDto.endringstype(),
                    fødselshendelseDto.fnrMor(), barnIdent, "MOR");
        } else if (fødselshendelseDto.fnrFar() != null) {
            // Legg til relasjoner for far
            leggTilForelderBarnRelasjon(forelderBarnRelasjonHendelseDtos, fødselshendelseDto.endringstype(),
                    fødselshendelseDto.fnrFar(), barnIdent, "FAR");
        }

        forelderBarnRelasjonHendelseDtos.forEach(this::produserForelderBarnRelasjonHendelse);
    }

    private void leggTilForelderBarnRelasjon(List<ForelderBarnRelasjonHendelseDto> dtos,
                                             String endringstype,
                                             String forelderFnr,
                                             String barnFnr,
                                             String forelderRolle) {
        dtos.add(new ForelderBarnRelasjonHendelseDto(endringstype, forelderFnr, barnFnr, "BARN", "MOR"));
        dtos.add(new ForelderBarnRelasjonHendelseDto(endringstype, barnFnr, forelderFnr, forelderRolle, "BARN"));
    }


    public void sendHendelsePåKafka(GenericData.Record rekord) {
        localKafkaProducer.sendMelding(LEESAH_TOPIC, rekord);
    }

    private void produserDødshendelse(DødshendelseDto dødshendelseDto) {
        registrerDødshendelse(dødshendelseDto);
        GenericRecordBuilder personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        var personIdent = new PersonIdent(dødshendelseDto.fnr());
        personhendelse.set(HENDELSE_ID, UUID.randomUUID().toString());
        personhendelse.set(PERSONIDENTER, List.of(personIdent.fnr(), personIdent.aktørId()));
        personhendelse.set(MASTER_FIELD, "Freg");
        personhendelse.set(OPPRETTET, LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set(OPPLYSNINGSTYPE, "DOEDSFALL_V1");
        personhendelse.set(ENDRINGSTYPE, Endringstype.valueOf(dødshendelseDto.endringstype()));
        if (dødshendelseDto.tidligereHendelseId() != null) {
            personhendelse.set(TIDLIGERE_HENDELSE_ID, dødshendelseDto.tidligereHendelseId());
        }

        if (!Endringstype.ANNULLERT.toString().equals(dødshendelseDto.endringstype())) {
            GenericRecordBuilder dødsfall = new GenericRecordBuilder(Doedsfall.SCHEMA$);
            dødsfall.set("doedsdato", oversettLocalDateTilAvroFormat(dødshendelseDto.doedsdato()));
            personhendelse.set("doedsfall", dødsfall.build());
        }

        LOG.info("Publiserer DOEDSFALL_V1 hendelse på kafka for person med ident {}, dødsdato: {}", dødshendelseDto.fnr(),
                dødshendelseDto.doedsdato());
        sendHendelsePåKafka(personhendelse.build());
    }

    private void produserDødfødselshendelse(DødfødselhendelseDto dødfødselhendelseDto) {
        registererDødfødselsHendelse(dødfødselhendelseDto);
        GenericRecordBuilder personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        var personIdent = new PersonIdent(dødfødselhendelseDto.fnr());
        personhendelse.set(HENDELSE_ID, UUID.randomUUID().toString());
        personhendelse.set(PERSONIDENTER, List.of(personIdent.fnr(), personIdent.aktørId()));
        personhendelse.set(MASTER_FIELD, "Freg");
        personhendelse.set(OPPRETTET, LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set(OPPLYSNINGSTYPE, "DOEDFOEDT_BARN_V1");
        personhendelse.set(ENDRINGSTYPE, Endringstype.valueOf(dødfødselhendelseDto.endringstype()));
        if (dødfødselhendelseDto.tidligereHendelseId() != null) {
            personhendelse.set(TIDLIGERE_HENDELSE_ID, dødfødselhendelseDto.tidligereHendelseId());
        }

        if (!Endringstype.ANNULLERT.toString().equals(dødfødselhendelseDto.endringstype())) {
            GenericRecordBuilder dødfødtBarn = new GenericRecordBuilder(DoedfoedtBarn.SCHEMA$);
            dødfødtBarn.set("dato", oversettLocalDateTilAvroFormat(dødfødselhendelseDto.doedfoedselsdato()));
            personhendelse.set("doedfoedtBarn", dødfødtBarn.build());
        }

        LOG.info("Publiserer DOEDFOEDT_BARN_V1 hendelse på kafka for barn med ident {}, dødsdato: {}", dødfødselhendelseDto.fnr(),
                dødfødselhendelseDto.doedfoedselsdato());
        sendHendelsePåKafka(personhendelse.build());
    }

    private void produserFamilierelasjonHendelse(FamilierelasjonHendelseDto familierelasjonHendelseDto) {
        var personhendelse = new GenericRecordBuilder(Personhendelse.SCHEMA$);

        var personIdent = new PersonIdent(familierelasjonHendelseDto.fnr());
        personhendelse.set(HENDELSE_ID, UUID.randomUUID().toString());
        personhendelse.set(PERSONIDENTER, List.of(personIdent.fnr(), personIdent.aktørId()));
        personhendelse.set(MASTER_FIELD, "Freg");
        personhendelse.set(OPPRETTET, LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        personhendelse.set(OPPLYSNINGSTYPE, "FAMILIERELASJON_V1");
        personhendelse.set(ENDRINGSTYPE, Endringstype.valueOf(familierelasjonHendelseDto.endringstype()));

        if (!Endringstype.ANNULLERT.toString().equals(familierelasjonHendelseDto.endringstype())) {
            GenericRecordBuilder familierelasjon = new GenericRecordBuilder(Familierelasjon.SCHEMA$);
            familierelasjon.set("relatertPersonsIdent", familierelasjonHendelseDto.relatertPersonsFnr());
            familierelasjon.set("relatertPersonsRolle", familierelasjonHendelseDto.relatertPersonsRolle());
            familierelasjon.set("minRolleForPerson", familierelasjonHendelseDto.minRolleForPerson());
            personhendelse.set("familierelasjon", familierelasjon.build());
        }

        LOG.info("Publiserer FAMILIERELASJON_V1 hendelse med minRolle: {}, minFnr: {}, relatertPersonRolle: {}, relatertPersonFnr: {}",
                familierelasjonHendelseDto.minRolleForPerson(), familierelasjonHendelseDto.fnr(),
                familierelasjonHendelseDto.relatertPersonsRolle(), familierelasjonHendelseDto.relatertPersonsFnr());
        sendHendelsePåKafka(personhendelse.build());
    }

    private int oversettLocalDateTilAvroFormat(LocalDate localDate) {
        // Avro krever at dato angis som antall dager siden epoch som int
        return (int) localDate.toEpochDay();
    }

    private PersonIdent opprettNyttBarnOgOppdaterFamilierelasjoner(FødselshendelseDto fødselshendelseDto) {
        var mor = Optional.ofNullable(personRepository.hentPerson(fødselshendelseDto.fnrMor()));
        var far = Optional.ofNullable(personRepository.hentPerson(fødselshendelseDto.fnrFar()));
        var barnet = nyttBarn(fødselshendelseDto.fødselsdato(), null, mor.orElse(null), far.orElse(null));
        var identifikasjonBarn = (PersonIdent) barnet.personopplysninger().identifikator();
        mor.ifPresent(person -> leggTilBarnRelasjonFor(person, identifikasjonBarn));
        far.ifPresent(person -> leggTilBarnRelasjonFor(person, identifikasjonBarn));
        personRepository.leggTilPerson(barnet);
        return identifikasjonBarn;
    }

    private void leggTilBarnRelasjonFor(Person person, PersonIdent identifikasjonBarn) {
        var familierelasjoner = new ArrayList<>(person.personopplysninger().familierelasjoner());
        familierelasjoner.add(new no.nav.vtp.personopplysninger.Familierelasjon(
                no.nav.vtp.personopplysninger.Familierelasjon.Relasjon.BARN, identifikasjonBarn
        ));
        var personopplysnignerFar = person.personopplysninger().tilBuilder()
                .medFamilierelasjoner(familierelasjoner)
                .build();
        var oppdatertFar = person.tilBuilder()
                .medPersonopplysninger(personopplysnignerFar)
                .build();
        personRepository.endrePerson(oppdatertFar);
    }

    private void registererDødfødselsHendelse(DødfødselhendelseDto dødfødselhendelseDto) {
        var mor = personRepository.hentPerson(dødfødselhendelseDto.fnr());
        var annenForelder = mor.personopplysninger().familierelasjoner().stream()
                .filter(fr -> no.nav.vtp.personopplysninger.Familierelasjon.Relasjon.EKTE.equals(fr.relasjon()))
                .findFirst()
                .map(fr -> personRepository.hentPerson(fr.relatertTilId().fnr()));

        var barnet = nyttBarn(dødfødselhendelseDto.doedfoedselsdato(), dødfødselhendelseDto.doedfoedselsdato(), mor, annenForelder.orElse(null));
        personRepository.leggTilPerson(barnet);

        var identifikatorBarn = (PersonIdent) barnet.personopplysninger().identifikator();
        leggTilBarnRelasjonFor(mor, identifikatorBarn);
        annenForelder.ifPresent(af -> leggTilBarnRelasjonFor(af, identifikatorBarn));
    }

    private static Person nyttBarn(LocalDate fødsesldato, LocalDate dødsdato, Person mor, Person far) {
        var ident = dødsdato != null
                ? dødsdato.format(DateTimeFormatter.ofPattern("ddMMyy")) + "00001"
                : new FiktiveFnr().tilfeldigBarnUnderTreAarFnr();
        var relasjoner = new ArrayList<no.nav.vtp.personopplysninger.Familierelasjon>();
        relasjoner.add(new no.nav.vtp.personopplysninger.Familierelasjon(no.nav.vtp.personopplysninger.Familierelasjon.Relasjon.MOR,
                (PersonIdent) mor.personopplysninger().identifikator()));
        Optional.ofNullable(far).ifPresent(f -> relasjoner.add(
                new no.nav.vtp.personopplysninger.Familierelasjon(no.nav.vtp.personopplysninger.Familierelasjon.Relasjon.FAR,
                        (PersonIdent) f.personopplysninger().identifikator())));
        var personopplysnigner = new no.nav.vtp.personopplysninger.Personopplysninger(
                new PersonIdent(ident),
                Rolle.BARN,
                new Navn("Baby",  null, "Fødsel Navnesen"),
                fødsesldato,
                dødsdato,
                Språk.NB,
                Kjønn.M, // TODO: Hardkodet, mulig fnr ikke matcher mtp kjønn identifikator
                mor.personopplysninger().geografiskTilknytning(),
                relasjoner.stream().toList(),
                mor.personopplysninger().statsborgerskap(),
                List.of(),
                mor.personopplysninger().personstatus(),
                List.of(),
                mor.personopplysninger().adresser(),
                mor.personopplysninger().erSkjermet()
        );
        return new Person(personopplysnigner, List.of(), List.of(), List.of());
    }

    private void registrerDødshendelse(DødshendelseDto dødshendelseDto) {
        var personen = personRepository.hentPerson(dødshendelseDto.fnr());
        var oppdatertPerson = personen.tilBuilder()
                .medPersonopplysninger(personen.personopplysninger().tilBuilder().medDødsdato(dødshendelseDto.doedsdato()).build())
                .build();
        personRepository.endrePerson(oppdatertPerson);
    }
}
