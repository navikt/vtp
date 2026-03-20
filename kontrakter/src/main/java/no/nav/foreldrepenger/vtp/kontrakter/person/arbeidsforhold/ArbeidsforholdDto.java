package no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold;

import java.time.LocalDate;
import java.util.List;

public record ArbeidsforholdDto(ArbeidsgiverDto arbeidsgiver,
                                String arbeidsforholdId,
                                LocalDate ansettelsesperiodeFom,
                                LocalDate ansettelsesperiodeTom,
                                Arbeidsforholdstype arbeidsforholdstype,
                                List<ArbeidsavtaleDto> arbeidsavtaler,
                                List<PermisjonDto> permisjoner) {

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ArbeidsforholdDto existing) {
        return new Builder(existing);
    }

    public static class Builder {
        private ArbeidsgiverDto arbeidsgiver;
        private String arbeidsforholdId;
        private LocalDate ansettelsesperiodeFom;
        private LocalDate ansettelsesperiodeTom;
        private Arbeidsforholdstype arbeidsforholdstype;
        private List<ArbeidsavtaleDto> arbeidsavtaler = List.of();
        private List<PermisjonDto> permisjoner = List.of();

        public Builder() {}

        public Builder(ArbeidsforholdDto existing) {
            this.arbeidsgiver = existing.arbeidsgiver();
            this.arbeidsforholdId = existing.arbeidsforholdId();
            this.ansettelsesperiodeFom = existing.ansettelsesperiodeFom();
            this.ansettelsesperiodeTom = existing.ansettelsesperiodeTom();
            this.arbeidsforholdstype = existing.arbeidsforholdstype();
            this.arbeidsavtaler = existing.arbeidsavtaler();
            this.permisjoner = existing.permisjoner();
        }

        public Builder medArbeidsgiver(ArbeidsgiverDto arbeidsgiver) { this.arbeidsgiver = arbeidsgiver; return this; }
        public Builder medArbeidsforholdId(String arbeidsforholdId) { this.arbeidsforholdId = arbeidsforholdId; return this; }
        public Builder medAnsettelsesperiodeFom(LocalDate ansettelsesperiodeFom) { this.ansettelsesperiodeFom = ansettelsesperiodeFom; return this; }
        public Builder medAnsettelsesperiodeTom(LocalDate ansettelsesperiodeTom) { this.ansettelsesperiodeTom = ansettelsesperiodeTom; return this; }
        public Builder medArbeidsforholdstype(Arbeidsforholdstype arbeidsforholdstype) { this.arbeidsforholdstype = arbeidsforholdstype; return this; }
        public Builder medArbeidsavtaler(List<ArbeidsavtaleDto> arbeidsavtaler) { this.arbeidsavtaler = arbeidsavtaler; return this; }
        public Builder medPermisjoner(List<PermisjonDto> permisjoner) { this.permisjoner = permisjoner; return this; }

        public ArbeidsforholdDto build() {
            return new ArbeidsforholdDto(arbeidsgiver, arbeidsforholdId, ansettelsesperiodeFom,
                    ansettelsesperiodeTom, arbeidsforholdstype, arbeidsavtaler, permisjoner);
        }
    }
}
