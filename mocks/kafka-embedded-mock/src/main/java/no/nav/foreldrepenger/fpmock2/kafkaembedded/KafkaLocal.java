package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.util.Properties;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;


class KafkaLocal {

    KafkaServerStartable kafka;
    ZooKeeperLocal zookeeper;

    KafkaLocal(Properties kafkaProperties, Properties zkProperties) {
        KafkaConfig kafkaConfig = new KafkaConfig(kafkaProperties);

        //start local zookeeper
        System.out.println("starting local zookeeper...");
        zookeeper = new ZooKeeperLocal(zkProperties);
        System.out.println("done");

        //start local kafka broker
        kafka = new KafkaServerStartable(kafkaConfig);
        System.out.println("starting local kafka broker...");
        kafka.startup();
        System.out.println("done");
    }

    void stop(){
        //stop kafka broker
        System.out.println("stopping kafka...");
        kafka.shutdown();
        System.out.println("done");
    }

}
