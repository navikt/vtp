package no.nav.abac.pdp;

import javax.mail.util.ByteArrayDataSource;
import javax.activation.DataSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.http.HTTPBinding;

@WebServiceProvider
@ServiceMode(value = javax.xml.ws.Service.Mode.MESSAGE)
@BindingType(value = HTTPBinding.HTTP_BINDING)
public class PdpMock implements Provider<DataSource> {

    @Override
    public DataSource invoke(DataSource request) {
        return new ByteArrayDataSource(buildPermitResponse().getBytes(),"application/json");
    }

    private String buildPermitResponse() {
        return " { \"Response\" : {\"Decision\" : \"Permit\",\"Status\" : {\"StatusCode\" : {\"Value\" : " +
                "\"urn:oasis:names:tc:xacml:1.0:status:ok\",\"StatusCode\" : {\"Value\" : " +
                "\"urn:oasis:names:tc:xacml:1.0:status:ok\"}}}}}";
    }
}
