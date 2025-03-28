package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.time.LocalDate;
import java.util.List;

public record ArbeidsforholdDto(List<ArbeidsavtaleDto> arbeidsavtaler,
                                String arbeidsforholdId,
                                LocalDate ansettelsesperiodeFom,
                                LocalDate ansettelsesperiodeTom,
                                Arbeidsforholdstype arbeidsforholdstype,
                                Arbeidsgiver arbeidsgiver,
                                List<PermisjonDto> permisjoner) {

    private ArbeidsforholdDto(Builder b) {
        this(b.arbeidsavtaler, b.arbeidsforholdId, b.ansettelsesperiodeFom, b.ansettelsesperiodeTom, b.arbeidsforholdstype, b.arbeidsgiver, b.permisjoner);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<ArbeidsavtaleDto> arbeidsavtaler;
        private String arbeidsforholdId;
        private LocalDate ansettelsesperiodeFom;
        private LocalDate ansettelsesperiodeTom;
        private Arbeidsforholdstype arbeidsforholdstype;
        private Arbeidsgiver arbeidsgiver;
        private List<PermisjonDto> permisjoner;

        Builder() {
        }


        public Builder arbeidsavtaler(List<ArbeidsavtaleDto> arbeidsavtaler) {
            this.arbeidsavtaler = arbeidsavtaler;
            return this;
        }

        public Builder arbeidsforholdId(String arbeidsforholdId) {
            this.arbeidsforholdId = arbeidsforholdId;
            return this;
        }

        public Builder ansettelsesperiodeFom(LocalDate ansettelsesperiodeFom) {
            this.ansettelsesperiodeFom = ansettelsesperiodeFom;
            return this;
        }

        public Builder ansettelsesperiodeTom(LocalDate ansettelsesperiodeTom) {
            this.ansettelsesperiodeTom = ansettelsesperiodeTom;
            return this;
        }


        public Builder arbeidsforholdstype(Arbeidsforholdstype arbeidsforholdstype) {
            this.arbeidsforholdstype = arbeidsforholdstype;
            return this;
        }

        public Builder arbeidsgiver(Arbeidsgiver arbeidsgiver) {
            this.arbeidsgiver = arbeidsgiver;
            return this;
        }

        public Builder permisjoner(List<PermisjonDto> permisjoner) {
            this.permisjoner = permisjoner;
            return this;
        }

        public ArbeidsforholdDto build() {
            return new ArbeidsforholdDto(this);
        }
    }
}
