package no.nav.foreldrepenger.fpmock2.server;

import org.jolokia.util.LogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JolokiaLogHandler implements LogHandler {

    private static final Logger log = LoggerFactory.getLogger(JolokiaLogHandler.class);
    
    @Override
    public void debug(String message) {
        log.debug(message);
    }

    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void error(String message, Throwable t) {
        log.error(message, t);
    }

}
