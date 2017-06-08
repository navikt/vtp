package no.nav.abac.pdp;

import org.apache.commons.io.IOUtils;

import javax.mail.util.ByteArrayDataSource;
import javax.activation.DataSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.http.HTTPBinding;
import java.io.IOException;

@WebServiceProvider
@ServiceMode(value = javax.xml.ws.Service.Mode.MESSAGE)
@BindingType(value = HTTPBinding.HTTP_BINDING)
public class PdpMock implements Provider<DataSource> {

    @Override
    public DataSource invoke(DataSource request) {
        String xacmlRequest="";
        try {
            xacmlRequest = IOUtils.toString(request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(xacmlRequest.contains("{\"AttributeId\":\"no.nav.abac.attributter.resource.felles.person.fnr\",\"Value\":\"03031751364\"}")||
        xacmlRequest.contains("{\"AttributeId\":\"no.nav.abac.attributter.resource.felles.person.fnr\",\"Value\":\"08059003306\"}")){
            return new ByteArrayDataSource(buildDenyResponse().getBytes(),"application/json");
        }
        return new ByteArrayDataSource(buildPermitResponse().getBytes(),"application/json");
    }

    private String buildDenyResponse()   {
        return " { \"Response\" : {\"Decision\" : \"Deny\",\"Status\" : {\"StatusCode\" : {\"Value\" : " +
            "\"urn:oasis:names:tc:xacml:1.0:status:ok\",\"StatusCode\" : {\"Value\" : " +
            "\"urn:oasis:names:tc:xacml:1.0:status:ok\"}}}}}";
}
    private String buildPermitResponse() {
        return " { \"Response\" : {\"Decision\" : \"Permit\",\"Status\" : {\"StatusCode\" : {\"Value\" : " +
                "\"urn:oasis:names:tc:xacml:1.0:status:ok\",\"StatusCode\" : {\"Value\" : " +
                "\"urn:oasis:names:tc:xacml:1.0:status:ok\"}}}}}";
    }
}
