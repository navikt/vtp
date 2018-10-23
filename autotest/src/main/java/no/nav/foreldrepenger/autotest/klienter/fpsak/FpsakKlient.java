package no.nav.foreldrepenger.autotest.klienter.fpsak;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;

public class FpsakKlient extends JsonRest{

    public FpsakKlient(HttpSession session) {
        super(session);
    }

    @Override
    public String hentRestRotUrl() {
        return MiljoKonfigurasjon.getRouteApi();
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
