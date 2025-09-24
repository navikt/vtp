package no.nav.foreldrepenger.vtp.kafkaembedded;

import java.util.Properties;
import java.util.UUID;

import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import no.nav.foreldrepenger.util.KeystoreUtils;

import static no.nav.foreldrepenger.vtp.kafkaembedded.KafkaToggle.skalBrukeNyKafka;

public class LocalKafkaProducer {
    private static final Logger LOG = LoggerFactory.getLogger(LocalKafkaProducer.class);

    private final KafkaProducer stringProducer;
    private final KafkaProducer avroProducer;

    public LocalKafkaProducer() {
        stringProducer = createStringProducer();
        avroProducer = createAvroProducer();
    }

    private KafkaProducer createStringProducer() {
        Properties props = createCommonProperties();
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer(props);
    }

    private KafkaProducer createAvroProducer() {
        Properties props = createCommonProperties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "mock://dummy");
        return new KafkaProducer(props);
    }

    private Properties createCommonProperties() {
        Properties props = new Properties();
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, KeystoreUtils.getTruststoreFilePath());
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, KeystoreUtils.getTruststorePassword());
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, KeystoreUtils.getKeystoreFilePath());
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, KeystoreUtils.getKeyStorePassword());

        if (!skalBrukeNyKafka()) {
            LOG.info("Setter opp producer for gammel Kafka");
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
            String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
            props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(jaasTemplate, "vtp", "vtp"));
            props.put(ProducerConfig.RETRIES_CONFIG, 15);
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_SSL.name);
            props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        } else {
            LOG.info("Setter opp producer for ny Kafka");
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BROKERS"));
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SSL.name);
            props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
            props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "jks");
            props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "PKCS12");
            props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, KeystoreUtils.getKeyStorePassword());
        }
        return props;
    }

    public void sendMelding(String topic, String key, String value) {
        stringProducer.send(new ProducerRecord<>(topic, key, value), (recordMetadata, e) -> {
            LOG.info("Received new metadata: [topic: {} partition: {} offset: {}]", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
        });
        stringProducer.flush();
    }

    public void sendMelding(String topic, GenericData.Record value) {
        avroProducer.send(new ProducerRecord<>(topic, UUID.randomUUID().toString(), value), (recordMetadata, e) -> {
            LOG.info("Received new metadata: [topic: {} partition: {} offset: {}]", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
        });
        avroProducer.flush();
    }
}
