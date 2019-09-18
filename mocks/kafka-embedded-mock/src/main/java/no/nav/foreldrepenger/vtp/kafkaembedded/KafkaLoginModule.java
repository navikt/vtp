package no.nav.foreldrepenger.vtp.kafkaembedded;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaLoginModule implements LoginModule {
    private final static Logger LOG = LoggerFactory.getLogger(KafkaLoginModule.class);
    private Subject subject;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        String username = options.get("username").toString();
        String password = options.get("password").toString();
        this.subject.getPublicCredentials().add(username);
        this.subject.getPrivateCredentials().add(password);

        LOG.info("called: initialize subject: {}, callbackHandler: {}", subject,callbackHandler);
    }

    @Override
    public boolean login() throws LoginException {
        LOG.info("called: login");
        return true;
    }

    @Override
    public boolean commit() throws LoginException {
        LOG.info("called: commit");
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        LOG.info("called: abort");
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        LOG.info("called: logout");
        return true;
    }

}
