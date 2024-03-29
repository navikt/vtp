package no.nav.foreldrepenger.vtp.server.auth.rest.azuread;

import java.util.List;

import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.NAVAnsatt;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;

@Tag(name = "AzureAd")
@Path("/MicrosoftGraphApi")
public class MicrosoftGraphApiMock {
    private static final JwtConsumer unvalidatingConsumer = new JwtConsumerBuilder().setSkipAllValidators()
            .setDisableRequireSignature()
            .setSkipSignatureVerification()
            .build();

    private static final AnsatteIndeks ansattIndeks = BasisdataProviderFileImpl.getInstance().getAnsatteIndeks();
    protected static final String AUTHORIZATION = "Authorization";

    @GET
    @Produces({"application/json;charset=UTF-8"})
    @Path("/oidc/userinfo")
    public UserInfo userInfo(@Context HttpServletRequest req) {
        var ansatt = getAnsatt(req.getHeader(AUTHORIZATION));
        return new UserInfo(ansatt.ansattId(), ansatt.ansatt().displayName(), req.getLocalName(), ansatt.ansatt().cn(),
                "http://example.com/picture.jpg", "user@nav.no");
    }

    @GET
    @Produces({"application/json;charset=UTF-8"})
    @Path("/v1.0/me")
    public Response me(@Context HttpServletRequest req, @QueryParam("select") String select) {
        var ansatt = getAnsatt(req.getHeader(AUTHORIZATION));
        var user = new User(ansatt.ansattId(),
                "https://graph.microsoft.com/v1.0/$metadata#users(id," + ansatt.ansattId() + ")/$entity", ansatt.ansattId(),
                ansatt.ansatt().groups().stream().map(Group::new).toList());
        return Response.ok(user).build();
    }

    @GET
    @Produces({"application/json;charset=UTF-8"})
    @Path("/v1.0/users/{id}/memberOf/microsoft.graph.group")
    public Response memberOf(@Context HttpServletRequest req, @PathParam("id") String id) {
        var ansatt = getAnsatt(req.getHeader(AUTHORIZATION));
        var user = new MemberOfResponse("https://graph.microsoft.com/v1.0/$metadata#groups(" + ansatt.ansatt().displayName() + ")",
                null, ansatt.ansatt().groups().stream().map(Group::new).toList());
        return Response.ok(user).build();
    }


    private Pair getAnsatt(String auth) {
        if (!auth.startsWith("Bearer ")) {
            throw new WebApplicationException("Bad mock access token; must be on format Bearer access:<userid>",
                    Response.Status.FORBIDDEN);
        } else {
            try {
                var assertion = auth.substring("Bearer ".length());
                var claims = unvalidatingConsumer.processToClaims(assertion);
                var ansattId = claims.getStringClaimValue("NAVident");
                var ansatt = ansattIndeks.findByCn(ansattId);
                return new Pair(ansattId, ansatt);
            } catch (Exception e) {
                throw new WebApplicationException("Bad mock access token; must be on format Bearer access:<userid>",
                        Response.Status.FORBIDDEN);
            }
        }
    }


    record Pair(String ansattId, NAVAnsatt ansatt) {
    }

    record UserInfo(String sub, String name, String family_name, String given_name, String picture, String email) {
    }

    record User(String id, @JsonProperty("@odata.context") String context, String onPremisesSamAccountName, List<Group> memberOf) {
    }

    record Group(String displayName) {
    }

    record MemberOfResponse(@JsonProperty("@odata.context") String context, @JsonProperty("@odata.nextLink") String nextLink,
                            List<Group> value) {
    }


}
