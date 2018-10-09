package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.sigrun;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Inntektsår {

    @JsonProperty("år")
    private String år;

    @JsonProperty("oppføring")
    private List<Oppfoering> oppfoering;

    public String getÅr() { return år; }

    public void setÅr(String år) { this.år = år; }

    public List<Oppfoering> getOppfoering() { return oppfoering; }

    public void setOppfoering(List<Oppfoering> oppfoering) { this.oppfoering = oppfoering; }
}
