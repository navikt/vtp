package no.nav.foreldrepenger.vtp.kafkaembedded;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.felles.KeystoreUtils;

public class LocalKafkaProducer {
    private static final Logger LOG = LoggerFactory.getLogger(LocalKafkaProducer.class);

    private final KafkaProducer<String, String> producer;

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
        props.put(StreamsConfig.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, KeystoreUtils.getTruststoreFilePath());
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, KeystoreUtils.getTruststorePassword());
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, KeystoreUtils.getKeystoreFilePath());
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, KeystoreUtils.getKeyStorePassword());
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(jaasTemplate, "vtp", "vtp"));

        // Create the producer
        producer = new KafkaProducer<String, String>(props);

    }

    public void sendMelding(String topic, String key, String value) {
        producer.send(new ProducerRecord<>(topic, key, value), (recordMetadata, e) -> {
            LOG.info("Received new metadata: [topic: {} partition: {} offset: {}]", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
        });
        producer.flush();
    }

    public void sendMelding(String topic, String value) {
        producer.send(new ProducerRecord<>(topic, value), (recordMetadata, e) -> {
            LOG.info("Received new metadata: [topic: {} partition: {} offset: {}]", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
        });
        producer.flush();
    }
}
