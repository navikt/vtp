package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.util.Collection;
import java.util.Properties;

import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.felles.KeystoreUtils;

public class LocalKafkaConsumerStream {
    private static final Logger LOG = LoggerFactory.getLogger(LocalKafkaConsumerStream.class);
    private KafkaStreams stream;


    public LocalKafkaConsumerStream(String bootstrapServers, Collection<String> topics) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "vtp");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        props.put("ssl.truststore.location", KeystoreUtils.getTruststoreFilePath());
        props.put("ssl.truststore.password", KeystoreUtils.getTruststorePassword());
        props.put("ssl.keystore.location", KeystoreUtils.getKeystoreFilePath());
        props.put("ssl.keystore.password", KeystoreUtils.getKeyStorePassword());
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(jaasTemplate, "vtp", "vtp"));

        final StreamsBuilder builder = new StreamsBuilder();
        Consumed<String, String> stringStringConsumed = Consumed.with(Topology.AutoOffsetReset.EARLIEST);
        topics.forEach(topic -> builder
                .stream(topic, stringStringConsumed)
                .foreach((key, message) -> handleMessage(topic, key, message)));

        final Topology topology = builder.build();
        this.stream = new KafkaStreams(topology, props);
    }

    public void start() {
        stream.start();
        LOG.info("Starter konsumering av topics");
    }

    private void handleMessage(String topic, String key, String message) {
        LOG.info("Receiced message on topic='{}' :: key='{}',value='{}'", topic, key, message);
    }
}
