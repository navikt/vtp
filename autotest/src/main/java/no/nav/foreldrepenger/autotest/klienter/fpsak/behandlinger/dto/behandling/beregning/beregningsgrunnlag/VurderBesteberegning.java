package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.util.List;

public class VurderBesteberegning {

    protected Boolean skalHaBesteberegning;
    protected List<BrukersAndelDto> andeler;

    public Boolean getSkalHaBesteberegning() {
        return skalHaBesteberegning;
    }

    public void setSkalHaBesteberegning(Boolean skalHaBesteberegning) {
        this.skalHaBesteberegning = skalHaBesteberegning;
    }

    public List<BrukersAndelDto> getAndeler() {
        return andeler;
    }

    public void setAndeler(List<BrukersAndelDto> andeler) {
        this.andeler = andeler;
    }
}
