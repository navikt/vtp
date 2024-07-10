package no.nav.foreldrepenger.vtp.server;

import static java.util.logging.Level.FINE;
import static org.glassfish.jersey.logging.LoggingFeature.Verbosity.PAYLOAD_ANY;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.GenericOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;
import no.nav.axsys.AxsysEnhetstilgangMock;
import no.nav.axsys.AxsysEnhetstilgangV2Mock;
import no.nav.digdir.DigdirKrrProxyMock;
import no.nav.dokarkiv.JournalpostMock;
import no.nav.dokdistfordeling.DokdistfordelingMock;
import no.nav.foreldrepenger.fpwsproxy.arena.FpWsProxyArenaMock;
import no.nav.foreldrepenger.fpwsproxy.oppdrag.FpWsProxySimuleringOppdragMock;
import no.nav.foreldrepenger.fpwsproxy.tilbakekreving.FpWsProxyTilbakekrevingMock;
import no.nav.foreldrepenger.fpwsproxy.tilbakekreving.TilbakekrevingKonsistensTjeneste;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.server.api.journalforing.JournalforingRestTjeneste;
import no.nav.foreldrepenger.vtp.server.api.kafka.KafkaRestTjeneste;
import no.nav.foreldrepenger.vtp.server.api.pdl.PdlLeesahRestTjeneste;
import no.nav.foreldrepenger.vtp.server.api.scenario.TestscenarioRestTjeneste;
import no.nav.foreldrepenger.vtp.server.api.scenario.TestscenarioV2RestTjeneste;
import no.nav.foreldrepenger.vtp.server.auth.rest.abac.PdpRestTjeneste;
import no.nav.foreldrepenger.vtp.server.auth.rest.azuread.AzureAdRestTjeneste;
import no.nav.foreldrepenger.vtp.server.auth.rest.azuread.MicrosoftGraphApiMock;
import no.nav.foreldrepenger.vtp.server.auth.rest.idporten.IdportenLoginTjeneste;
import no.nav.foreldrepenger.vtp.server.auth.rest.sts.STSRestTjeneste;
import no.nav.foreldrepenger.vtp.server.auth.rest.tokenx.TokenxRestTjeneste;
import no.nav.foreldrepenger.vtp.server.selftest.IsAliveImpl;
import no.nav.foreldrepenger.vtp.server.selftest.IsReadyImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioBuilderRepository;
import no.nav.infotrygdpaaroerendesykdom.rest.PårørendeSykdomMock;
import no.nav.medl2.rest.api.v1.MedlemskapsunntakMock;
import no.nav.mock.pesys.UføreMock;
import no.nav.nom.SkjermetPersonMock;
import no.nav.omsorgspenger.rammemeldinger.OmsorgspengerMock;
import no.nav.oppgave.OppgaveMockImpl;
import no.nav.pdl.PdlMock;
import no.nav.pip.PdlPipMock;
import no.nav.saf.SafMock;
import no.nav.sigrun.SigrunMock;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.rest.ArbeidsfordelingRestMock;
import no.nav.tjeneste.virksomhet.arbeidsforhold.rs.AaregRSV1Mock;
import no.nav.tjeneste.virksomhet.infotrygd.rest.InfotrygdMock;
import no.nav.tjeneste.virksomhet.organisasjon.rs.OrganisasjonRSV1Mock;
import no.nav.tjeneste.virksomhet.sak.rs.SakRestMock;
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;
import no.nav.tjeneste.virksomhet.spokelse.rest.SpøkelseMock;
import no.nav.vtp.DummyRestTjeneste;
import no.nav.vtp.DummyRestTjenesteBoolean;
import no.nav.vtp.DummyRestTjenesteFile;
import no.nav.vtp.hentinntektlistebolk.HentInntektlisteBolkREST;

@ApplicationPath(ApplicationConfigJersey.API_URI)
public class ApplicationConfigJersey extends ResourceConfig {

    public static final String API_URI = "/rest";

    public ApplicationConfigJersey() {
        setApplicationName("VTP");
        packages("no.nav", "com.fasterxml.jackson.jaxrs.json");
        register(new LoggingFeature(java.util.logging.Logger.getLogger(getClass().getName()), FINE, PAYLOAD_ANY, 10000));
        registerClasses(registerClasses());
        instanserSwagger();
    }

    private void instanserSwagger() {
        var oas = new OpenAPI();
        var info = new Info().title("VTP - Virtuell Tjeneste Plattform").version("1.0").description("REST grensesnitt for VTP.");

        oas.info(info).addServersItem(new Server().url("/"));
        var oasConfig = new SwaggerConfiguration().openAPI(oas)
                .prettyPrint(true)
                .resourceClasses(getClasses().stream().map(Class::getName).collect(Collectors.toSet()));
        try {
            new GenericOpenApiContextBuilder<>().openApiConfiguration(oasConfig).buildContext(true).read();
        } catch (OpenApiConfigurationException e) {
            throw new IllegalStateException("OPEN-API", e);
        }
    }

