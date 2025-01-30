package no.nav.foreldrepenger.vtp.server.api.kafka;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "Kafka services")
@Path("/api/kafka")
public class KafkaRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaRestTjeneste.class);

    @Context
    private LocalKafkaProducer localKafkaProducer;


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
