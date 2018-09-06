package no.nav.foreldrepenger.fpmock2.server;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ContextResolver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.swagger.jaxrs.config.BeanConfig;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioRestTjeneste;
import no.nav.foreldrepenger.fpmock2.server.checks.IsAliveImpl;
import no.nav.foreldrepenger.fpmock2.server.checks.IsReadyImpl;
import no.nav.sigrun.SigrunMock;

public class ApplicationConfig extends Application {

    public static final String API_URI = "/api";

    public ApplicationConfig() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setSchemes(new String[] { "http" });
        beanConfig.setBasePath("/api");
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
        classes.add(TestscenarioRestTjeneste.class);

        // tekniske ting
        classes.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        classes.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        classes.add(IsAliveImpl.class);
        classes.add(IsReadyImpl.class);
        classes.add(JacksonConfigResolver.class);

        return classes;
    }

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

}
