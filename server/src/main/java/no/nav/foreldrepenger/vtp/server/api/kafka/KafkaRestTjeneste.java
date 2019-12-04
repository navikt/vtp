package no.nav.foreldrepenger.vtp.server.api.kafka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaServer;

@Api(tags = "Kafka services")
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
    @ApiOperation(value = "", notes = ("Returnerer kafka topics"), response = ArrayList.class)
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
    @ApiOperation(value = "", notes = ("Oppretter ny (tom) Kafka topic."))
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
    @ApiOperation(value = "", notes = ("Legger melding på Kafka topic"))
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
