package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
