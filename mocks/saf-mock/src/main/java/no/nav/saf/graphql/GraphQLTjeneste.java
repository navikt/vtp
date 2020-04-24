package no.nav.saf.graphql;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.AsyncSerialExecutionStrategy;
import graphql.execution.SimpleDataFetcherExceptionHandler;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.saf.Arkivsaksystem;
import no.nav.saf.AvsenderMottaker;
import no.nav.saf.AvsenderMottakerIdType;
import no.nav.saf.Bruker;
import no.nav.saf.BrukerIdType;
import no.nav.saf.Datotype;
import no.nav.saf.DokumentInfo;
import no.nav.saf.Dokumentoversikt;
import no.nav.saf.Dokumentstatus;
import no.nav.saf.Dokumentvariant;
import no.nav.saf.Journalpost;
import no.nav.saf.Journalposttype;
import no.nav.saf.Journalstatus;
import no.nav.saf.Kanal;
import no.nav.saf.RelevantDato;
import no.nav.saf.Sak;
import no.nav.saf.SideInfo;
import no.nav.saf.SkjermingType;
import no.nav.saf.Tema;
import no.nav.saf.Variantformat;

import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GraphQLTjeneste {

    private static final String SCHEME_PATH = "schemas/saf.graphql";

    private TypeDefinitionRegistry typeRegistry;
    private SchemaGenerator schemaGenerator;

    private static GraphQLTjeneste instance;

    public static synchronized GraphQLTjeneste getInstance(){

        if(instance == null){
            instance = new GraphQLTjeneste();
            instance.init();
        }

        return instance;
    }


    public void init() {
        SchemaParser schemaParser = new SchemaParser();
        schemaGenerator = new SchemaGenerator();

        InputStreamReader schema = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(SCHEME_PATH)));
        typeRegistry = schemaParser.parse(schema);
    }

    public ExecutionResult executeStatement(GraphQLRequest request, JournalRepository testscenarioRepository) {
        if (request.getQuery().contains("dokumentoversiktFagsak(fagsak:")) {
            return utførdokumentOversiktFagsak(request, testscenarioRepository);
        } else if (request.getQuery().contains("journalpost(journalpostId:")) {
            return utførJournalpost(request, testscenarioRepository);
        }
        throw new UnsupportedOperationException("Query er ikke støttet i mock");
    }

    private ExecutionResult utførdokumentOversiktFagsak(GraphQLRequest request, JournalRepository testscenarioRepository) {
        String fagsakId = (String) request.getVariables().getOrDefault("fagsakId", "87654321");
        String fagsaksystem = (String) request.getVariables().getOrDefault("fagsaksystem", "87654321");

        RuntimeWiring runtimeWiring = DokumentoversiktWiring.lagRuntimeWiring(opprettDokumentsiktFagsak(fagsaksystem, fagsakId, testscenarioRepository));
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        return GraphQL.newGraphQL(graphQLSchema)
                .mutationExecutionStrategy(new AsyncSerialExecutionStrategy(new SimpleDataFetcherExceptionHandler()))
                .queryExecutionStrategy(new AsyncExecutionStrategy(new SimpleDataFetcherExceptionHandler()))
                .build().execute(ExecutionInput.newExecutionInput()
                .query(request.getQuery())
                .operationName(request.getOperationName())
                .variables(request.getVariables() == null ? Collections.emptyMap() : request.getVariables())
                .build());
    }

    private ExecutionResult utførJournalpost(GraphQLRequest request, JournalRepository testscenarioRepository) {
        String journalpostId = (String) request.getVariables().getOrDefault("journalpostId", "87654321");

        RuntimeWiring runtimeWiring = DokumentWiring.lagRuntimeWiring(opprettJournalpostFeed(journalpostId, testscenarioRepository));
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        return GraphQL.newGraphQL(graphQLSchema)
                .mutationExecutionStrategy(new AsyncSerialExecutionStrategy(new SimpleDataFetcherExceptionHandler()))
                .queryExecutionStrategy(new AsyncExecutionStrategy(new SimpleDataFetcherExceptionHandler()))
                .build().execute(ExecutionInput.newExecutionInput()
                .query(request.getQuery())
                .operationName(request.getOperationName())
                .variables(request.getVariables() == null ? Collections.emptyMap() : request.getVariables())
                .build());
    }

    private DokumentoversiktFagsakCoordinator opprettDokumentsiktFagsak(String fagsakId, String fagsystem, JournalRepository journalRepository) {

        //TODO: legg inn logikk for å bygge hentJournalpost-response

        return new DokumentoversiktFagsakCoordinator() {


            @Override
            public Dokumentoversikt hentDokumentoversikt(String fagsakId, String fagsystem) {
                Journalpost journalpost = byggJournalpost("journalpostId");

                return new Dokumentoversikt(List.of(journalpost), new SideInfo("sluttpeker", false));

            }
        };
    }

    private Journalpost byggJournalpost(String journalpostId) {
        // TODO: Bygge builder rundt alt dette vrælet, så slipper man at det brekker for hver kontraktsendring
        RelevantDato registrert = new RelevantDato(Date.from(Instant.now()), Datotype.DATO_REGISTRERT);
        RelevantDato journalført = new RelevantDato(Date.from(Instant.now()), Datotype.DATO_JOURNALFOERT);
        String filtype = "PDF";
        Dokumentvariant dokumentvariant = new Dokumentvariant(Variantformat.ARKIV, "filnavn", "filuuid", filtype, true, SkjermingType.FEIL);
        DokumentInfo dokumenter = new DokumentInfo("dokumentInfoId", "tittel", "brevkode", Dokumentstatus.FERDIGSTILT,
                Date.from(Instant.now()), "originalJournalpostId", "skjerming", List.of(), List.of(dokumentvariant));
        return new Journalpost(
                journalpostId,
                "tittel",
                Journalposttype.I,
                Journalstatus.JOURNALFOERT,
                Tema.AAP,
                "temanavn",
                "behandlingstema",
                "behandlingstemanavn",
                new Sak("arkivsaksnummer", Arkivsaksystem.GSAK, Date.from(Instant.now()), "fagsakId", "fagsaksystem"),
                new Bruker("id", BrukerIdType.AKTOERID),
                new AvsenderMottaker("id", AvsenderMottakerIdType.FNR, "navn", "land", true),
                "avsenderMottakerId",
                "avsenderMottakerNavn",
                "avsenderMottakrLand",
                "journalForendeEnhet",
                "journalFoerendeEnhet",
                "journalfortAvNavn",
                "opprettetAvNavn",
                Kanal.ALTINN,
                "kanalnavn",
                "skjerming",
                Date.from(Instant.now()),
                List.of(registrert, journalført),
                "antallRetur",
                "eksternReferanseId",
                List.of(),
                List.of(dokumenter)
        );
    }

    private JournalpostCoordinator opprettJournalpostFeed(String journalpostId, JournalRepository journalRepository) {

        //TODO: legg inn logikk for å bygge hentJournalpost-response

        return new JournalpostCoordinator() {
            @Override
            public Journalpost hentJournalpost(String journalpostId) {

                // TODO: Bygge builder rundt alt dette vrælet, så slipper man at det brekker for hver kontraktsendring
                return byggJournalpost(journalpostId);
            }
        };
    }

}
