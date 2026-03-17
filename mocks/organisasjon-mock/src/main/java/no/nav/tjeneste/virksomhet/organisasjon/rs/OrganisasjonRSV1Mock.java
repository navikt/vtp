package no.nav.tjeneste.virksomhet.organisasjon.rs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.arbeidsforhold.Organisasjon;
import no.nav.vtp.person.ident.Orgnummer;

@Path("ereg/api/v1/organisasjon")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganisasjonRSV1Mock {

    private static final Logger LOG = LoggerFactory.getLogger(OrganisasjonRSV1Mock.class);

    @Context
    private PersonRepository personRepository;


    @GET
    @Path("/{orgnummer}")
    public OrganisasjonResponse hentOrganisasjonAdresse(@PathParam("orgnummer") String orgnummer,
                                                        @Context HttpHeaders httpHeaders,
                                                        @Context UriInfo uriInfo) {
        if (orgnummer == null) {
            throw new IllegalArgumentException("Orgnummer ikke angitt");
        }

        LOG.info("EREG REST {}", orgnummer);
        return personRepository.hentInformasjonOmArbeidsforhold(new Orgnummer(orgnummer))
                .map(OrganisasjonRSV1Mock::tilOrganisasjonRespons)
                .orElse(null);
    }

    @GET
    @Path("/{orgnummer}/noekkelinfo")
    public OrganisasjonNoekkelinfo hentOrganisasjonNoekkelinfo(@PathParam("orgnummer") String orgnummer,
                                                               @Context HttpHeaders httpHeaders,
                                                               @Context UriInfo uriInfo) {
        if (orgnummer == null) {
            throw new IllegalArgumentException("Orgnummer ikke angitt");
        }

        LOG.info("EREG REST noekkelinfo {}", orgnummer);
        return personRepository.hentInformasjonOmArbeidsforhold(new Orgnummer(orgnummer))
                .map(OrganisasjonRSV1Mock::tilNøkkelinfo)
                .orElse(null);
    }


    private static OrganisasjonResponse tilOrganisasjonRespons(Organisasjon organisasjon) {
        var info = organisasjon.informasjon();
        var organisasjonDetaljer = new OrganisasjonResponse.OrganisasjonDetaljer(
                Optional.ofNullable(info.registreringsdato())
                        .map(LocalDate::atStartOfDay)
                        .orElse(LocalDateTime.now().minusYears(1)),
                null,
                null,
                null
        );
        return new OrganisasjonResponse(
                organisasjon.identifikator(),
                OrganisasjonstypeEReg.VIRKSOMHET,
                tilNavn(organisasjon.informasjon().navn()),
                organisasjonDetaljer
        );
    }

    private static OrganisasjonResponse.Navn tilNavn(String navn) {
        return new OrganisasjonResponse.Navn(
                navn,
                navn,
                null,
                null,
                null,
                null
        );
    }

    private static OrganisasjonNoekkelinfo tilNøkkelinfo(Organisasjon organisasjon) {
        var info = organisasjon.informasjon();
        return new OrganisasjonNoekkelinfo(
                null,
                organisasjon.identifikator(),
                OrganisasjonstypeEReg.VIRKSOMHET.getKode(),
                tilNavn(info.navn()),
                null
        );
    }
}

