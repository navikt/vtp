package no.nav.oppgave;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;


class HentOppgaverResponse {
    private final int antallTreffTotalt;
    private final List<ObjectNode> oppgaver;

    HentOppgaverResponse(List<ObjectNode> oppgaver) {
        this.antallTreffTotalt = oppgaver.size();
        this.oppgaver = oppgaver;
    }

    @JsonProperty("oppgaver")
    public List<ObjectNode> getOppgaver() {
        return oppgaver;
    }

    @JsonProperty("antallTreffTotalt")
    public long getAntallTreffTotalt() {
        return antallTreffTotalt;
    }
}
