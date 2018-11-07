package no.nav.foreldrepenger.fpmock2.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.swagger.jaxrs.config.BeanConfig;
import no.nav.foreldrepenger.fpmock2.server.api.journalforing.JournalforingRestTjeneste;
import no.nav.foreldrepenger.fpmock2.server.api.sak.SakRestTjeneste;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioRestTjeneste;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioTemplateRestTjeneste;
import no.nav.foreldrepenger.fpmock2.server.rest.AutotestRestService;
import no.nav.foreldrepenger.fpmock2.server.rest.IsAliveImpl;
import no.nav.foreldrepenger.fpmock2.server.rest.IsReadyImpl;
import no.nav.foreldrepenger.fpmock2.server.rest.Oauth2RestService;
import no.nav.foreldrepenger.fpmock2.server.rest.PdpRestTjeneste;
import no.nav.sigrun.SigrunMock;

public class ApplicationConfig extends Application {

    public static final String API_URI = "/";

    public ApplicationConfig() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setSchemes(new String[] { "https", "http" });
        beanConfig.setBasePath(API_URI);
        beanConfig.setResourcePackage("no.nav");
        beanConfig.setTitle("VLMock2 - Virtualiserte Tjenester");
        beanConfig.setDescription("REST grensesnitt for VTP.");
        beanConfig.setScan(true);
    }

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> classes = new HashSet<>();
        // funksjonelle mocks for rest
        classes.add(SigrunMock.class);
        classes.add(TestscenarioTemplateRestTjeneste.class);
        classes.add(TestscenarioRestTjeneste.class);
        classes.add(JournalforingRestTjeneste.class);
        classes.add(SakRestTjeneste.class);

        // tester
        classes.add(AutotestRestService.class);

        // tekniske ting
        classes.add(Oauth2RestService.class);
        classes.add(PdpRestTjeneste.class);
        
        classes.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        classes.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        classes.add(IsAliveImpl.class);
        classes.add(IsReadyImpl.class);
        classes.add(JacksonConfigResolver.class);
        classes.add(MyExceptionMapper.class);
        

        return classes;
    }

    @Provider
    @Produces(MediaType.APPLICATION_JSON)
    public static class JacksonConfigResolver implements ContextResolver<ObjectMapper> {
        private final ObjectMapper objectMapper = new ObjectMapper();

        public JacksonConfigResolver() {
            objectMapper.registerModule(new Jdk8Module());
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        @Override
        public ObjectMapper getContext(Class<?> type) {
            return objectMapper;
        }
    }

    @Provider
    public static class MyExceptionMapper implements ExceptionMapper<NotFoundException> {
        
        private static final Logger log = LoggerFactory.getLogger(MyExceptionMapper.class);

        @Context HttpServletRequest req; 

        @Override
        public Response toResponse(NotFoundException exception) {
            
            String fullUrl = getFullURL(req);

            Response response = exception.getResponse();
            if (fullUrl.contains("favicon.ico")) {
                return response;
            }

            StringBuilder logMsg = new StringBuilder("NOT_FOUND: ").append(req.getMethod()).append(" ").append(fullUrl);
            for (String header : Collections.list(req.getHeaderNames())) {
                logMsg.append("\n\t").append(header).append("=").append(req.getHeader(header));
            }

            try (BufferedReader br = req.getReader()) {
                br.lines().forEach(line -> logMsg.append("\n\t").append(line));
            } catch (IOException e) {
               log.error("Kunne ikke lese request", e);
               return response;
            }

            String logMessage = logMsg.toString();
            log.warn(logMessage);
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(logMessage).build();
        }
    }
    

    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}
