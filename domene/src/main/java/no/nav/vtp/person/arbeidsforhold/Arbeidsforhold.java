package no.nav.vtp.person.arbeidsforhold;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public record Arbeidsforhold(Arbeidsgiver arbeidsgiver,
                             LocalDate ansettelsesperiodeFom,
                             LocalDate ansettelsesperiodeTom,
                             Arbeidsforholdstype arbeidsforholdstype,
                             List<Arbeidsavtale> arbeidsavtaler,
                             List<Permisjon> permisjoner,
                             long navArbeidsforholdId) {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(10000L);

    public Arbeidsforhold(Arbeidsgiver arbeidsgiver,
                          LocalDate ansettelsesperiodeFom,
                          LocalDate ansettelsesperiodeTom,
                          Arbeidsforholdstype arbeidsforholdstype,
                          List<Arbeidsavtale> arbeidsavtaler,
                          List<Permisjon> permisjoner) {
        this(arbeidsgiver, ansettelsesperiodeFom, ansettelsesperiodeTom, arbeidsforholdstype, arbeidsavtaler, permisjoner, ID_GENERATOR.incrementAndGet());
    }

    public Builder tilBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private Arbeidsforhold kopi;

        public Builder(Arbeidsforhold arbeidsforhold) {
            this.kopi = arbeidsforhold;
        }

        public Builder medArbeidsgiver(Arbeidsgiver arbeidsgiver) {
            kopi = new Arbeidsforhold(arbeidsgiver, kopi.ansettelsesperiodeFom, kopi.ansettelsesperiodeTom, kopi.arbeidsforholdstype, kopi.arbeidsavtaler, kopi.permisjoner, kopi.navArbeidsforholdId);
            return this;
        }

        public Builder medAnsettelsesperiodeFom(LocalDate ansettelsesperiodeFom) {
            kopi = new Arbeidsforhold(kopi.arbeidsgiver, ansettelsesperiodeFom, kopi.ansettelsesperiodeTom, kopi.arbeidsforholdstype, kopi.arbeidsavtaler, kopi.permisjoner, kopi.navArbeidsforholdId);
            return this;
        }

        public Builder medAnsettelsesperiodeTom(LocalDate ansettelsesperiodeTom) {
            kopi = new Arbeidsforhold(kopi.arbeidsgiver, kopi.ansettelsesperiodeFom, ansettelsesperiodeTom, kopi.arbeidsforholdstype, kopi.arbeidsavtaler, kopi.permisjoner, kopi.navArbeidsforholdId);
            return this;
        }

        public Builder medArbeidsforholdstype(Arbeidsforholdstype arbeidsforholdstype) {
            kopi = new Arbeidsforhold(kopi.arbeidsgiver, kopi.ansettelsesperiodeFom, kopi.ansettelsesperiodeTom, arbeidsforholdstype, kopi.arbeidsavtaler, kopi.permisjoner, kopi.navArbeidsforholdId);
            return this;
        }

        public Builder medArbeidsavtaler(List<Arbeidsavtale> arbeidsavtaler) {
            kopi = new Arbeidsforhold(kopi.arbeidsgiver, kopi.ansettelsesperiodeFom, kopi.ansettelsesperiodeTom, kopi.arbeidsforholdstype, arbeidsavtaler, kopi.permisjoner, kopi.navArbeidsforholdId);
            return this;
        }

        public Builder medPermisjoner(List<Permisjon> permisjoner) {
            kopi = new Arbeidsforhold(kopi.arbeidsgiver, kopi.ansettelsesperiodeFom, kopi.ansettelsesperiodeTom, kopi.arbeidsforholdstype, kopi.arbeidsavtaler, permisjoner, kopi.navArbeidsforholdId);
            return this;
        }

        public Arbeidsforhold build() {
            return kopi;
        }
    }
}
