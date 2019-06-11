package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.felles.KeystoreUtils;

public class LocalKafkaProducer {
    Logger LOG = LoggerFactory.getLogger(LocalKafkaProducer.class);
    private final KafkaProducer producer;
    private final AdminClient kafkaAdminClient;

    public LocalKafkaProducer(String bootstrapServer) {
        // Create Producer properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.RETRIES_CONFIG, 15);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "PLAIN");
        props.put("plain.sasl.jaas.config","no.nav.foreldrepenger.fpmock2.kafkaembedded.KafkaLoginModule required;");
        //props.put("sasl.jaas.config","org.apache.kafka.common.security.scram.ScramLoginModule required username=\"vtp_user\" password=\"vtp_password\";");
        props.put("ssl.truststore.location", KeystoreUtils.getTruststoreFilePath());
        props.put("ssl.truststore.password",KeystoreUtils.getTruststorePassword());
        props.put("ssl.keystore.location",KeystoreUtils.getKeystoreFilePath());
        props.put("ssl.keystore.password",KeystoreUtils.getKeyStorePassword());

        // Create the producer
        producer = new KafkaProducer<String, String>(props);
        kafkaAdminClient = AdminClient.create(props);
    }

    public AdminClient getKafkaAdminClient() {
        return kafkaAdminClient;
    }

    KafkaProducer getKafkaProducer() {
        return producer;
    }

    public void sendMelding(String topic, String key, String value) {
        producer.send(new ProducerRecord(topic, key, value), (recordMetadata, e) -> {
            LOG.info("Received new metadata: [topic: {} partition: {} offset: {}]", recordMetadata.topic(), recordMetadata.partition(),recordMetadata.offset());
        });
        producer.flush();
    }
}
