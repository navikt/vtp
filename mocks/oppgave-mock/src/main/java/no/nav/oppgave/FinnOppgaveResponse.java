package no.nav.oppgave;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

class FinnOppgaveResponse {

    private final long antallTreffTotalt;
    private final List<OppgaveJson> oppgaver;

    FinnOppgaveResponse(List<OppgaveJson> oppgaver, long antallTreffTotalt) {
        this.oppgaver = oppgaver;
        this.antallTreffTotalt = antallTreffTotalt;
    }

    @JsonProperty("oppgaver")
    @ApiModelProperty(value = "Liste over oppgaver")
    public List<OppgaveJson> getOppgaver() {
        return oppgaver;
    }

    @JsonProperty("antallTreffTotalt")
    @ApiModelProperty(value = "Totalt antall oppgaver funnet med dette søket")
    public long getAntallTreffTotalt() {
        return antallTreffTotalt;
    }
}
