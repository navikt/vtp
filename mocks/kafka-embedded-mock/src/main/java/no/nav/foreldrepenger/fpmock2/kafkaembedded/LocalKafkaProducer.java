package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class LocalKafkaProducer {
    private final KafkaProducer producer;
    private final AdminClient kafkaAdminClient;

    public LocalKafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        //props["acks"] = "all"
        props.put("retries", 15);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer(props);

        kafkaAdminClient = AdminClient.create(props);
    }

    public AdminClient getKafkaAdminClient() {
        return kafkaAdminClient;
    }

    public void sendSynkront(String topic, String key, String value) {
        try {
            producer.send(new ProducerRecord(topic, key, value))
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
