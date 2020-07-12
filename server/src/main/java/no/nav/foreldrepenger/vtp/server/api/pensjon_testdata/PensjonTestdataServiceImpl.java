package no.nav.foreldrepenger.vtp.server.api.pensjon_testdata;

import static java.util.Optional.of;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.text;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static javax.ws.rs.core.UriBuilder.fromUri;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.VoksenModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;

public class PensjonTestdataServiceImpl implements PensjonTestdataService {
    private final Logger logger = getLogger(getClass());
    private final Client client = newClient();
    private final UriBuilder uriBuilder;

    public PensjonTestdataServiceImpl(final String baseUrl) {
        uriBuilder = fromUri(baseUrl);
    }

    @Override
    public void opprettData(final Testscenario testscenario) {
        of(testscenario)
                .map(Testscenario::getPersonopplysninger)
                .map(Personopplysninger::getSøker)
                .map(VoksenModell::getIdent)
                .ifPresent(this::lagrePerson);

        of(testscenario)
                .map(Testscenario::getPersonopplysninger)
                .map(Personopplysninger::getAnnenPart)
                .map(VoksenModell::getIdent)
                .ifPresent(this::lagrePerson);
    }

    public void lagrePerson(final String ident) {
        final URI uri = uriBuilder.clone().path("/api/person").path(ident).build();

        final Response response = client.target(uri)
                .request()
                .post(text(""));

        if (response.getStatusInfo().getFamily() != SUCCESSFUL) {
            logger.error("Failed to create person (ident={}, uri={}, responseCode={})", ident, uri, response.getStatus());
            throw new RuntimeException("Failed to create person with ident=" + ident + " in pensjon-testdata using Uri=" + uri);
        }
    }
}
