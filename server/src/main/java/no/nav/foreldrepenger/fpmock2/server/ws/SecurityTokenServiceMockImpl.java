package no.nav.foreldrepenger.fpmock2.server.ws;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

import org.apache.cxf.ws.security.sts.provider.SecurityTokenService;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenCollectionType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseCollectionType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenType;

/** Mock implementation of STS service for WS-Trust. */
@WebService(targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512?wsdl", name = "SecurityTokenService")
@XmlSeeAlso({ org.apache.cxf.ws.security.sts.provider.model.ObjectFactory.class,
        org.apache.cxf.ws.security.sts.provider.model.wstrust14.ObjectFactory.class,
        org.apache.cxf.ws.security.sts.provider.model.secext.ObjectFactory.class,
        org.apache.cxf.ws.security.sts.provider.model.utility.ObjectFactory.class,
        org.apache.cxf.ws.security.sts.provider.model.xmldsig.ObjectFactory.class,
        org.apache.cxf.ws.addressing.ObjectFactory.class })
@HandlerChain(file = "Handler-chain.xml")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@Addressing()
public class SecurityTokenServiceMockImpl implements SecurityTokenService {

    @Resource
    private WebServiceContext ws;

    @Override
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/KET", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/KETFinal")
    @WebMethod(operationName = "KeyExchangeToken", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/KET")
    public RequestSecurityTokenResponseType keyExchangeToken(
                                                             @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenType request) {
        // TODO - må verifisers dersom behov
        return new STSIssueResponseGenerator().buildRequestSecurityTokenResponseType(request);
    }

    @Override
    @WebResult(name = "RequestSecurityTokenResponseCollection", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "responseCollection")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTRC/IssueFinal")
    @WebMethod(operationName = "Issue", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue")
    public RequestSecurityTokenResponseCollectionType issue(
                                                            @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenType request) {

        return new STSIssueResponseGenerator().buildRequestSecurityTokenResponseCollectionType(request);

    }

    @Override
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTRC/IssueFinal")
    @WebMethod(action = "Issue")
    public RequestSecurityTokenResponseType issueSingle(
                                                        @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenType request) {
        return new STSIssueResponseGenerator().buildRequestSecurityTokenResponseType(request);
    }

    @Override
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Cancel", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/CancelFinal")
    @WebMethod(operationName = "Cancel", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Cancel")
    public RequestSecurityTokenResponseType cancel(
                                                   @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenType request) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Validate", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/ValidateFinal")
    @WebMethod(operationName = "Validate", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Validate")
    public RequestSecurityTokenResponseType validate(
                                                     @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenType request) {
        // TODO - må verifiseres dersom behov
        return new STSIssueResponseGenerator().buildRequestSecurityTokenResponseType(request);
    }

    @Override
    @WebResult(name = "RequestSecurityTokenResponseCollection", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "responseCollection")
    @WebMethod(operationName = "RequestCollection")
    public RequestSecurityTokenResponseCollectionType requestCollection(
                                                                        @WebParam(partName = "requestCollection", name = "RequestSecurityTokenCollection", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenCollectionType requestCollection) {

        return new STSIssueResponseGenerator().buildRequestSecurityTokenResponseCollectionType(requestCollection);
    }

    @Override
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Renew", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/RenewFinal")
    @WebMethod(operationName = "Renew", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Renew")
    public RequestSecurityTokenResponseType renew(
                                                  @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512") RequestSecurityTokenType request) {
        return new STSIssueResponseGenerator().buildRequestSecurityTokenResponseType(request);
    }

}
