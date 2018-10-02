package no.nav.foreldrepenger.fpmock2.server.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

import org.apache.cxf.ws.security.sts.provider.SecurityTokenService;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenCollectionType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseCollectionType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Mock implementation of STS service for WS-Trust. */
@WebService(targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/wsdl", name = "SecurityTokenService")
@XmlSeeAlso({ org.apache.cxf.ws.security.sts.provider.model.ObjectFactory.class,
        org.apache.cxf.ws.security.sts.provider.model.wstrust14.ObjectFactory.class,
        org.apache.cxf.ws.security.sts.provider.model.secext.ObjectFactory.class,
        org.apache.cxf.ws.security.sts.provider.model.utility.ObjectFactory.class,
        org.apache.cxf.ws.security.sts.provider.model.xmldsig.ObjectFactory.class,
        org.apache.cxf.ws.addressing.ObjectFactory.class })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class SecurityTokenServiceMockImpl implements SecurityTokenService {

    private static final Logger log = LoggerFactory.getLogger(SecurityTokenServiceMockImpl.class);

    @Override
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/KET", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/KETFinal")
    @WebMethod(operationName = "KeyExchangeToken")
    public RequestSecurityTokenResponseType keyExchangeToken(
                                                             @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenType request) {
        logRequestSecurityTokenType(request, "keyExchangeToken");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @WebResult(name = "RequestSecurityTokenResponseCollection", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "responseCollection")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTRC/IssueFinal")
    @WebMethod(operationName = "Issue")
    public RequestSecurityTokenResponseCollectionType issue(
                                                            @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenType request) {
        logRequestSecurityTokenType(request, "issue");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTRC/IssueFinal")
    @WebMethod(operationName = "Issue")
    public RequestSecurityTokenResponseType issueSingle(
                                                        @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenType request) {
        logRequestSecurityTokenType(request, "issueSingle");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Cancel", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/CancelFinal")
    @WebMethod(operationName = "Cancel")
    public RequestSecurityTokenResponseType cancel(
                                                   @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenType request) {
        logRequestSecurityTokenType(request, "cancel");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Validate", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/ValidateFinal")
    @WebMethod(operationName = "Validate")
    public RequestSecurityTokenResponseType validate(
                                                     @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenType request) {
        logRequestSecurityTokenType(request, "validate");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @WebResult(name = "RequestSecurityTokenResponseCollection", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "responseCollection")
    @WebMethod(operationName = "RequestCollection")
    public RequestSecurityTokenResponseCollectionType requestCollection(
                                                                        @WebParam(partName = "requestCollection", name = "RequestSecurityTokenCollection", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenCollectionType requestCollection) {
        for (RequestSecurityTokenType rs : requestCollection.getRequestSecurityToken()) {
            logRequestSecurityTokenType(rs, "requestCollection");
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Renew", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/RenewFinal")
    @WebMethod(operationName = "Renew")
    public RequestSecurityTokenResponseType renew(
                                                  @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenType request) {
        logRequestSecurityTokenType(request, "renew");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void logRequestSecurityTokenType(RequestSecurityTokenType request, String method) {
        StringBuilder sb = new StringBuilder(4000);
        sb.append("Not Implemented: " + method + ". Context=" + request.getContext() + "; otherAttributes=" + request.getOtherAttributes() + "; any="
            + request.getAny());
        log.info(sb.toString());
    }

}
