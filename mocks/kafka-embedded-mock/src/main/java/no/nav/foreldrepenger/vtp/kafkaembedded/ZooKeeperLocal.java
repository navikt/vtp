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
    private static final Logger LOG = LoggerFactory.getLogger(ZooKeeperLocal.class);
    private final Thread t;
    private final ZooKeeperServerMain zooKeeperServer;

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
            } catch (IOException | AdminServer.AdminServerException e) {
                LOG.error("Zookeeper failed: ", e);
            }
        });
        t.start();
        // Vent på zookeeper start
        try {
            if (!started.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Could not start Zookeeper in time (5 secs)");
            }
            int sekunder = 1;
            Thread.sleep(sekunder * 1000L); // vent littegrann til på zookeeper
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    void stop() {
        t.interrupt();
    }
}
