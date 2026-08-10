package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.vtp.kontrakter.person.Arbeidsforholdstype;

public record ArbeidsforholdDto(ArbeidsgiverDto arbeidsgiver,
                                String arbeidsforholdId,
                                LocalDate ansettelsesperiodeFom,
                                LocalDate ansettelsesperiodeTom,
                                Arbeidsforholdstype arbeidsforholdstype,
                                List<ArbeidsavtaleDto> arbeidsavtaler,
                                List<PermisjonDto> permisjoner) {

    private ArbeidsforholdDto(Builder b) {
        this(b.arbeidsgiver, b.arbeidsforholdId, b.ansettelsesperiodeFom, b.ansettelsesperiodeTom, b.arbeidsforholdstype,
                b.arbeidsavtaler, b.permisjoner);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ArbeidsgiverDto arbeidsgiver;
        private String arbeidsforholdId;
        private LocalDate ansettelsesperiodeFom;
        private LocalDate ansettelsesperiodeTom;
        private Arbeidsforholdstype arbeidsforholdstype;
        private List<ArbeidsavtaleDto> arbeidsavtaler = new ArrayList<>();
        private List<PermisjonDto> permisjoner = new ArrayList<>();

        Builder() {
        }

        public Builder arbeidsgiver(ArbeidsgiverDto arbeidsgiver) {
            this.arbeidsgiver = arbeidsgiver;
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

        public Builder arbeidsavtaler(List<ArbeidsavtaleDto> arbeidsavtaler) {
            this.arbeidsavtaler = arbeidsavtaler;
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
