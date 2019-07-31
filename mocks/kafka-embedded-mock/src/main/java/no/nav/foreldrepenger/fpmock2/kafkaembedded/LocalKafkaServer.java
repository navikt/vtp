package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.util.Collection;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.felles.KeystoreUtils;

public class LocalKafkaServer {

    final private static Logger LOG = LoggerFactory.getLogger(LocalKafkaServer.class);
    final static public String VTP_KAFKA_HOST = null != System.getenv("VTP_KAFKA_HOST") ? System.getenv("VTP_KAFKA_HOST") : "localhost";

    private static KafkaLocal kafka;
    private static LocalKafkaProducer localProducer;
    private static LocalKafkaConsumerStream localConsumer;
    private static AdminClient kafkaAdminClient;

    private static int zookeeperPort;
    private static int kafkaBrokerPort;

    public static int getZookeperPort() {
        return zookeeperPort;
    }

    public static int getKafkaBrokerPort() {
        return kafkaBrokerPort;
    }

    public static AdminClient getKafkaAdminClient() {
        return kafkaAdminClient;
    }

    public static void startKafka(final int zookeeperPort, final int kafkaBrokerPort, Collection<String> bootstrapTopics) {
        Logger LOG = LoggerFactory.getLogger(LocalKafkaServer.class);

        final String bootstrapServers = String.format("%s:%s", "localhost", kafkaBrokerPort);

        LocalKafkaServer.kafkaBrokerPort = kafkaBrokerPort;
        LocalKafkaServer.zookeeperPort = zookeeperPort;

        Properties kafkaProperties = setupKafkaProperties(zookeeperPort, kafkaBrokerPort);
        Properties zkProperties = setupZookeperProperties(zookeeperPort);
        System.setProperty(JaasUtils.JAVA_LOGIN_CONFIG_PARAM, "kafkasecurity.conf");
        LOG.info("Kafka startes med Jaas login config param: " + System.getProperty(JaasUtils.JAVA_LOGIN_CONFIG_PARAM));


        try {
            kafka = new KafkaLocal(kafkaProperties, zkProperties);

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Kunne ikke starte Kafka producer og/eller consumer");
        }


        kafkaAdminClient = AdminClient.create(createAdminClientProps(bootstrapServers));
        kafkaAdminClient.createTopics(
                bootstrapTopics.stream().map(
                        name -> new NewTopic(name, 1, (short) 1)).collect(Collectors.toList()));

        localConsumer = new LocalKafkaConsumerStream(bootstrapServers, bootstrapTopics);
        localConsumer.start();

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
        props.put("ssl.truststore.location", KeystoreUtils.getTruststoreFilePath());
        props.put("ssl.truststore.password", KeystoreUtils.getTruststorePassword());
        props.put("ssl.keystore.location", KeystoreUtils.getKeystoreFilePath());
        props.put("ssl.keystore.password", KeystoreUtils.getKeyStorePassword());
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(jaasTemplate, "vtp", "vtp"));

        return props;
    }

    private static Properties setupZookeperProperties(int zookeeperPort) {
        Properties zkProperties = new Properties();
        final String zookeeperTempInstanceDataDir = "" + System.currentTimeMillis(); // For å hindre NodeExists-feil på restart p.g.a. at data allerede finnes i katalogen.
        zkProperties.put("dataDir", "target/zookeeper/" + zookeeperTempInstanceDataDir);
        zkProperties.put("clientPort", "" + zookeeperPort);
        zkProperties.put("maxClientCnxns", "0");
        zkProperties.put("admin.enableServer", "false");
        zkProperties.put("jaasLoginRenew", "3600000");

        zkProperties.put("authorizer.class.name", "kafka.security.auth.SimpleAclAuthorizer");
        zkProperties.put("allow.everyone.if.no.acl.found", "true");
        zkProperties.put("ssl.client.auth", "required");
        zkProperties.put("ssl.keystore.location", KeystoreUtils.getKeystoreFilePath());
        zkProperties.put("ssl.keystore.password", KeystoreUtils.getKeyStorePassword());
        zkProperties.put("ssl.truststore.location", KeystoreUtils.getTruststoreFilePath());
        zkProperties.put("ssl.truststore.password", KeystoreUtils.getTruststorePassword());

        return zkProperties;
    }

    private static Properties setupKafkaProperties(int zookeeperPort, int kafkaBrokerPort) {
        Properties kafkaProperties = new Properties();
/*
        //TODO: Gjør dette om til kode når POC fungerer i VTP
        String listeners = "INTERNAL://localhost:"+kafkaBrokerPort;
        if(null != System.getenv("VTP_KAFKA_HOST")){
            listeners = listeners + String.format(",EXTERNAL://%s",VTP_KAFKA_HOST);
            kafkaProperties.put("listener.security.protocol.map","INTERNAL:SASL_SSL,EXTERNAL:SASL_SSL");
            LOG.info("VTP_KAFKA_HOST satt for miljø. Starter med følgende listeners: {}", listeners);
        } else {
            LOG.info("VTP_KAFKA_HOST ikke satt for miljø. Starter med følgende listeners: {}", listeners);
            kafkaProperties.put("listener.security.protocol.map","INTERNAL:SASL_SSL");
        }
*/
        kafkaProperties.put("listener.security.protocol.map", "INTERNAL:SASL_SSL,EXTERNAL:SASL_SSL"); //TODO: Fjern når POC fungerer
        kafkaProperties.put("zookeeper.connect", "localhost:" + zookeeperPort);
        kafkaProperties.put("offsets.topic.replication.factor", "1");
        kafkaProperties.put("log.dirs", "target/kafka-logs");
        kafkaProperties.put("auto.create.topics.enable", "true");
        kafkaProperties.put("listeners", "INTERNAL://:9092,EXTERNAL://:9093");
        kafkaProperties.put("advertised.listeners", "INTERNAL://localhost:9092,EXTERNAL://fpmock2:9093");
        kafkaProperties.put("socket.request.max.bytes", "369296130");
        kafkaProperties.put("sasl.enabled.mechanisms", "DIGEST-MD5,PLAIN");
        kafkaProperties.put("sasl.mechanism.inter.broker.protocol", "PLAIN");
        kafkaProperties.put("inter.broker.listener.name", "INTERNAL");

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


}
