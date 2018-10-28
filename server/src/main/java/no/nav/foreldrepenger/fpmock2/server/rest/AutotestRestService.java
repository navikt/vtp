package no.nav.foreldrepenger.fpmock2.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
*/

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "Autotest" })
@Path("/autotest")
public class AutotestRestService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "autotest", notes = ("Kjører alle autotester og rapporterer output"))
    public Response autotest() {
        return null;
        /*
        LauncherDiscoveryRequestBuilder requestBuilder = LauncherDiscoveryRequestBuilder.request();
        LauncherDiscoveryRequest launcherRequest = requestBuilder
            .selectors(DiscoverySelectors.selectPackage("no.nav.foreldrepenger.autotest.tests"))
            .build();

        Launcher launcher = LauncherFactory.create();

        SummaryGeneratingListener summary = new SummaryGeneratingListener();

        StringWriter detailSw = new StringWriter(4 * 8192);

        TestExecutionListener testExecutionListener = new TestExecutionListener() {

            @Override
            public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
                if (testIdentifier.isTest()) {
                    detailSw.write(testIdentifier + " - " + testExecutionResult + "\n");
                }
            }
        };

        launcher.registerTestExecutionListeners(summary, testExecutionListener);
        launcher.execute(launcherRequest);

        StringWriter sw = new StringWriter(4 * 8192);
        PrintWriter pw = new PrintWriter(sw, false);
        TestExecutionSummary testExecutionSummary = summary.getSummary();
        testExecutionSummary.printTo(pw);
        testExecutionSummary.printFailuresTo(pw);

        pw.write(detailSw.toString());

        pw.flush();

        if (testExecutionSummary.getContainersFailedCount() > 0) {
            return Response.ok(sw.toString()).build(); // TODO: annen error kode? 500?
        } else {
            return Response.ok(sw.toString()).build();
        }
        */
    }
}
