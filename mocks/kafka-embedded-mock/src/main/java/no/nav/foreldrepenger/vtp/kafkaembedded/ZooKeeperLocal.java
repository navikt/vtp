package no.nav.foreldrepenger.vtp.kafkaembedded;

import java.io.IOException;
import java.util.Properties;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.admin.AdminServer;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ZooKeeperLocal {
    private Thread t;
    private Logger LOG = LoggerFactory.getLogger(ZooKeeperLocal.class);
    private ZooKeeperServerMain zooKeeperServer;

    ZooKeeperLocal(Properties zkProperties) {
        QuorumPeerConfig quorumConfiguration = new QuorumPeerConfig();
        try {
            quorumConfiguration.parseProperties(zkProperties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        zooKeeperServer = new ZooKeeperServerMain();
        final ServerConfig configuration = new ServerConfig();
        configuration.readFrom(quorumConfiguration);


        t = new Thread(() -> {
            try {
                zooKeeperServer.runFromConfig(configuration);
            } catch (IOException e) {
                LOG.error("Zookeeper failed: {}", e.getMessage());
            } catch (AdminServer.AdminServerException e) {
                LOG.error("Zookeeper failed: {}", e.getMessage());
                e.printStackTrace();
            }
        }
        );
        t.start();
    }

    void stop() {
        t.interrupt();
    }
}
