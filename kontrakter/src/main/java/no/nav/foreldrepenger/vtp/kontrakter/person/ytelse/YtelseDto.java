package no.nav.foreldrepenger.vtp.kontrakter.person.ytelse;

import java.time.LocalDate;
import java.util.List;

public record YtelseDto(YtelseType ytelse,
                        LocalDate fra,
                        LocalDate til,
                        Integer dagsats,
                        Integer utbetalt,
                        List<Beregningsgrunnlag> beregningsgrunnlag) {

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(YtelseDto existing) {
        return new Builder(existing);
    }

    public static class Builder {
        private YtelseType ytelse;
        private LocalDate fra;
        private LocalDate til;
        private Integer dagsats;
        private Integer utbetalt;
        private List<Beregningsgrunnlag> beregningsgrunnlag = List.of();

        public Builder() {}

        public Builder(YtelseDto existing) {
            this.ytelse = existing.ytelse();
            this.fra = existing.fra();
            this.til = existing.til();
            this.dagsats = existing.dagsats();
            this.utbetalt = existing.utbetalt();
            this.beregningsgrunnlag = existing.beregningsgrunnlag();
        }

        public Builder medYtelse(YtelseType ytelse) { this.ytelse = ytelse; return this; }
        public Builder medFra(LocalDate fra) { this.fra = fra; return this; }
        public Builder medTil(LocalDate til) { this.til = til; return this; }
        public Builder medDagsats(Integer dagsats) { this.dagsats = dagsats; return this; }
        public Builder medUtbetalt(Integer utbetalt) { this.utbetalt = utbetalt; return this; }
        public Builder medBeregningsgrunnlag(List<Beregningsgrunnlag> beregningsgrunnlag) { this.beregningsgrunnlag = beregningsgrunnlag; return this; }

        public YtelseDto build() {
            return new YtelseDto(ytelse, fra, til, dagsats, utbetalt, beregningsgrunnlag);
        }
    }
}
