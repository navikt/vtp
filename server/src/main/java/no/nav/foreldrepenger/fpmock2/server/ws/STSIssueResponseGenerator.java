package no.nav.foreldrepenger.fpmock2.server.ws;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.cxf.security.SecurityContext;
import org.apache.cxf.sts.StaticSTSProperties;
import org.apache.cxf.sts.operation.TokenIssueOperation;
import org.apache.cxf.sts.token.provider.SAMLTokenProvider;
import org.apache.cxf.sts.token.provider.TokenProvider;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseCollectionType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenType;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.CryptoFactory;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.principal.CustomTokenPrincipal;
import org.apache.wss4j.dom.engine.WSSConfig;

import no.nav.foreldrepenger.fpmock2.server.rest.KeyStoreTool;

public class STSIssueResponseGenerator {

    public RequestSecurityTokenResponseCollectionType buildRequestSecurityTokenResponseCollectionType(RequestSecurityTokenType request) {
        TokenIssueOperation issueOperation = new TokenIssueOperation();

        initTokenProviders(issueOperation);

        // Add Service
        //ServiceMBean service = new StaticService();
        //service.setEndpoints(Collections.singletonList("http://dummy-service.com/dummy"));
        //issueOperation.setServices(Collections.singletonList(service));

        String alias = initSTSProperties(issueOperation);

        // Mock up a request

        // Mock up message context
        Principal principal = new CustomTokenPrincipal(alias);
        Map<String, Object> msgCtx = new HashMap<>();
        msgCtx.put(
            SecurityContext.class.getName(),
            createSecurityContext(principal)
        );


        // Issue a token
        RequestSecurityTokenResponseCollectionType response = issueOperation.issue(request, principal, msgCtx);
        return response;
    }

    private void initTokenProviders(TokenIssueOperation issueOperation) {
        // Add Token Provider
        List<TokenProvider> providerList = new ArrayList<>();
        providerList.add(new SAMLTokenProvider());
        issueOperation.setTokenProviders(providerList);
    }

    private String initSTSProperties(TokenIssueOperation issueOperation) {
        // Add STSProperties object
        String alias = KeyStoreTool.getKeyAndCertAlias();
        StaticSTSProperties stsProperties = new StaticSTSProperties();
        Crypto crypto = getCrypto();
        stsProperties.setEncryptionCrypto(crypto);
        stsProperties.setSignatureCrypto(crypto);
        stsProperties.setCallbackHandler(new PasswordCallbackHandler());
        stsProperties.setSignatureUsername(alias);
        stsProperties.setIssuer("VTP");
        issueOperation.setStsProperties(stsProperties);
        return alias;
    }

    private Crypto getCrypto() {
        Crypto crypto;
        try {
            crypto = CryptoFactory.getInstance(getEncryptionProperties());
            return crypto;
        } catch (WSSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    /*
     * Create a security context object
     */
    private SecurityContext createSecurityContext(final Principal p) {
        return new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return p;
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }
        };
    }
    
    public class PasswordCallbackHandler implements CallbackHandler {

        @Override
        public void handle(Callback[] callbacks) throws IOException,
                UnsupportedCallbackException {
            for (int i = 0; i < callbacks.length; i++) {
                if (callbacks[i] instanceof WSPasswordCallback) { // CXF
                    WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
                    if (KeyStoreTool.getKeyAndCertAlias().equals(pc.getIdentifier())) {
                        pc.setPassword(new String(KeyStoreTool.getKeyStoreAndKeyPassword()));
                        break;
                    }
                }
            }
        }
    }

    private Properties getEncryptionProperties() {
        WSSConfig.init();
        Properties properties = new Properties();
        properties.put(
            "org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        properties.put("org.apache.wss4j.crypto.merlin.keystore.password", new String(KeyStoreTool.getKeyStoreAndKeyPassword()));
        properties.put("org.apache.wss4j.crypto.merlin.keystore.file", KeyStoreTool.getDefaultKeyStorePath());

        return properties;
    }


}
