package no.nav.foreldrepenger.vtp.server.api.kafka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;

@Tag(name = "Kafka services")
@Path("/api/kafka")
public class KafkaRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaRestTjeneste.class);

    @Context
    private LocalKafkaProducer localKafkaProducer;
    @Context
    private AdminClient kafkaAdminClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/topics")
    @Operation(description = "Returnerer kafka topics", responses = {
            @ApiResponse(responseCode = "OK", description = "liste av kafka topics", content = @Content(schema = @Schema(implementation  = String[].class))),
    })
    public Response getTopics() throws InterruptedException, ExecutionException {
        ArrayList<KafkaTopicDto> list = new ArrayList<>();
        Map<String, TopicListing> topics = kafkaAdminClient.listTopics().namesToListings().get();
        for (Map.Entry<String, TopicListing> entry : topics.entrySet()) {
            KafkaTopicDto dto = new KafkaTopicDto();
            dto.setName(entry.getKey());
            dto.setInternal(entry.getValue().isInternal());
            list.add(dto);
        }
        return Response
                .status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(list)
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/topics/{topic}")
    @Operation(description = "Oppretter ny (tom) Kafka topic.")
    public Response createTopic(@PathParam("topic") String topic) {
        LOG.info("Request: oppretter topic: {}", topic);
        kafkaAdminClient.createTopics(Collections.singleton(new NewTopic(topic, 1, (short) 1)));

        return Response
                .status(Response.Status.CREATED)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/send/{topic}")
    @Operation(description = "Legger melding på Kafka topic")
    public Response sendMessage(@PathParam("topic") String topic, @QueryParam("key") String key, String message) {
        LOG.info("Request: send message to topic [{}]: {}", topic, message);
        localKafkaProducer.sendMelding(topic, key, message);

        return Response
                .status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON)
                .entity(message)
                .build();
    }
}
