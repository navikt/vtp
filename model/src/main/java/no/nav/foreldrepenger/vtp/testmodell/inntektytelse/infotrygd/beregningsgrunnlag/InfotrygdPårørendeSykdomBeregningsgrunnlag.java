package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("pårørendesykdom")
public class InfotrygdPårørendeSykdomBeregningsgrunnlag extends InfotrygdBeregningsgrunnlagPeriodeYtelse {

    private LocalDate fødselsdatoPleietrengende;

    public LocalDate getFødselsdatoPleietrengende() {
        return fødselsdatoPleietrengende;
    }

    public void setFødselsdatoPleietrengende(LocalDate fødselsdatoPleietrengende) {
        this.fødselsdatoPleietrengende = fødselsdatoPleietrengende;
    }
}
