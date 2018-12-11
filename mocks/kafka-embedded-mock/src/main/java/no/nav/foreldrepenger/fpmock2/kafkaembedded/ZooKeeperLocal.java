package no.nav.foreldrepenger.fpmock2.kafkaembedded;

import java.io.IOException;
import java.util.Properties;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;

class ZooKeeperLocal {

    ZooKeeperServerMain zooKeeperServer;

    ZooKeeperLocal(Properties zkProperties) {
        QuorumPeerConfig quorumConfiguration = new QuorumPeerConfig();
        try {
            quorumConfiguration.parseProperties(zkProperties);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        zooKeeperServer = new ZooKeeperServerMain();
        final ServerConfig configuration = new ServerConfig();
        configuration.readFrom(quorumConfiguration);


        new Thread(() -> {
                try {
                    zooKeeperServer.runFromConfig(configuration);
                } catch (IOException e) {
                    System.out.println("ZooKeeper Failed");
                    e.printStackTrace(System.err);
                }
            }
        ).start();
    }
}