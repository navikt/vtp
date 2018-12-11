package no.nav.foreldrepenger.fpmock2.kafkaembedded;
import java.util.Collection;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.NewTopic;

public class LocalKafkaServer {

    private static KafkaLocal kafka;


    private static int zookeeperPort;
    private static int kafkaBrokerPort;

    public static int getZookeperPort() {
        return zookeeperPort;
    }

    public static int getKafkaBrokerPort() {
        return kafkaBrokerPort;
    }

    public static void startKafka(final int zookeeperPort, final int kafkaBrokerPort, Collection<String> bootstrapTopics){
        LocalKafkaServer.kafkaBrokerPort = kafkaBrokerPort;
        LocalKafkaServer.zookeeperPort = zookeeperPort;
        Properties kafkaProperties = new Properties();
        Properties zkProperties = new Properties();

        kafkaProperties.put("zookeeper.connect", "localhost:" + zookeeperPort);
        kafkaProperties.put("offsets.topic.replication.factor", "1");
        kafkaProperties.put("logs.dirs", "target/kafka-logs");
        kafkaProperties.put("listeners", "PLAINTEXT://localhost:" + kafkaBrokerPort);
        kafkaProperties.put("advertised.host.name", "localhost");
        kafkaProperties.put("port", "9092");


        final String zookeeperTempInstanceDataDir = ""+ System.currentTimeMillis(); // For å hindre NodeExists-feil på restart p.g.a. at data allerede finnes i katalogen.
        zkProperties.put("dataDir", "target/zookeeper/" + zookeeperTempInstanceDataDir);
        zkProperties.put("clientPort", "" + zookeeperPort);
        zkProperties.put("maxClientCnxns", "0");

        try {
            kafka = new KafkaLocal(kafkaProperties, zkProperties);

            if (bootstrapTopics != null) {
                new LocalKafkaProducer().getKafkaAdminClient().createTopics(
                        bootstrapTopics.stream().map(
                                name -> new NewTopic(name, 1, (short)1)).collect(Collectors.toList()));
            }

        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
}
