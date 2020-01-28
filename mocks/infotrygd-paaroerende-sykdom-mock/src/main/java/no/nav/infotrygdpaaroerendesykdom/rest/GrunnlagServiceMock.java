package no.nav.infotrygdpaaroerendesykdom.rest;

import io.swagger.api.GrunnlagApiService;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDate;

public class GrunnlagServiceMock implements GrunnlagApiService {
    @Override
    public Response paaroerendeSykdomUsingGET(String fnr, LocalDate fom, LocalDate tom, SecurityContext securityContext) {
        return null;
    }
}
