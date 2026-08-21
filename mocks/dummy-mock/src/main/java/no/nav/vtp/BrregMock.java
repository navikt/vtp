package no.nav.vtp;

import java.net.URI;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

@Path("/dummy/brreg")
@Produces(MediaType.APPLICATION_JSON)
public class BrregMock {

    private static final String ORGANISASJONSNUMMER = "999999999";
    private static final String NAVN = "VTP FISKE";

    @POST
    @Path("/autorisert-api/personer/rolleutskrift")
    @Consumes(MediaType.TEXT_PLAIN)
    public Rolleutskrift hentRolleutskrift(@Context UriInfo uriInfo) {
        var enhetUri = uriInfo.getBaseUriBuilder()
            .path("dummy/brreg/enheter/{organisasjonsnummer}")
            .build(ORGANISASJONSNUMMER);
        var rolle = new Rolle(false, false, new Kode("INNH", "Innehaver"));
        var enhet = new RolleEnhet(ORGANISASJONSNUMMER, NAVN, List.of(rolle), new Links(new Link(enhetUri)));
        return new Rolleutskrift(List.of(enhet));
    }

    @GET
    @Path("/enheter/" + ORGANISASJONSNUMMER)
    public Enhet hentEnhet() {
        return new Enhet(
            ORGANISASJONSNUMMER,
            NAVN,
            new Kode("ENK", "Enkeltpersonforetak"),
            new Kode("03.110", "Hav- og kystfiske"),
            false
        );
    }

    public record Rolleutskrift(List<RolleEnhet> enheter) {
    }

    public record RolleEnhet(String organisasjonsnummer, String navn, List<Rolle> roller, Links _links) {
    }

    public record Rolle(boolean fratraadt, boolean avregistrert, Kode type) {
    }

    public record Links(Link enhet) {
    }

    public record Link(URI href) {
    }

    public record Enhet(String organisasjonsnummer,
                        String navn,
                        Kode organisasjonsform,
                        Kode naeringskode1,
                        boolean underAvvikling) {
    }

    public record Kode(String kode, String beskrivelse) {
    }
}
