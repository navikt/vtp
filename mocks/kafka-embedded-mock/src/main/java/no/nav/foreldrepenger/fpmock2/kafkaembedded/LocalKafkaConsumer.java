package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.util.Collection;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalKafkaConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(LocalKafkaConsumer.class);
    private final KafkaConsumer consumer;

    public LocalKafkaConsumer(Collection<String> topics){
        LOG.info("Starter consumer for topics: "+ topics.stream().collect(Collectors.joining(",")));
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        //props["acks"] = "all"
        props.put("retries", 15);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer(props);
        consumer.subscribe(topics);
    }

    public KafkaConsumer getConsumer(){
        return consumer;
    }

}
