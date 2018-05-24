package no.nav.sigrun;

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
public class SigrunMock implements Provider<DataSource> {

    public DataSource invoke(DataSource request) {
        return new ByteArrayDataSource(buildPermitResponse().getBytes(),"application/json");
    }

    private String buildPermitResponse() {
        return "[\n" +
                "  {\n" +
                "    \"tekniskNavn\": \"personinntektFiskeFangstFamiliebarnehage\",\n" +
                "    \"verdi\": \"814952\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"tekniskNavn\": \"personinntektNaering\",\n" +
                "    \"verdi\": \"785896\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"tekniskNavn\": \"personinntektBarePensjonsdel\",\n" +
                "    \"verdi\": \"844157\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"tekniskNavn\": \"svalbardLoennLoennstrekkordningen\",\n" +
                "    \"verdi\": \"874869\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"tekniskNavn\": \"personinntektLoenn\",\n" +
                "    \"verdi\": \"746315\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"tekniskNavn\": \"svalbardPersoninntektNaering\",\n" +
                "    \"verdi\": \"696009\"\n" +
                "  }\n" +
                "]";
    }
}
