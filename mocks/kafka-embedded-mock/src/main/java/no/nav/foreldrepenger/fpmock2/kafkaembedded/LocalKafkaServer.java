package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.util.Collection;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalKafkaServer {

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

    public static void startKafka(final int zookeeperPort, final int kafkaBrokerPort, Collection<String> bootstrapTopics) {
        Logger LOG = LoggerFactory.getLogger(LocalKafkaServer.class);

        final String KAFKA_URL = String.format("localhost:%s",kafkaBrokerPort);

        LocalKafkaServer.kafkaBrokerPort = kafkaBrokerPort;
        LocalKafkaServer.zookeeperPort = zookeeperPort;
        Properties kafkaProperties = new Properties();
        Properties zkProperties = new Properties();

        kafkaProperties.put("zookeeper.connect", "localhost:" + zookeeperPort);
        kafkaProperties.put("offsets.topic.replication.factor", "1");
        kafkaProperties.put("log.dirs", "target/kafka-logs");
        kafkaProperties.put("auto.create.topics.enable", "true");
        kafkaProperties.put("listeners", "PLAINTEXT://localhost:" + kafkaBrokerPort);
        kafkaProperties.put("advertised.listeners", "PLAINTEXT://localhost:" + kafkaBrokerPort);

        //kafkaProperties.put("listeners", "SASL_SSL://localhost:" + kafkaBrokerPort);
        //kafkaProperties.put("advertised.listeners","SASL_SSL://localhost:" +kafkaBrokerPort);
        //kafkaProperties.put("security.inter.broker.protocol","SASL_SSL");


        final String zookeeperTempInstanceDataDir = "" + System.currentTimeMillis(); // For å hindre NodeExists-feil på restart p.g.a. at data allerede finnes i katalogen.
        zkProperties.put("dataDir", "target/zookeeper/" + zookeeperTempInstanceDataDir);
        zkProperties.put("clientPort", "" + zookeeperPort);
        zkProperties.put("maxClientCnxns", "0");
        try {
            kafka = new KafkaLocal(kafkaProperties, zkProperties);

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Kunne ikke starte Kafka producer og/eller consumer");
        }


        localProducer = new LocalKafkaProducer(KAFKA_URL);
        kafkaAdminClient = localProducer.getKafkaAdminClient();

        if (bootstrapTopics != null) {
            kafkaAdminClient.createTopics(
                    bootstrapTopics.stream().map(
                            name -> new NewTopic(name, 1, (short) 1)).collect(Collectors.toList()));
        }

        localConsumer = new LocalKafkaConsumerStream(KAFKA_URL, bootstrapTopics);
        localConsumer.start();
    }

}
