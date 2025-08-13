package no.nav.foreldrepenger.vtp.kafkaembedded;

import java.util.Collection;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.util.KeystoreUtils;


public class LocalKafkaServer {

    final private static Logger log = LoggerFactory.getLogger(LocalKafkaServer.class);
    private final Collection<String> bootstrapTopics;
    private KafkaLocal kafka;
    private LocalKafkaProducer localProducer;
    private AdminClient kafkaAdminClient;
    private int kafkaBrokerPort;

    public LocalKafkaServer(final int kafkaBrokerPort, Collection<String> bootstrapTopics) {
        this.kafkaBrokerPort = kafkaBrokerPort;
        this.bootstrapTopics = bootstrapTopics;
    }

    private static Properties createAdminClientProps(String boostrapServer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServer);
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
        props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(jaasTemplate, "vtp", "vtp"));

        return props;
    }




    private static Properties setupKafkaProperties() {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put("process.roles", "broker,controller");
        kafkaProperties.put("node.id", "1001");
        kafkaProperties.put("controller.quorum.voters", "1001@localhost:9095");
        kafkaProperties.put("controller.listener.names", "CONTROLLER");
        kafkaProperties.put("offsets.topic.replication.factor", "1");
        kafkaProperties.put("log.dirs", "target/kafka-logs");
        kafkaProperties.put("auto.create.topics.enable", "true");
        kafkaProperties.put("listeners", "INTERNAL://:9092,EXTERNAL://:9093,PLAINTEXT://:9094,CONTROLLER://:9095");
        kafkaProperties.put("advertised.listeners", "INTERNAL://localhost:9092,EXTERNAL://vtp:9093,PLAINTEXT://localhost:9094,CONTROLLER://localhost:9095");
        kafkaProperties.put("socket.request.max.bytes", "369296130");
        kafkaProperties.put("sasl.enabled.mechanisms", "DIGEST-MD5,PLAIN");
        kafkaProperties.put("sasl.mechanism.inter.broker.protocol", "PLAIN");
        kafkaProperties.put("inter.broker.listener.name", "INTERNAL");
        kafkaProperties.put("listener.security.protocol.map", "INTERNAL:SASL_SSL,EXTERNAL:SASL_SSL,PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT");

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        kafkaProperties.put("SASL_SSL.".toLowerCase() + SaslConfigs.SASL_JAAS_CONFIG, String.format(jaasTemplate, "vtp", "vtp"));
        kafkaProperties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");

        //SSL
        kafkaProperties.put("ssl.keystore.location", KeystoreUtils.getKeystoreFilePath());
        kafkaProperties.put("ssl.keystore.password", KeystoreUtils.getKeyStorePassword());
        kafkaProperties.put("ssl.truststore.location", KeystoreUtils.getTruststoreFilePath());
        kafkaProperties.put("ssl.truststore.password", KeystoreUtils.getTruststorePassword());
        return kafkaProperties;
    }

    public int getKafkaBrokerPort() {
        return kafkaBrokerPort;
    }

    public AdminClient getKafkaAdminClient() {
        return kafkaAdminClient;
    }

    public void start() {
        final var bootstrapServers = String.format("%s:%s", "localhost", kafkaBrokerPort);

        var kafkaProperties = setupKafkaProperties();
        System.setProperty(JaasUtils.JAVA_LOGIN_CONFIG_PARAM, "kafkasecurity.conf");
        kafka = new KafkaLocal(kafkaProperties);
        kafkaAdminClient = AdminClient.create(createAdminClientProps(bootstrapServers));
        kafkaAdminClient.createTopics(
                bootstrapTopics.stream().map(
                        name -> new NewTopic(name, 1, (short) 1)).collect(Collectors.toList()));

        localProducer = new LocalKafkaProducer(bootstrapServers);
    }

    public void stop() {
        log.info("Stopper kafka server");
        kafkaAdminClient.close();
        kafka.stop();
    }

    public LocalKafkaProducer getLocalProducer() {
        return localProducer;
    }
}
