package no.nav.tjeneste.virksomhet.infotrygd.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import no.nav.vtp.person.PersonRepository;

@Path("/infotrygd")
public class InfotrygdMock {
    private static final Logger LOG = LoggerFactory.getLogger(InfotrygdMock.class);

    private final PersonRepository personRepository;

    public InfotrygdMock(@Context PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/grunnlag/sykepenger")
    @Produces({"application/json"})
    public List<GrunnlagDto> getSykepenger(PersonRequest personRequest) {
        LOG.info("Infotrygd Rest kall til sykepenger");
        return personRequest.fnr().stream().flatMap(fnr -> {
            var person = personRepository.hentPerson(fnr);
            return PersonTilGrunnlagMapper.tilSykepengerGrunnlag(person).stream();
        }).toList();
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/grunnlag/paaroerende-sykdom")
    @Produces({"application/json"})
    public List<GrunnlagDto> paaroerendeSykdomUsingPost(PersonRequest personRequest) {
        LOG.info("Infotrygd Rest kall til pårørendesykdom");
        return personRequest.fnr().stream().flatMap(fnr -> {
            var person = personRepository.hentPerson(fnr);
            return PersonTilGrunnlagMapper.tilBarnsykdomGrunnlag(person).stream();
        }).toList();
    }
}
