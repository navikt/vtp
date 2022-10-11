package no.nav.foreldrepenger.vtp.server.auth.soap.sts;

import static no.nav.foreldrepenger.util.KeystoreUtils.getKeyAndCertAlias;

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
import org.apache.cxf.sts.request.ReceivedToken;
import org.apache.cxf.sts.request.RequestRequirements;
import org.apache.cxf.sts.request.TokenRequirements;
import org.apache.cxf.sts.token.delegation.TokenDelegationHandler;
import org.apache.cxf.sts.token.delegation.UsernameTokenDelegationHandler;
import org.apache.cxf.sts.token.provider.SAMLTokenProvider;
import org.apache.cxf.sts.token.provider.TokenProvider;
import org.apache.cxf.sts.token.provider.TokenProviderParameters;
import org.apache.cxf.ws.security.sts.provider.model.ObjectFactory;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenCollectionType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseCollectionType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenType;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.CryptoFactory;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.principal.CustomTokenPrincipal;
import org.apache.wss4j.dom.engine.WSSConfig;

import no.nav.foreldrepenger.util.KeystoreUtils;


public class STSIssueResponseGenerator {

    private static final String USERNAME = "goddagmannøkseskaft";

    static class PasswordCallbackHandler implements CallbackHandler {

        @Override
        public void handle(Callback[] callbacks) throws IOException,
                UnsupportedCallbackException {
            for (int i = 0; i < callbacks.length; i++) {
                if (callbacks[i] instanceof WSPasswordCallback pc) { // CXF
                    if (getKeyAndCertAlias().equals(pc.getIdentifier())) {
                        pc.setPassword(KeystoreUtils.getKeyStorePassword());
                        break;
                    }
                }
            }
        }
    }

    private static final Crypto CRYPTO = getCrypto();
    private static final StaticSTSProperties STS_PROPERTIES = initSTSProperties(CRYPTO);

    private final TokenIssueOperation issueOperation = new TokenIssueOperation() {
        @Override
        protected org.apache.cxf.sts.token.provider.TokenProviderParameters createTokenProviderParameters(RequestRequirements requestRequirements,
                                                                                                          Principal principal,
                                                                                                          Map<String, Object> messageContext) {
            TokenProviderParameters providerParameters = super.createTokenProviderParameters(requestRequirements, principal, messageContext);
            TokenRequirements tokenRequirements = providerParameters.getTokenRequirements();
            tokenRequirements.setOnBehalfOf(null);
            tokenRequirements.setActAs(null);
            return providerParameters;
        }
    };

    public STSIssueResponseGenerator() {
        initTokenProviders(issueOperation);
        issueOperation.setStsProperties(STS_PROPERTIES);

        // Add Service
        // ServiceMBean service = new StaticService();
        // service.setEndpoints(Collections.singletonList("http://dummy-service.com/dummy"));
        // issueOperation.setServices(Collections.singletonList(service));

        // Mock up a request

    }

    private Map<String, Object> createMessageContext(Principal principal) {
        Map<String, Object> messageContext = new HashMap<>();
        messageContext.put(
            SecurityContext.class.getName(),
            createSecurityContext(principal));
        return messageContext;
    }

    public RequestSecurityTokenResponseCollectionType buildRequestSecurityTokenResponseCollectionType(RequestSecurityTokenCollectionType requestCollection) {
        Principal principal = new CustomTokenPrincipal(USERNAME);
        Map<String, Object> messageContext = createMessageContext(principal);
        return issueOperation.issue(requestCollection, principal, messageContext);
    }

    /** Issue a token as part of collection */
    public RequestSecurityTokenResponseCollectionType buildRequestSecurityTokenResponseCollectionType(RequestSecurityTokenType request) {
        Principal principal = new CustomTokenPrincipal(USERNAME);
        Map<String, Object> messageContext = createMessageContext(principal);
        return issueOperation.issue(request, principal, messageContext);
    }

    /** Issue a single token */
    public RequestSecurityTokenResponseType buildRequestSecurityTokenResponseType(RequestSecurityTokenType request) {
        Principal principal = new CustomTokenPrincipal(USERNAME);
        Map<String, Object> messageContext = createMessageContext(principal);
        return issueOperation.issueSingle(request, principal, messageContext);
    }

    /** Issue a single token */
    public RequestSecurityTokenResponseType buildRequestSecurityTokenResponseType(String tokenType){
        ObjectFactory of = new ObjectFactory();
        RequestSecurityTokenType request2 = of.createRequestSecurityTokenType();
        request2.getAny().add(of.createTokenType(tokenType));
        request2.getAny().add(of.createRequestType("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue"));

        Principal principal = new CustomTokenPrincipal(USERNAME);
        Map<String, Object> messageContext = createMessageContext(principal);
        return issueOperation.issueSingle(request2, principal, messageContext);
    }

    private static SecurityContext createSecurityContext(final Principal p) {
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

    private static Properties getEncryptionProperties() {
        WSSConfig.init();
        Properties properties = new Properties();
        properties.put(
            "org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        properties.put("org.apache.wss4j.crypto.merlin.keystore.password", KeystoreUtils.getKeyStorePassword());
        properties.put("org.apache.wss4j.crypto.merlin.keystore.file", KeystoreUtils.getKeystoreFilePath());

        return properties;
    }

    private static synchronized Crypto getCrypto() {
        try {
            return CryptoFactory.getInstance(getEncryptionProperties());
        } catch (WSSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    private static StaticSTSProperties initSTSProperties(Crypto CRYPTO) {
        // Add STSProperties object
        String alias = getKeyAndCertAlias();
        StaticSTSProperties stsProperties = new StaticSTSProperties();
        stsProperties.setEncryptionCrypto(CRYPTO);
        stsProperties.setSignatureCrypto(CRYPTO);
        stsProperties.setCallbackHandler(new PasswordCallbackHandler());
        stsProperties.setSignatureUsername(alias);
        stsProperties.setIssuer("VTP");
        return stsProperties;
    }

    private static void initTokenProviders(TokenIssueOperation issueOperation) {
        // Add Token Provider
        List<TokenProvider> providerList = new ArrayList<>();
        providerList.add(new SAMLTokenProvider());
        issueOperation.setTokenProviders(providerList);

        // Add TokenDelegationHandler for onBehalfOf
        List<TokenDelegationHandler> handlers = new ArrayList<>();
        handlers.add(new UsernameTokenDelegationHandler() {
            @Override
            public boolean canHandleToken(ReceivedToken delegateTarget) {
                return true;
            }
        });
        issueOperation.setDelegationHandlers(handlers);
    }

}
