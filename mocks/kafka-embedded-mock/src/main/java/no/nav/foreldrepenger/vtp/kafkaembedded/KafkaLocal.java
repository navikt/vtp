package no.nav.foreldrepenger.vtp.kafkaembedded;

import java.util.Properties;

import org.apache.kafka.common.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.server.KafkaConfig;
import kafka.server.KafkaRaftServer;


class KafkaLocal {
    private Logger LOG = LoggerFactory.getLogger(KafkaLocal.class);

    private KafkaRaftServer kafka;

    KafkaLocal(Properties kafkaProperties) {
        var kafkaConfig = new KafkaConfig(kafkaProperties);
        startKafka(kafkaConfig);
    }

    private void startKafka(KafkaConfig kafkaConfig) {
        LOG.info("Starting Kafka in KRaft mode...");
        kafka = new KafkaRaftServer(kafkaConfig, Time.SYSTEM);
        kafka.startup();
        LOG.info("Kafka started successfully in KRaft mode");
    }

    void stop() {
        LOG.info("Stopping Kafka...");
        if (kafka != null) {
            kafka.shutdown();
            kafka.awaitShutdown();
        }
        // Delete temp directories
        LOG.info("Kafka stopped");
    }


    public KafkaRaftServer getKafka() {
        return kafka;
    }
}
