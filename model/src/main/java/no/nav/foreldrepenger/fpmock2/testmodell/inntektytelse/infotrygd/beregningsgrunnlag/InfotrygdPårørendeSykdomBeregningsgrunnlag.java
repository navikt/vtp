package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("pårørendesykdom")
public class InfotrygdPårørendeSykdomBeregningsgrunnlag extends InfotrygdBeregningsgrunnlagPeriodeYtelse {

    @JsonProperty("fødselsdatoPleietrengende")
    private LocalDate fødselsdatoPleietrengende;

    public LocalDate getFødselsdatoPleietrengende() {
        return fødselsdatoPleietrengende;
    }

    public void setFødselsdatoPleietrengende(LocalDate fødselsdatoPleietrengende) {
        this.fødselsdatoPleietrengende = fødselsdatoPleietrengende;
    }
}