    public static Set<Class<?>> registerClasses() {
        Set<Class<?>> classes = new HashSet<>();
        // funksjonelle mocks for rest
        classes.add(SigrunMock.class);
        classes.add(JournalpostMock.class);
        classes.add(InfotrygdMock.class);
        classes.add(ArbeidsfordelingRestMock.class);
        classes.add(TestscenarioRestTjeneste.class);
        classes.add(TestscenarioV2RestTjeneste.class);
        classes.add(JournalforingRestTjeneste.class);
        classes.add(SakRestMock.class);
        classes.add(SafMock.class);
        classes.add(PdlLeesahRestTjeneste.class);
        classes.add(HentInntektlisteBolkREST.class);
        classes.add(DummyRestTjeneste.class);
        classes.add(DummyRestTjenesteFile.class);
        classes.add(DummyRestTjenesteBoolean.class);
        classes.add(MedlemskapsunntakMock.class);
        classes.add(OmsorgspengerMock.class);
        classes.add(OppgaveMockImpl.class);
        classes.add(OrganisasjonRSV1Mock.class);
        classes.add(AaregRSV1Mock.class);
        classes.add(PdlMock.class);
        classes.add(PdlPipMock.class);
        classes.add(PårørendeSykdomMock.class);
        classes.add(DokdistfordelingMock.class);
        classes.add(DigdirKrrProxyMock.class);
        classes.add(AxsysEnhetstilgangMock.class);
        classes.add(AxsysEnhetstilgangV2Mock.class);
        classes.add(SkjermetPersonMock.class);
        classes.add(UføreMock.class);
        classes.add(SpøkelseMock.class);
        classes.add(FpWsProxyArenaMock.class);
        classes.add(FpWsProxySimuleringOppdragMock.class);
        classes.add(FpWsProxyTilbakekrevingMock.class);

        // tekniske ting
        classes.add(AzureAdRestTjeneste.class);
        classes.add(IdportenLoginTjeneste.class);
        classes.add(MicrosoftGraphApiMock.class);
        classes.add(STSRestTjeneste.class);
        classes.add(PdpRestTjeneste.class);
        classes.add(TokenxRestTjeneste.class);

        classes.add(IsAliveImpl.class);
        classes.add(IsReadyImpl.class);
        classes.add(JacksonConfigResolver.class);
        classes.add(MyExceptionMapper.class);
        classes.add(CorsFilter.class); // todo legg på en sjekk på om man kjører på localhost, fjern hvis man er deployed
        classes.add(KafkaRestTjeneste.class);

        classes.add(LocalDateStringConverterProvider.class);
        classes.add(TilbakekrevingKonsistensTjeneste.class);
        classes.add(OpenApiResource.class);

        return classes;
    }

    public static String getFullURL(HttpServletRequest request) {
        var requestURL = new StringBuilder(request.getRequestURL().toString());
        var queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    public ApplicationConfigJersey setup(DelegatingTestscenarioBuilderRepository testScenarioRepository,
                                         TestscenarioRepository instance,
                                         GsakRepo gsakRepo,
                                         LocalKafkaProducer localKafkaProducer,
                                         AdminClient kafkaAdminClient,
                                         JournalRepository journalRepository) {
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(testScenarioRepository).to(TestscenarioBuilderRepository.class);
                bind(instance).to(TestscenarioRepository.class);
                bind(journalRepository).to(JournalRepository.class);
                bind(gsakRepo).to(GsakRepo.class);
                bind(localKafkaProducer).to(LocalKafkaProducer.class);
                bind(kafkaAdminClient).to(AdminClient.class);
            }
        });
        return this;
    }

    @Provider
    @Produces(MediaType.APPLICATION_JSON)
    public static class JacksonConfigResolver implements ContextResolver<ObjectMapper> {
        private final ObjectMapper objectMapper = JsonMapper.builder()
                .addModules(new Jdk8Module(), new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build();

        public JacksonConfigResolver() {
            //CDI
        }

        @Override
        public ObjectMapper getContext(Class<?> type) {
            return objectMapper;
        }
    }

    @Provider
    public static class MyExceptionMapper implements ExceptionMapper<NotFoundException> {

        private static final Logger log = LoggerFactory.getLogger(MyExceptionMapper.class);

        @Context
        HttpServletRequest req;

        @Override
        public Response toResponse(NotFoundException exception) {

            var fullUrl = getFullURL(req);

            var response = exception.getResponse();
            if (fullUrl.contains("favicon.ico")) {
                return response;
            }

            var logMsg = new StringBuilder("NOT_FOUND: ").append(req.getMethod()).append(" ").append(fullUrl);
            for (var header : Collections.list(req.getHeaderNames())) {
                logMsg.append("\n\t").append(header).append("=").append(req.getHeader(header));
            }
            var logMessage = logMsg.toString();

            log.warn(logMessage);

            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(logMessage).build();
        }
    }

    @Provider
    public static class LocalDateStringConverterProvider implements ParamConverterProvider {

        @SuppressWarnings("unchecked")
        @Override
        public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
            if (rawType.isAssignableFrom(LocalDate.class)) {
                return (ParamConverter<T>) new LocalDateStringConverter();
            }
            return null;
        }
    }

    public static class LocalDateStringConverter implements ParamConverter<LocalDate> {
        @Override
        public LocalDate fromString(String s) {
            if (s == null) {
                return null;
            }
            return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
        }

        @Override
        public String toString(LocalDate localDate) {
            if (localDate == null) {
                return null;
            }
            return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    @Provider
    public static class CorsFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
            responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders()
                    .add("Access-Control-Allow-Headers",
                            "content-type, pragma, accept, expires, accept-language, cache-control, accepted-encoding, x-requested-with, "
                                    + "host, origin, content-length, user-agent, referer, connection, cookie, nav-callid, x_nav-callid, authorization");
            responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        }
    }
}
