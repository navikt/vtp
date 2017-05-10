package no.nav.tjeneste.virksomhet.oppgavebehandling.v3;

import no.nav.tjeneste.virksomhet.oppgavebehandling.v3.meldinger.*;
import no.nav.tjeneste.virksomhet.oppgavebehandling.v3.meldinger.OpprettOppgave;
import no.nav.tjeneste.virksomhet.oppgavebehandling.v3.meldinger.OpprettOppgaveResponse;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OppgavebehandlingServiceMockImplTest {

    @Test
    public void skal_returnere_unik_oppgaveId() throws Exception {
        // Arrage
        OppgavebehandlingServiceMockImpl mock = new OppgavebehandlingServiceMockImpl();
        OpprettOppgaveRequest request = new OpprettOppgaveRequest();
        no.nav.tjeneste.virksomhet.oppgavebehandling.v3.meldinger.OpprettOppgave opprettOppgave = new OpprettOppgave();
        opprettOppgave.setSaksnummer("123");
        request.setOpprettOppgave(opprettOppgave);

        // Act
        OpprettOppgaveResponse opprettOppgaveResponse = mock.opprettOppgave(request);

        // Assert
        String oppgaveId = opprettOppgaveResponse.getOppgaveId();
        assertThat(oppgaveId).isEqualTo("1230");
    }
}