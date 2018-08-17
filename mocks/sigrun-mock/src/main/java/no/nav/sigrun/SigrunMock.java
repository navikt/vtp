package no.nav.sigrun;

import javax.activation.DataSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.soap.Addressing;

import com.sun.xml.internal.ws.util.ByteArrayDataSource;

@Addressing
@WebServiceProvider
@ServiceMode(value = javax.xml.ws.Service.Mode.MESSAGE)
@BindingType(value = HTTPBinding.HTTP_BINDING)
public class SigrunMock implements Provider<DataSource> {

    @Override
    public DataSource invoke(DataSource request) {
        return new ByteArrayDataSource(buildPermitResponse().getBytes(), "application/json");
    }

    private String buildPermitResponse() {
        return "[\n" +
            "  {\n" +
            "    \"tekniskNavn\": \"personinntektFiskeFangstFamiliebarnehage\",\n" +
            "    \"verdi\": \"50000\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"tekniskNavn\": \"personinntektNaering\",\n" +
            "    \"verdi\": \"50000\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"tekniskNavn\": \"personinntektBarePensjonsdel\",\n" +
            "    \"verdi\": \"50000\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"tekniskNavn\": \"svalbardLoennLoennstrekkordningen\",\n" +
            "    \"verdi\": \"5000\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"tekniskNavn\": \"personinntektLoenn\",\n" +
            "    \"verdi\": \"50000\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"tekniskNavn\": \"svalbardPersoninntektNaering\",\n" +
            "    \"verdi\": \"50000\"\n" +
            "  }\n" +
            "]";
    }
}
