package no.nav.foreldrepenger.vtp.server.auth.rest;

import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.AUDIENCE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.CLIENT_ID;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.GRANT_TYPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.SCOPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.STATE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.TokenClaims.ACR;
import static no.nav.foreldrepenger.vtp.server.auth.rest.TokenClaims.AZP;
import static no.nav.foreldrepenger.vtp.server.auth.rest.TokenClaims.defaultJwtClaims;
import static no.nav.foreldrepenger.vtp.server.auth.rest.fpaad.AzureADForeldrepengerRestTjeneste.TENANT;
import static no.nav.foreldrepenger.vtp.server.auth.rest.fpaad.AzureADForeldrepengerRestTjeneste.getIssuer;

import java.text.ParseException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.jwt.MalformedClaimException;

import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.server.auth.rest.fpaad.AzureADForeldrepengerRestTjeneste;
import no.nav.foreldrepenger.vtp.server.auth.rest.isso.OpenAMRestService;
import no.nav.foreldrepenger.vtp.server.auth.rest.tokenx.TokenxRestTjeneste;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;

/**
 * Tjeneste som kan brukes for å hente ulike forskjellige tokens til testformål

 */
@ApplicationScoped
@Path("/token/provider")
public class TokenProviderTjeneste {

    private static final AnsatteIndeks ansattIndeks = BasisdataProviderFileImpl.getInstance().getAnsatteIndeks();

    private final AzureADForeldrepengerRestTjeneste aadRestTjeneste;
    private final OpenAMRestService openAMRestService;
    private final TokenxRestTjeneste tokenxRestTjeneste;

    public static final String ANSATT_ID = "ansatt_id";
    public static final String FNR = "fnr";


    @Inject
    public TokenProviderTjeneste(AzureADForeldrepengerRestTjeneste aadRestTjeneste,
                                 OpenAMRestService openAMRestService,
                                 TokenxRestTjeneste tokenxRestTjeneste) {
        this.aadRestTjeneste = aadRestTjeneste;
        this.openAMRestService = openAMRestService;
        this.tokenxRestTjeneste = tokenxRestTjeneste;
    }

