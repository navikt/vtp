package no.nav.foreldrepenger.vtp.kafkaembedded;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;


class KafkaLocal {
    private Logger LOG = LoggerFactory.getLogger(KafkaLocal.class);

    private KafkaServer kafka;
    private ZooKeeperLocal zookeeper;

    KafkaLocal(Properties kafkaProperties, Properties zkProperties) {
        KafkaConfig kafkaConfig = new KafkaConfig(kafkaProperties);

        startZookeeper(zkProperties);
        startKafka(kafkaConfig);

    }

    private void startZookeeper(Properties zkProperties) {
        LOG.info("starting local zookeeper...");
        zookeeper = new ZooKeeperLocal(zkProperties);
    }

    private void startKafka(KafkaConfig kafkaConfig) {
        kafka = new KafkaServer(kafkaConfig,
                KafkaServer.$lessinit$greater$default$2(),
                KafkaServer.$lessinit$greater$default$3(),
                true);
        LOG.info("starting local kafka broker...");
        kafka.startup();

    }

    void stop() {
        LOG.info("stopping kafka...");
        kafka.shutdown();
        zookeeper.stop();
    }

}
