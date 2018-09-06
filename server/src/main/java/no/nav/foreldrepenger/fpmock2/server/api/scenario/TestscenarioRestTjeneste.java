package no.nav.foreldrepenger.fpmock2.server.api.scenario;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplateRepository;

@Api(tags = { "Testscenario/templates" })
@Path("/testscenario")
public class TestscenarioRestTjeneste {

    @Context
    private TestscenarioTemplateRepository templateRepository;

    @Context
    private TestscenarioRepository testscenarioRepository;

    @GET
    @Path("/templates")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "templates", notes = ("Liste av tilgjengelig Testscenario Templates"))
    public List<TemplateBeskrivelse> listTestscenarioTemplates() {
        List<TemplateBeskrivelse> templates = templateRepository.getTemplates()
            .stream()
            .map(t -> new TemplateBeskrivelse(t.getTemplateNavn(), t.getTemplateNavn(), t.getDefaultVars()))
            .collect(Collectors.toList());
        return templates;
    }
}
