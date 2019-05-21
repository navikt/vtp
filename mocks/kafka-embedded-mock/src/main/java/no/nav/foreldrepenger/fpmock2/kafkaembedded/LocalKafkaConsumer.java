package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalKafkaConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(LocalKafkaConsumer.class);
    private final KafkaConsumer consumer;

    public LocalKafkaConsumer(Collection<String> topics){
        LOG.info("Starter consumer for topics: "+ topics.stream().collect(Collectors.joining(",")));
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id","vtp-group");
        consumer = new KafkaConsumer(props);
        consumer.subscribe(topics);
    }

    public KafkaConsumer getConsumer(){
        return consumer;
    }


    public void startConsumerPoller(Collection<String> topics){
        getConsumer().subscribe(topics);

        Runnable r = () -> {
            try {
                Thread.sleep(4000);
                ConsumerRecords<String, String> records = getConsumer().poll(Duration.ofSeconds(3));
                for (ConsumerRecord<String, String> record : records){
                    Map<String, Object> data = new HashMap<>();
                    System.out.println("Mottatt melding: "+record);
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
                e.printStackTrace();
            } finally {
                getConsumer().close();
            }
        };

        new Thread(r).start();

    }

}
