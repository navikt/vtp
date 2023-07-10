package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.time.LocalDate;

public record ArbeidsavtaleDto(Integer avtaltArbeidstimerPerUke,
                               Integer stillingsprosent,
                               Integer beregnetAntallTimerPerUke,
                               LocalDate sisteLønnsendringsdato,
                               LocalDate fomGyldighetsperiode,
                               LocalDate tomGyldighetsperiode) {


    public ArbeidsavtaleDto(Builder b) {
        this(b.avtaltArbeidstimerPerUke,
             b.stillingsprosent,
             b.beregnetAntallTimerPerUke,
             b.sisteLønnsendringsdato,
             b.fomGyldighetsperiode,
             b.tomGyldighetsperiode);
    }

    public static Builder arbeidsavtale(LocalDate fom, LocalDate tom) {
        return new Builder(fom, tom);
    }

    public static Builder arbeidsavtale(LocalDate fom) {
        return new Builder(fom, null);
    }

    public static class Builder {
        private Integer avtaltArbeidstimerPerUke;
        private Integer stillingsprosent;
        private Integer beregnetAntallTimerPerUke;
        private LocalDate sisteLønnsendringsdato;
        private LocalDate fomGyldighetsperiode;
        private LocalDate tomGyldighetsperiode;
        Builder(LocalDate fom, LocalDate tom) {
            avtaltArbeidstimerPerUke = 40;
            stillingsprosent = 100;
            beregnetAntallTimerPerUke = 40;
            sisteLønnsendringsdato = LocalDate.now().minusMonths(6);
            fomGyldighetsperiode = fom;
            tomGyldighetsperiode = tom;
        }

        public Builder avtaltArbeidstimerPerUke(Integer avtaltArbeidstimerPerUke) {
            this.avtaltArbeidstimerPerUke = avtaltArbeidstimerPerUke;
            return this;
        }

        public Builder stillingsprosent(Integer stillingsprosent) {
            this.stillingsprosent = stillingsprosent;
            this.avtaltArbeidstimerPerUke = this.avtaltArbeidstimerPerUke *  stillingsprosent/100;
            this.beregnetAntallTimerPerUke = this.beregnetAntallTimerPerUke *  stillingsprosent/100;
            return this;
        }

        public Builder beregnetAntallTimerPerUke(Integer beregnetAntallTimerPerUke) {
            this.beregnetAntallTimerPerUke = beregnetAntallTimerPerUke;
            return this;
        }

        public Builder sisteLønnsendringsdato(LocalDate sisteLønnsendringsdato) {
            this.sisteLønnsendringsdato = sisteLønnsendringsdato;
            return this;
        }

        public Builder fomGyldighetsperiode(LocalDate fomGyldighetsperiode) {
            this.fomGyldighetsperiode = fomGyldighetsperiode;
            return this;
        }

        public Builder tomGyldighetsperiode(LocalDate tomGyldighetsperiode) {
            this.tomGyldighetsperiode = tomGyldighetsperiode;
            return this;
        }

        public ArbeidsavtaleDto build() {
            return new ArbeidsavtaleDto(this);
        }
    }

}
