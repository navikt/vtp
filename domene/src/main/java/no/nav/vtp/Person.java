package no.nav.vtp;


import java.util.List;

import no.nav.vtp.arbeidsforhold.Arbeidsforhold;
import no.nav.vtp.inntekt.Inntektsperiode;
import no.nav.vtp.personopplysninger.Personopplysninger;
import no.nav.vtp.ytelse.Ytelse;

// Første utkast av ny modell
public record Person(Personopplysninger personopplysninger,
                     List<Arbeidsforhold> arbeidsforhold,
                     List<Inntektsperiode> inntekt,
                     List<Ytelse> ytelser) {

    public Person.Builder tilBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private Person kopi;

        public Builder(Person person) {
            this.kopi = person;
        }

        public Builder medPersonopplysninger(Personopplysninger personopplysninger) {
            kopi = new Person(personopplysninger, kopi.arbeidsforhold, kopi.inntekt, kopi.ytelser);
            return this;
        }

        public Builder medArbeidsforhold(List<Arbeidsforhold> arbeidsforhold) {
            kopi = new Person(kopi.personopplysninger, arbeidsforhold, kopi.inntekt, kopi.ytelser);
            return this;
        }

        public Builder medInntekt(List<Inntektsperiode> inntekt) {
            kopi = new Person(kopi.personopplysninger, kopi.arbeidsforhold, inntekt, kopi.ytelser);
            return this;
        }

        public Builder medYtelser(List<Ytelse> ytelser) {
            kopi = new Person(kopi.personopplysninger, kopi.arbeidsforhold, kopi.inntekt, ytelser);
            return this;
        }

        public Person build() {
            return kopi;
        }
    }
}
