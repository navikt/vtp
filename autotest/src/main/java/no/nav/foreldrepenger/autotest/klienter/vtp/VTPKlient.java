package no.nav.foreldrepenger.autotest.klienter.vtp;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;

public class VTPKlient extends JsonRest{

    public VTPKlient(HttpSession session) {
        super(session);
    }
    
    @Override
    public String hentRestRotUrl() {
        return System.getProperty("autotest.vtp.url")+":" + System.getProperty("autotest.vtp.port") + "/api";
    }

    @Override
    protected ObjectMapper hentObjectMapper() {
        ObjectMapper mapper = super.hentObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.PROTECTED_AND_PUBLIC);
        mapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
        return mapper;
    }

    
}
