package no.nav.foreldrepenger.autotest.klienter.fpoppdrag;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;

public class FPOppdragKlient extends JsonRest {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); //Sets serialization format of LocalDate to: "yyyy-mm-dd";
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC);
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
    }


    public FPOppdragKlient(HttpSession session) {
        super(session);
    }

    @Override
    protected ObjectMapper hentObjectMapper() {
        return mapper;
    }

    @Override
    public String hentRestRotUrl() {
        return System.getProperty("autotest.fpoppdrag.http.routing.api");
    }
}
