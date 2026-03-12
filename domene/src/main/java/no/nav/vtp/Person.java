package no.nav.vtp;


import java.util.List;

import no.nav.vtp.arbeidsforhold.Arbeidsforhold;
import no.nav.vtp.inntekt.Inntektsperiode;
import no.nav.vtp.personopplysninger.Personopplysninger;
import no.nav.vtp.skatt.Skatteopplysning;
import no.nav.vtp.ytelse.Ytelse;

public record Person(Personopplysninger personopplysninger,
                     List<Arbeidsforhold> arbeidsforhold,
                     List<Inntektsperiode> inntekt,
                     List<Ytelse> ytelser,
                     List<Skatteopplysning> skatteopplysninger) {

    public Person.Builder tilBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private Person kopi;

        public Builder(Person person) {
            this.kopi = person;
        }

        public Builder medPersonopplysninger(Personopplysninger personopplysninger) {
            kopi = new Person(personopplysninger, kopi.arbeidsforhold, kopi.inntekt, kopi.ytelser, kopi.skatteopplysninger);
            return this;
        }

        public Builder medArbeidsforhold(List<Arbeidsforhold> arbeidsforhold) {
            kopi = new Person(kopi.personopplysninger, arbeidsforhold, kopi.inntekt, kopi.ytelser, kopi.skatteopplysninger);
            return this;
        }

        public Builder medInntekt(List<Inntektsperiode> inntekt) {
            kopi = new Person(kopi.personopplysninger, kopi.arbeidsforhold, inntekt, kopi.ytelser, kopi.skatteopplysninger);
            return this;
        }

        public Builder medYtelser(List<Ytelse> ytelser) {
            kopi = new Person(kopi.personopplysninger, kopi.arbeidsforhold, kopi.inntekt, ytelser, kopi.skatteopplysninger);
            return this;
        }

        public Builder medSkatteopplysninger(List<Skatteopplysning> skatteopplysninger) {
            kopi = new Person(kopi.personopplysninger, kopi.arbeidsforhold, kopi.inntekt, kopi.ytelser, skatteopplysninger);
            return this;
        }

        public Person build() {
            return kopi;
        }
    }
}
