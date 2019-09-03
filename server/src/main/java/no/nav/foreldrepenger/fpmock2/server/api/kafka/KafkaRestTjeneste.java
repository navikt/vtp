package no.nav.foreldrepenger.fpmock2.server.api.kafka;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.fpmock2.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.fpmock2.kafkaembedded.LocalKafkaServer;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Api(tags = "Kafka services")
@Path("/api/kafka")
public class KafkaRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaRestTjeneste.class);

    @Context
    LocalKafkaProducer localKafkaProducer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/topics")
    @ApiOperation(value = "", notes = ("Returnerer kafka topics"), response = ArrayList.class)
    public Response getTopics() throws InterruptedException, ExecutionException {
        ArrayList<KafkaTopicDto> list = new ArrayList<>();
        Map<String, TopicListing> topics = LocalKafkaServer.getKafkaAdminClient().listTopics().namesToListings().get();
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
    @Path("/send/{topic}")
    @ApiOperation(value = "", notes = ("Legger melding på Kafka topic"))
    public Response sendMessage(@PathParam("topic") String topic, String message) {
        LOG.info("Request: send message to topic [{}]: {}", topic, message);
        localKafkaProducer.sendMelding(topic, message);

        return Response
                .status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON)
                .entity(message)
                .build();
    }

}