    /**
     * Vi tilbyr to ulike tokens for ansatte: ISSO/OpenAM og AzureAD.
     * Vi har forhåndsdefinerte ansatte med ulike AD-roller definert i filen nav-ansatte.json.
     * Alternativ kan en kalle dette endepunktet for å få oppp denne informasjonen, for så å velge bruker programatisk.
     * @return Liste over navansatte og deres respektive rolle {@link no.nav.foreldrepenger.vtp.testmodell.ansatt.NAVAnsatt}
     */
    @GET
    @Path("/ansatte")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "token/provider/ansatte", notes = ("Henter ut alle predefinerte ansatte som kan genereres token for"))
    public Response hentAlleRegisterteAnsatte(@Context HttpServletRequest req) {
        return Response.ok(ansattIndeks.alleAnsatte()).build();
    }

    /**
     * Vi tilbyr to ulike tokens for ansatte. Vi har forhåndsdefinerte ansatte med ulike AD-roller definert i filen nav-ansatte.json.
     * @param ansattId: Matcher feltet 'cn' i json filen. Eksempler: saksbeh, saksbeh6, saksbeh7, beslut, oversty, klageb, veil og oppgs
     * @return default Oauth2 response -> {@link Oauth2AccessTokenResponse}
     */
    @Deprecated(forRemoval = true) // Brukes bare i overgang til AzureAD
    @POST
    @Path("/ansatt/openam")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "token/provider/openam", notes = ("Henter openam token for saksbehandler"))
    public Response hentAnsattTokenOpenAm(@Context HttpServletRequest req, @FormParam(ANSATT_ID) String ansattId) {
        var ansatt = ansattIndeks.findByCn(ansattId);
        return openAMRestService.accessToken(
                req,
                null,
                null,
                ansatt.cn(),
                null,
                req.getParameter(STATE));
    }


    /**
     * Henter et on-behalf-of (OBO) token fro nav ansatt
     * @param tenant kan ikke være null og settes til samme som validerer token i løsningen din
     * @param grantType støtter client_credentials og urn:ietf:params:oauth:grant-type:jwt-bearer og refresh_token
     * @param clientId: Med format localhost.<namespace>.<app-name>, eks: localhost.teamforeldrepenger.fpsak
     * @param ansattId: Matcher feltet 'cn' i json filen. Eksempler: saksbeh, saksbeh6, saksbeh7, beslut, oversty, klageb, veil og oppgs
     * @return default Oauth2 response -> {@link Oauth2AccessTokenResponse}
     * @throws ParseException
     */
    @POST
    @Path("/ansatt/azuread")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "azureAd/access_token", notes = ("Henter ut et OBO Azure AD token for saksbehandler"))
    public Response hentAnsattTokenAzureAd(@Context HttpServletRequest req,
                                           @FormParam(ANSATT_ID) @NotNull String ansattId,
                                           @FormParam(CLIENT_ID) String clientId,
                                           @FormParam(TENANT) @NotNull String tenant,
                                           @FormParam(SCOPE) String scope,
                                           @FormParam(GRANT_TYPE) String grantType) throws ParseException, MalformedClaimException {
        Token subjectToken = null;
        if (grantType.equalsIgnoreCase("urn:ietf:params:oauth:grant-type:jwt-bearer")) {
            var ansatt = ansattIndeks.findByCn(ansattId);
            var basicClaimsMedSubject = defaultJwtClaims(clientId + ":" + ansatt.cn(), "ikke_i_bruk", "ikke_i_bruk");
            subjectToken = Token.fra(basicClaimsMedSubject);
        }
        return aadRestTjeneste.accessToken(req,
                tenant,
                scope != null ? scope: "foo",
                clientId,
                null,
                grantType,
                subjectToken,
                null,
                null,
                null);
    }

    @POST
    @Path("/borger/loginservice")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "token/provider/loginservice", notes = ("Henter ut et loginservice token for borger"))
    public Response henterLoginserviceTokenSelvbetjening(@Context HttpServletRequest req,
                                                         @FormParam(TENANT) String tenant,
                                                         @FormParam(FNR) String fnr) {
        var claims = defaultJwtClaims(fnr, getIssuer(req, tenant), "OIDC");
        claims.setClaim("tid", tenant);
        claims.setClaim("ver", "2.0");
        claims.setClaim("oid", fnr);
        claims.setClaim(AZP, "OIDC");
        claims.setClaim(ACR, "Level4");
        var token = Token.fra(claims);
        return Response.ok(new Oauth2AccessTokenResponse(token)).build();
    }

    @POST
    @Path("/borger/idporten")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "token/provider/idporten", notes = ("Henter ut et idporten token for borger"))
    public Response henterUtIDportenTokenForBorger(@Context HttpServletRequest req,
                                                   @FormParam(FNR) String fnr) {
        throw new UnsupportedOperationException("Støtter ikke IDporten for øyeblikket!");
    }

    /**
     * Henter ut et TokenX token med subject/pid som brukeren definert med fnr og angitt audience – hvilken applikasjon som kan kalles
     * @param audience <cluster>:<namespace>:<appname> e.g. lokal:teamforeldrepenger:fpsak (fpsak må dermed ha lagt inn dette som en akseptert audience ved token validering.
     * @param fnr identen til brukeren.
     * @return Nokså default Oauth2 respons, men har noen unike verdier som kan benyttes -> {@link TokenxRestTjeneste.TokenDingsResponsDto}
     * @throws ParseException
     */
    @POST
    @Path("/borger/tokenx")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "token/provider/tokenx", notes = ("Henter ut TokenX token for borger"))
    public Response hentTokenXToken(@Context HttpServletRequest req,
                                    @FormParam(AUDIENCE) String audience,
                                    @FormParam(FNR) String fnr) throws ParseException {
        var basicClaimsMedSubject = defaultJwtClaims(fnr, "ikke_i_bruk", "ikke_i_bruk");
        return tokenxRestTjeneste.token(req,
                "urn:ietf:params:oauth:grant-type:token-exchange",
                "urn:ietf:params:oauth:grant-type:token-exchange",
                null,
                null,
                Token.fra(basicClaimsMedSubject),
                audience);
    }

}
