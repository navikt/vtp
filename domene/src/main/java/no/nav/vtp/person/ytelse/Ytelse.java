
package no.nav.vtp.person.ytelse;

import java.time.LocalDate;
import java.util.List;

public record Ytelse(YtelseType ytelse,
                     LocalDate fom,
                     LocalDate tom,
                     Integer dagsats,
                     Integer utbetalt,
                     List<Beregningsgrunnlag> beregningsgrunnlag) {

    public Builder tilBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private Ytelse kopi;

        public Builder(Ytelse ytelse) {
            this.kopi = ytelse;
        }

        public Builder medYtelse(YtelseType ytelse) {
            kopi = new Ytelse(ytelse, kopi.fom, kopi.tom, kopi.dagsats, kopi.utbetalt, kopi.beregningsgrunnlag);
            return this;
        }

        public Builder medFom(LocalDate fom) {
            kopi = new Ytelse(kopi.ytelse, fom, kopi.tom, kopi.dagsats, kopi.utbetalt, kopi.beregningsgrunnlag);
            return this;
        }

        public Builder medTom(LocalDate tom) {
            kopi = new Ytelse(kopi.ytelse, kopi.fom, tom, kopi.dagsats, kopi.utbetalt, kopi.beregningsgrunnlag);
            return this;
        }

        public Builder medDagsats(Integer dagsats) {
            kopi = new Ytelse(kopi.ytelse, kopi.fom, kopi.tom, dagsats, kopi.utbetalt, kopi.beregningsgrunnlag);
            return this;
        }

        public Builder medUtbetalt(Integer utbetalt) {
            kopi = new Ytelse(kopi.ytelse, kopi.fom, kopi.tom, kopi.dagsats, utbetalt, kopi.beregningsgrunnlag);
            return this;
        }

        public Builder medBeregningsgrunnlag(List<Beregningsgrunnlag> beregningsgrunnlag) {
            kopi = new Ytelse(kopi.ytelse, kopi.fom, kopi.tom, kopi.dagsats, kopi.utbetalt, beregningsgrunnlag);
            return this;
        }

        public Ytelse build() {
            return kopi;
        }
    }
}
