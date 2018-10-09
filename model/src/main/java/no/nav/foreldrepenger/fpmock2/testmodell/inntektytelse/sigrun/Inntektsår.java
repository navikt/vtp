package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.sigrun;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Inntektsår {

    @JsonProperty("år")
    private String år;

    @JsonProperty("oppføring")
    private List<Oppføring> oppføring;

    public String getÅr() { return år; }

    public void setÅr(String år) { this.år = år; }

    public List<Oppføring> getOppføring() { return oppføring; }

    public void setOppfoering(List<Oppføring> oppføring) { this.oppføring = oppføring; }
}
