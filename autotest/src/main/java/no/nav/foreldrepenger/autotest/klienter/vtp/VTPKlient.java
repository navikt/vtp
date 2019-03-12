package no.nav.foreldrepenger.autotest.klienter.vtp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;

public class VTPKlient extends JsonRest{

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); //Sets serialization format of LocalDate to: "yyyy-mm-dd";
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.PROTECTED_AND_PUBLIC);
        mapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
    }

    protected Logger log;

    public VTPKlient(HttpSession session) {
        super(session);
        log = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public String hentRestRotUrl() {
        if (null != System.getenv("AUTOTEST_VTP_BASE_URL")) {
            return System.getenv("AUTOTEST_VTP_BASE_URL") + "/api";
        } else {
            return System.getProperty("autotest.vtp.url")+":" + System.getProperty("autotest.vtp.port") + "/api";
        }

    }

    @Override
    protected ObjectMapper hentObjectMapper() {
        return mapper;
    }


}
