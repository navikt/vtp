package no.nav.foreldrepenger.fpmock2.testutil;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.melding.virksomhet.behandlingsstatus.hendelsehandterer.v1.hendelseshandtererbehandlingsstatus.BehandlingAvsluttet;
import no.nav.melding.virksomhet.behandlingsstatus.hendelsehandterer.v1.hendelseshandtererbehandlingsstatus.BehandlingOpprettet;
import no.nav.vedtak.felles.integrasjon.sakogbehandling.SakOgBehandlingClient;

@ApplicationScoped
@Alternative
@Priority(1)
public class NoopSakOgBehandlingClient implements SakOgBehandlingClient {
    protected final Logger log = LoggerFactory.getLogger(NoopSakOgBehandlingClient.class);

    @Override
    public void sendBehandlingOpprettet(BehandlingOpprettet behandlingOpprettet) {
        log.info("Invoked: sendBehandlingOpprettet");
    }

    @Override
    public void sendBehandlingAvsluttet(BehandlingAvsluttet behandlingAvsluttet) {
        log.info("Invoked: sendBehandlingAvsluttet");
    }

    @Override
    public void testConnection() throws JMSException {
        log.info("Invoked: testConnection");
    }

    @Override
    public String getConnectionEndpoint() {
        log.info("Invoked: getConnectionEndpoint");
        return null;
    }
}
