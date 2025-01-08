package no.nav.foreldrepenger.vtp.server.auth.rest.azuread;

import java.util.List;
import java.util.UUID;

import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.NavAnsatt;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;

@Tag(name = "AzureAd")
@Path("/MicrosoftGraphApi")
public class MicrosoftGraphApiMock {
    private static final JwtConsumer unvalidatingConsumer = new JwtConsumerBuilder().setSkipAllValidators()
            .setDisableRequireSignature()
            .setSkipSignatureVerification()
            .build();

    private static final AnsatteIndeks ANSATTE_INDEKS = BasisdataProviderFileImpl.getInstance().getAnsatteIndeks();
    protected static final String AUTHORIZATION = "Authorization";

    @GET
    @Produces({"application/json;charset=UTF-8"})
    @Path("/oidc/userinfo")
    public UserInfo userInfo(@Context HttpServletRequest req) {
        var ansatt = getAnsatt(req.getHeader(AUTHORIZATION));
        return new UserInfo(ansatt.oid().toString(), ansatt.displayName(), req.getLocalName(), ansatt.ident(), ansatt.givenName(),
                ansatt.surname(), "http://example.com/picture.jpg", "user@nav.no", ansatt.streetAddress());
    }

    @GET
    @Produces({"application/json;charset=UTF-8"})
    @Path("/v1.0/me")
    public Response me(@Context HttpServletRequest req, @QueryParam("select") String select) {
        var navAnsatt = getAnsatt(req.getHeader(AUTHORIZATION));
        if (navAnsatt != null) {
            var user = new User(navAnsatt.oid(), navAnsatt.ident(), navAnsatt.displayName(),
                    navAnsatt.givenName(), navAnsatt.surname(), navAnsatt.streetAddress(),
                    "https://graph.microsoft.com/v1.0/$metadata#users(id," + navAnsatt.ident() + ")/$entity");
            return Response.ok(user).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/v1.0/me/memberOf")
    @Produces({"application/json;charset=UTF-8"})
    @Operation(description = "hent informasjon om innlogget bruker.")
    public Response meMemberOf(@Context HttpServletRequest req) {
        var navAnsatt = getAnsatt(req.getHeader(AUTHORIZATION));
        if (navAnsatt != null) {
            return Response.ok(opprettMemberOfResponse(navAnsatt)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    private static @NotNull MemberOfResponse opprettMemberOfResponse(NavAnsatt ansatt) {
        return new MemberOfResponse("https://graph.microsoft.com/v1.0/$metadata#groups(" + ansatt.displayName() + ")", null,
                mapTilUserGroupsResponse(ansatt));
    }

    @GET
    @Produces({"application/json;charset=UTF-8"})
    @Path("/v1.0/users")
    public Response users(@QueryParam("$filter") @NotNull String filter) {
        var filterBy = filter.split(" eq ");
        var identifikator = filterBy[1].replace("'", "");

        NavAnsatt ansatt;
        if (filterBy[0].contains("onPremisesSamAccountName")) {
            ansatt = ANSATTE_INDEKS.findByIdent(identifikator);
        } else {
            ansatt = ANSATTE_INDEKS.findById(UUID.fromString(identifikator));
        }

        if (ansatt != null) {
            var user = new User(ansatt.oid(),
                    ansatt.ident(),
                    ansatt.displayName(),
                    ansatt.givenName(), ansatt.surname(), ansatt.streetAddress(),
                    "https://graph.microsoft.com/v1.0/$metadata#users(id," + identifikator + ")/$entity");
            return Response.ok(user).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Produces({"application/json;charset=UTF-8"})
    @Path("/v1.0/users/{oid}/memberOf")
    public Response userMemberOf(@PathParam("oid") @NotNull UUID id) {
        var ansatt = ANSATTE_INDEKS.findById(id);
        if (ansatt != null) {
            var response = opprettMemberOfResponse(ansatt);
            return Response.ok(response).build();
        }
        return Response.noContent().build();
    }

    private NavAnsatt getAnsatt(String auth) {
        if (!auth.startsWith("Bearer ")) {
            throw new WebApplicationException("Bad mock access token; must be on format Bearer access:<userid>",
                    Response.Status.FORBIDDEN);
        } else {
            try {
                var assertion = auth.substring("Bearer ".length());
                var claims = unvalidatingConsumer.processToClaims(assertion);
                var ident = claims.getClaimValue("NAVident", String.class);
                return ANSATTE_INDEKS.findByIdent(ident);
            } catch (Exception e) {
                throw new WebApplicationException("Bad mock access token; must be on format Bearer access:<userid>",
                        Response.Status.FORBIDDEN);
            }
        }
    }

    private static List<Group> mapTilUserGroupsResponse(NavAnsatt ansatt) {
        return ansatt.groups().stream().map(MicrosoftGraphApiMock::mapTilGroup).toList();
    }

    private static Group mapTilGroup(NavAnsatt.NavGroup group) {
        return new Group(group.oid(), group.name(), group.name());
    }

    record UserInfo(String sub, String name, String family_name, String given_name, String givenName, String surname,
                    String picture, String email, String streetAddress) {
    }

    record User(UUID id, String onPremisesSamAccountName, String displayName, String givenName, String surname, String streetAddress,
                @JsonProperty("@odata.context") String context) {
    }

    record Group(UUID id, String displayName, String onPremisesSamAccountName) {
    }

    record MemberOfResponse(@JsonProperty("@odata.context") String context, @JsonProperty("@odata.nextLink") String nextLink,
                            List<Group> value) {
    }

}
