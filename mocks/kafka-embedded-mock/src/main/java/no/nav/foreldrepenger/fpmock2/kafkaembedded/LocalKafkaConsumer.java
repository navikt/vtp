package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class LocalKafkaConsumer {
    private final KafkaConsumer consumer;

    public LocalKafkaConsumer(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        //props["acks"] = "all"
        props.put("retries", 15);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        consumer = new KafkaConsumer(props);
    }

}
