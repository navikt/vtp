package no.nav.foreldrepenger.vtp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.swagger.jaxrs.config.BeanConfig;
import no.nav.aktoerregister.rest.api.v1.AktoerIdentMock;
import no.nav.dokarkiv.JournalpostMock;
import no.nav.dokdistfordeling.DokdistfordelingMock;
import no.nav.foreldrepenger.vtp.server.api.journalforing.JournalforingRestTjeneste;
import no.nav.foreldrepenger.vtp.server.api.kafka.KafkaRestTjeneste;
import no.nav.foreldrepenger.vtp.server.api.pdl.PdlLeesahRestTjeneste;
import no.nav.foreldrepenger.vtp.server.api.scenario.TestscenarioRestTjeneste;
import no.nav.foreldrepenger.vtp.server.rest.IsAliveImpl;
import no.nav.foreldrepenger.vtp.server.rest.IsReadyImpl;
import no.nav.foreldrepenger.vtp.server.rest.auth.Oauth2RestService;
import no.nav.foreldrepenger.vtp.server.rest.auth.PdpRestTjeneste;
import no.nav.foreldrepenger.vtp.server.rest.auth.STSRestTjeneste;
import no.nav.foreldrepenger.vtp.server.rest.azuread.navansatt.AzureAdNAVAnsattService;
import no.nav.foreldrepenger.vtp.server.rest.azuread.navansatt.MicrosoftGraphApiMock;
import no.nav.infotrygdpaaroerendesykdom.rest.PårørendeSykdomMock;
import no.nav.medl2.rest.api.v1.MedlemskapsunntakMock;
import no.nav.oppgave.OppgaveMockImpl;
import no.nav.pdl.PdlMock;
import no.nav.saf.SafMock;
import no.nav.sigrun.SigrunMock;
import no.nav.tjeneste.fpformidling.FpFormidlingMock;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.rest.ArbeidsfordelingRestMock;
import no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock;
import no.nav.tjeneste.virksomhet.infotrygd.rest.InfotrygdGrunnlagMock;
import no.nav.tjeneste.virksomhet.organisasjon.rs.OrganisasjonRSV1Mock;
import no.nav.tjeneste.virksomhet.sak.rs.SakRestMock;
import no.nav.vtp.DummyRestTjeneste;
import no.nav.vtp.DummyRestTjenesteBoolean;
import no.nav.vtp.hentinntektlistebolk.HentInntektlisteBolkREST;

public class ApplicationConfig extends Application {

    public static final String API_URI = "/rest";

    public ApplicationConfig() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setSchemes(new String[] { "http", "https" });
        beanConfig.setBasePath(API_URI);
        beanConfig.setTitle("VLMock2 - Virtualiserte Tjenester");
        beanConfig.setResourcePackage("no.nav");
        beanConfig.setDescription("REST grensesnitt for VTP.");
        beanConfig.setScan(true);
    }

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> classes = new HashSet<>();
        // funksjonelle mocks for rest
        classes.add(SigrunMock.class);
        classes.add(JournalpostMock.class);
        classes.add(InfotrygdGrunnlagMock.class);
        classes.add(ArbeidsfordelingRestMock.class);
        classes.add(TestscenarioRestTjeneste.class);
        classes.add(JournalforingRestTjeneste.class);
        classes.add(SakRestMock.class);
        classes.add(SafMock.class);
        classes.add(PdlLeesahRestTjeneste.class);
        classes.add(HentInntektlisteBolkREST.class);
        classes.add(FpFormidlingMock.class);
        classes.add(DummyRestTjeneste.class);
        classes.add(DummyRestTjenesteBoolean.class);
        classes.add(MedlemskapsunntakMock.class);
        classes.add(OppgaveMockImpl.class);
        classes.add(OrganisasjonRSV1Mock.class);
        classes.add(AaregRSV1Mock.class);
        classes.add(AktoerIdentMock.class);
        classes.add(PdlMock.class);
        classes.add(PårørendeSykdomMock.class);
        classes.add(DokdistfordelingMock.class);

        // tekniske ting
        classes.add(Oauth2RestService.class);
        classes.add(AzureAdNAVAnsattService.class);
        classes.add(MicrosoftGraphApiMock.class);
        classes.add(STSRestTjeneste.class);
        classes.add(PdpRestTjeneste.class);

        classes.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        classes.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        classes.add(IsAliveImpl.class);
        classes.add(IsReadyImpl.class);
        classes.add(JacksonConfigResolver.class);
        classes.add(MyExceptionMapper.class);
        classes.add(CorsFilter.class); // todo legg på en sjekk på om man kjører på localhost, fjern hvis man er deployed
        classes.add(KafkaRestTjeneste.class);

        classes.add(LocalDateStringConverterProvider.class);

        return classes;
    }

    @Provider
    @Produces(MediaType.APPLICATION_JSON)
    public static class JacksonConfigResolver implements ContextResolver<ObjectMapper> {
        private static final ObjectMapper objectMapper = new ObjectMapper();

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

    @Provider
    public static class LocalDateStringConverterProvider implements ParamConverterProvider {

        @SuppressWarnings("unchecked")
        @Override
        public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
            if(rawType.isAssignableFrom(LocalDate.class)) {
                return (ParamConverter<T>) new LocalDateStringConverter();
            }
            return null;
        }
    }

    public static class LocalDateStringConverter implements ParamConverter<LocalDate> {
        @Override
        public LocalDate fromString(String s) {
            return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
        }

        @Override
        public String toString(LocalDate localDate) {
            return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }


    @Provider
    public static class CorsFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext requestContext,
                           ContainerResponseContext responseContext) throws IOException {
            responseContext.getHeaders().add(
                    "Access-Control-Allow-Origin", "*");
            responseContext.getHeaders().add(
                    "Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add(
                    "Access-Control-Allow-Headers",
                    "content-type, pragma, accept, expires, accept-language, cache-control, accepted-encoding, " +
                        "host, origin, content-length, user-agent, referer, connection, cookie, nav-callid, authorization");
            responseContext.getHeaders().add(
                    "Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD");
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
