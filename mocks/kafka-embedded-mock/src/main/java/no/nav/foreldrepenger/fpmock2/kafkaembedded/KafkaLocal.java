package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;


class KafkaLocal {
    Logger LOG = LoggerFactory.getLogger(KafkaLocal.class);

    KafkaServerStartable kafka;
    ZooKeeperLocal zookeeper;

    KafkaLocal(Properties kafkaProperties, Properties zkProperties) {
        KafkaConfig kafkaConfig = new KafkaConfig(kafkaProperties);

        LOG.info("starting local zookeeper...");
        zookeeper = new ZooKeeperLocal(zkProperties);
        startKafka(kafkaConfig);

    }

    private void startKafka(KafkaConfig kafkaConfig) {
        kafka = new KafkaServerStartable(kafkaConfig);
        LOG.info("starting local kafka broker...");
        kafka.startup();

    }


    void stop() {
        LOG.info("stopping kafka...");
        kafka.shutdown();
    }

}
