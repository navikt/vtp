package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.util.Collection;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalKafkaConsumerStream {
    private static final Logger LOG = LoggerFactory.getLogger(LocalKafkaConsumerStream.class);
    private KafkaStreams stream;


    public LocalKafkaConsumerStream(String bootstrapServers, Collection<String> topics){
        Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG,"vtp");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        /*
        streamsConfiguration.put(StreamsConfig.SECURITY_PROTOCOL_CONFIG,"SASL_SSL");
        streamsConfiguration.put("sasl.mechanism","DIGEST-MD5");
        streamsConfiguration.put("sasl.jaas.config","org.apache.kafka.common.security.scram.ScramLoginModule required username=\"vtp_user\" password=\"vtp_password\";");
        streamsConfiguration.put("ssl.truststore.location", KeystoreUtils.getTruststoreFilePath());
        streamsConfiguration.put("ssl.truststore.password",KeystoreUtils.getTruststorePassword());
        streamsConfiguration.put("ssl.keystore.location",KeystoreUtils.getKeystoreFilePath());
        streamsConfiguration.put("ssl.keystore.password",KeystoreUtils.getKeyStorePassword());
        */

        //TODO (OL): Skrive om slik at vi logger hvilken kø meldingen kommer inn på.
        final StreamsBuilder builder = new StreamsBuilder();
        Consumed<String, String> stringStringConsumed = Consumed.with(Topology.AutoOffsetReset.EARLIEST);
        builder.stream(topics, stringStringConsumed)
                .foreach(this::handleMessage);

        final Topology topology = builder.build();
        this.stream = new KafkaStreams(topology,streamsConfiguration);


    }

    public void start(){
        stream.start();
        LOG.info("Starter konsumering av topics");
    }

    private void handleMessage(String key, String message){
        LOG.info("{} : {}", key, message);
    }



}
