package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.util.ArrayList;
import java.util.List;

public abstract class InfotrygdBeregningsgrunnlagPeriodeYtelse extends InfotrygdBeregningsgrunnlag {

    private InfotrygdArbeidskategori arbeidskategori;

    private List<InfotrygdArbeidsforhold> arbeidsforhold = new ArrayList<>();

    public InfotrygdArbeidskategori getArbeidskategori() {
        return arbeidskategori;
    }

    public void setArbeidskategori(InfotrygdArbeidskategori arbeidskategori) {
        this.arbeidskategori = arbeidskategori;
    }

    public List<InfotrygdArbeidsforhold> getArbeidsforhold() {
        return arbeidsforhold;
    }

    public void setArbeidsforhold(List<InfotrygdArbeidsforhold> arbeidsforhold) {
        this.arbeidsforhold.clear();
        this.arbeidsforhold.addAll(arbeidsforhold);
    }
}
