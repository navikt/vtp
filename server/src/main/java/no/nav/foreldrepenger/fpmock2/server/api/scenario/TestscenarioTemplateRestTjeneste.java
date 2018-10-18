package no.nav.foreldrepenger.fpmock2.server.api.scenario;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplateRepository;

@Api(tags = { "Testscenario/templates" })
@Path("/api/testscenario/templates")
public class TestscenarioTemplateRestTjeneste {

    private final static Logger LOG = LoggerFactory.getLogger(TestscenarioTemplateRestTjeneste.class);

    @Context
    private TestscenarioTemplateRepository templateRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "templates", notes = ("Liste av tilgjengelig Testscenario Templates"), response = TemplateReferanse.class, responseContainer = "List")
    public List<TemplateReferanse> listTestscenarioTemplates() {
        List<TemplateReferanse> templates = templateRepository.getTemplates()
            .stream()
            .map(t -> {
                String key = t.getTemplateKey();
                return new TemplateReferanse(key, t.getTemplateNavn());
            })
            .collect(Collectors.toList());
        LOG.info("Tilgjengelige scenarioer oppgitt: {}", templates.stream().map(t -> t.getNavn()).collect(Collectors.joining(",")));
        return templates;
    }

    @GET
    @Path("/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "templates", notes = ("Beskrivelse av template, inklusiv påkrevde variable (og evt. default verdier"), response = TemplateDto.class)
    public Response beskrivTestscenarioTemplate(@PathParam("key") String key) {
        TestscenarioTemplate template = templateRepository.finn(key);

        if (template!=null) {
            TreeMap<String, String> map = new TreeMap<>();

            template.getDefaultVars().forEach((k, v) -> map.putIfAbsent(k, v));
            template.getExpectedVars().forEach(v -> map.putIfAbsent(v.getName(), null));

            return Response.ok(new TemplateDto(key, template.getTemplateNavn(), map)).build();
        } else {
            return Response.status(Status.NOT_FOUND.getStatusCode(), "Template " + key + " ikke funnet").build();
        }
    }

}
