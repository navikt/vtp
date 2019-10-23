package no.nav.foreldrepenger.vtp.kafkaembedded;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

        var started = new CountDownLatch(1);
        t = new Thread(() -> {
            try {
                started.countDown(); // here we go
                zooKeeperServer.runFromConfig(configuration);
            } catch (IOException e) {
                LOG.error("Zookeeper failed: {}", e.getMessage());
            } catch (AdminServer.AdminServerException e) {
                LOG.error("Zookeeper failed: {}", e.getMessage());
                e.printStackTrace();
            }
        });
        t.start();
        // Vent på zookeeper start
        try {
            if (!started.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Could not start Zookeeper in time (5 secs)");
            }
            Thread.sleep(1 * 1000L); // vent littegrann til på zookeeper
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    void stop() {
        t.interrupt();
    }
}
