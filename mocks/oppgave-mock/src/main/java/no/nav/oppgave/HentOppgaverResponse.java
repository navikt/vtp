package no.nav.oppgave;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.v3.oas.annotations.media.Schema;

class HentOppgaverResponse {
    private final int antallTreffTotalt;
    private final List<ObjectNode> oppgaver;

    HentOppgaverResponse(List<ObjectNode> oppgaver) {
        this.antallTreffTotalt = oppgaver.size();
        this.oppgaver = oppgaver;
    }

    @JsonProperty("oppgaver")
    @Schema(description = "Liste over oppgaver")
    public List<ObjectNode> getOppgaver() {
        return oppgaver;
    }

    @JsonProperty("antallTreffTotalt")
    @Schema(description = "Totalt antall oppgaver funnet med dette søket")
    public long getAntallTreffTotalt() {
        return antallTreffTotalt;
    }
}
