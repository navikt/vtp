package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalKafkaServer {

    private static KafkaLocal kafka;
    private static LocalKafkaProducer localProducer;
    private static LocalKafkaConsumer localConsumer;

    private static int zookeeperPort;
    private static int kafkaBrokerPort;

    public static int getZookeperPort() {
        return zookeeperPort;
    }

    public static int getKafkaBrokerPort() {
        return kafkaBrokerPort;
    }

    public static void startKafka(final int zookeeperPort, final int kafkaBrokerPort, Collection<String> bootstrapTopics){
        Logger LOG = LoggerFactory.getLogger(LocalKafkaServer.class);

        LocalKafkaServer.kafkaBrokerPort = kafkaBrokerPort;
        LocalKafkaServer.zookeeperPort = zookeeperPort;
        Properties kafkaProperties = new Properties();
        Properties zkProperties = new Properties();

        kafkaProperties.put("zookeeper.connect", "localhost:" + zookeeperPort);
        kafkaProperties.put("offsets.topic.replication.factor", "1");
        kafkaProperties.put("logs.dirs", "target/kafka-logs");
        //     kafkaProperties.put("listeners", "PLAINTEXT://localhost:" + kafkaBrokerPort);
        kafkaProperties.put("advertised.host.name", "localhost");
        kafkaProperties.put("advertised.port",kafkaBrokerPort);
        kafkaProperties.put("socket.request.max.bytes","480000000");
        kafkaProperties.put("security.protocol","SASL_SSL");
        kafkaProperties.put("sasl.mechanism","PLAIN");
        kafkaProperties.put("port", kafkaBrokerPort);


        final String zookeeperTempInstanceDataDir = ""+ System.currentTimeMillis(); // For å hindre NodeExists-feil på restart p.g.a. at data allerede finnes i katalogen.
        zkProperties.put("dataDir", "target/zookeeper/" + zookeeperTempInstanceDataDir);
        zkProperties.put("clientPort", "" + zookeeperPort);
        zkProperties.put("maxClientCnxns", "0");

        try {
            kafka = new KafkaLocal(kafkaProperties, zkProperties);

            if (bootstrapTopics != null) {
                localProducer = new LocalKafkaProducer();
                localProducer.getKafkaAdminClient().createTopics(
                        bootstrapTopics.stream().map(
                                name -> new NewTopic(name, 1, (short)1)).collect(Collectors.toList()));
                localConsumer = new LocalKafkaConsumer(bootstrapTopics);

                bootstrapTopics.forEach(t->
                        localProducer.sendSynkront(t,"testkey","testvalue"));

                startConsumerPoller(bootstrapTopics);
            }


        } catch (Exception e){
            e.printStackTrace();
            LOG.error("Kunne ikke starte Kafka producer og/eller consumer");
        }
    }


    private static void startConsumerPoller(Collection<String> topics){
            localConsumer.getConsumer().subscribe(topics);

                Runnable r = () -> {
                    try {
                    ConsumerRecords<String, String> records = localConsumer.getConsumer().poll(6000);
                    for (ConsumerRecord<String, String> record : records){
                        Map<String, Object> data = new HashMap<>();
                        System.out.println(record);
                    }
                    } finally {
                        localConsumer.getConsumer().close();
                    }
                };

            new Thread(r).start();




    }
}
