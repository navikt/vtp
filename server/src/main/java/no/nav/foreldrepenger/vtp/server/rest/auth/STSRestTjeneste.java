package no.nav.foreldrepenger.vtp.server.rest.auth;

import io.swagger.annotations.Api;
import no.nav.foreldrepenger.vtp.felles.KeyStoreTool;
import no.nav.foreldrepenger.vtp.server.ws.STSIssueResponseGenerator;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Base64;

@Api(tags = {"Security Token Service"})
@Path("/v1/sts")
public class STSRestTjeneste {
    private final STSIssueResponseGenerator generator = new STSIssueResponseGenerator();

    @POST
    @Path("/token/exchange")
    @Produces({MediaType.APPLICATION_JSON})
    public SAMLResponse dummySaml(@QueryParam("grant_type") String grant_type,
                                  @QueryParam("subject_token_type") String issuedTokenType,
                                  @QueryParam("subject_token") String subject_token) {
        RequestSecurityTokenResponseType token = generator.buildRequestSecurityTokenResponseType("urn:oasis:names:tc:SAML:2.0:assertion");
        StringWriter sw = new StringWriter();
        JAXB.marshal(token, sw);
        String xmlString = sw.toString();

        SAMLResponse response = new SAMLResponse();
        response.setAccess_token(Base64.getUrlEncoder().withoutPadding().encodeToString(xmlString.getBytes()));
        response.setDecodedToken(xmlString);
        response.setToken_type("Bearer");
        response.setIssued_token_type(issuedTokenType);
        response.setExpires_in(LocalDateTime.MAX);

        return response;
    }

    @GET
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON})
    public UserTokenResponse dummyToken(@QueryParam("grant_type") String grant_type,
                                        @QueryParam("scope") String scope) throws JoseException {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setKey(KeyStoreTool.getJsonWebKey().getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        String token = jws.getCompactSerialization();
        return new UserTokenResponse(token, 600000L, "jwt");
    }

    public static class SAMLResponse {

        private String access_token;
        private String issued_token_type;
        private String token_type;
        private String decodedToken;
        private LocalDateTime expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getIssued_token_type() {
            return issued_token_type;
        }

        public void setIssued_token_type(String issued_token_type) {
            this.issued_token_type = issued_token_type;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getDecodedToken() {
            return decodedToken;
        }

        public void setDecodedToken(String decodedToken) {
            this.decodedToken = decodedToken;
        }

        public LocalDateTime getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(LocalDateTime expires_in) {
            this.expires_in = expires_in;
        }
    }

    public static class UserTokenResponse {
        private String access_token;
        private Long expires_in;
        private String token_type;
        private final LocalDateTime issuedTime = LocalDateTime.now();

        @SuppressWarnings("unused")
        public UserTokenResponse() {
            //Required by Jackson when mapping json object
        }

        public UserTokenResponse(String access_token, Long expires_in, String token_type) {
            this.access_token = access_token;
            this.expires_in = expires_in;
            this.token_type = token_type;
        }

        /**
         *
         * @param expirationLeeway the amount of seconds to be subtracted from the expirationTime to avoid returning false positives
         * @return <code>true</code> if "now" is after the expirationtime(minus leeway), else returns <code>false</code>
         */
        public boolean isExpired(long expirationLeeway) {
            return LocalDateTime.now().isAfter(issuedTime.plusSeconds(expires_in).minusSeconds(expirationLeeway));
        }

        public String getAccess_token() {
            return access_token;
        }

        public Long getExpires_in() {
            return expires_in;
        }

        public String getToken_type() {
            return token_type;
        }

        @Override
        public String toString() {
            return "UserTokenImpl{" +
                    "access_token='" + access_token + '\'' +
                    ", expires_in=" + expires_in +
                    ", token_type='" + token_type + '\'' +
                    '}';
        }
    }
}