package no.nav.engangsstønad.mock.checks;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.soap.Addressing;

@Addressing
@WebServiceProvider
@ServiceMode(value = javax.xml.ws.Service.Mode.MESSAGE)
@BindingType(value = HTTPBinding.HTTP_BINDING)
public class IsAliveImpl implements Provider<DataSource> {

    @Override
    public DataSource invoke(DataSource request) {
        return new ByteArrayDataSource(buildPermitResponse().getBytes(),"application/json");
    }

    private String buildPermitResponse() {
        return " { \"Response\" : {\"Status\" : {\"StatusCode\" : {\"Value\" : " +
                "\"status:ok\"}}}}";
    }
}
