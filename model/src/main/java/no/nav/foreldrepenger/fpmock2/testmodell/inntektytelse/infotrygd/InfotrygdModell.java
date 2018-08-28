package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.fpmock2.testmodell.Feilkode;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdBeregningsgrunnlag;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.ytelse.InfotrygdYtelse;

public class InfotrygdModell {

    @JsonProperty("feilkode")
    private Feilkode feilkode;
    
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("ytelser")
    private List<InfotrygdYtelse> ytelser = new ArrayList<>();
    
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("grunnlag")
    private List<InfotrygdBeregningsgrunnlag> grunnlag = new ArrayList<>();

    public Feilkode getFeilkode() {
        return feilkode;
    }

    public void setFeilkode(Feilkode feilkode) {
        this.feilkode = feilkode;
    }

    public List<InfotrygdYtelse> getYtelser() {
        return Collections.unmodifiableList(ytelser);
    }

    public void setYtelser(List<InfotrygdYtelse> ytelser) {
        this.ytelser.clear();
        this.ytelser.addAll(ytelser);
    }

    public List<InfotrygdBeregningsgrunnlag> getGrunnlag() {
        return Collections.unmodifiableList(grunnlag);
    }

    public void setGrunnlag(List<InfotrygdBeregningsgrunnlag> grunnlag) {
        this.grunnlag.clear();
        this.grunnlag.addAll(grunnlag);
    }
    
}
