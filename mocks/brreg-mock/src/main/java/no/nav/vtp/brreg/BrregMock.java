package no.nav.vtp.brreg;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.næring.RegistrertNæringsvirksomhet;

@Path("/dummy/brreg")
@Produces(MediaType.APPLICATION_JSON)
public class BrregMock {

    @POST
    @Path("/autorisert-api/personer/rolleutskrift")
    @Consumes(MediaType.TEXT_PLAIN)
    public Rolleutskrift hentRolleutskrift(String fødselsnummer, @Context UriInfo uriInfo) {
        var enheter = Optional.ofNullable(fødselsnummer)
                .map(String::strip)
                .map(PersonRepository::hentPerson)
                .stream()
                .flatMap(person -> person.registrerteNæringsvirksomheter().stream())
                .map(virksomhet -> tilRolleEnhet(virksomhet, uriInfo))
                .toList();
        return new Rolleutskrift(enheter);
    }

    private static RolleEnhet tilRolleEnhet(RegistrertNæringsvirksomhet virksomhet, UriInfo uriInfo) {
        var enhetUri = uriInfo.getBaseUriBuilder()
            .path("dummy/brreg/enheter/{organisasjonsnummer}")
            .build(virksomhet.organisasjonsnummer());
        var rolle = new Rolle(false, false, new Kode("INNH", "Innehaver"));
        return new RolleEnhet(virksomhet.organisasjonsnummer(), virksomhet.navn(), List.of(rolle), new Links(new Link(enhetUri)));
    }

    @GET
    @Path("/enheter/{organisasjonsnummer}")
    public Enhet hentEnhet(@PathParam("organisasjonsnummer") String organisasjonsnummer) {
        var virksomhet = PersonRepository.hentRegistrertNæringsvirksomhet(organisasjonsnummer)
                .orElseThrow(NotFoundException::new);
        return new Enhet(
            virksomhet.organisasjonsnummer(),
            virksomhet.navn(),
            new Kode(virksomhet.organisasjonsformKode(), virksomhet.organisasjonsformBeskrivelse()),
            new Kode(virksomhet.næringskode(), virksomhet.næringskodeBeskrivelse()),
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
