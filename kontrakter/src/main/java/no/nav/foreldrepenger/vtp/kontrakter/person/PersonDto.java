package no.nav.foreldrepenger.vtp.kontrakter.person;

import java.util.List;

import no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold.ArbeidsforholdDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.inntekt.InntektsperiodeDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.PersonopplysningerDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.skatt.SkatteopplysningDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.ytelse.YtelseDto;

public record PersonDto(PersonopplysningerDto personopplysninger,
                        List<ArbeidsforholdDto> arbeidsforhold,
                        List<InntektsperiodeDto> inntekt,
                        List<YtelseDto> ytelser,
                        List<SkatteopplysningDto> skatteopplysninger) {

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(PersonDto existing) {
        return new Builder(existing);
    }

    public static class Builder {
        private PersonopplysningerDto personopplysninger;
        private List<ArbeidsforholdDto> arbeidsforhold = List.of();
        private List<InntektsperiodeDto> inntekt = List.of();
        private List<YtelseDto> ytelser = List.of();
        private List<SkatteopplysningDto> skatteopplysninger = List.of();

        public Builder() {}

        public Builder(PersonDto existing) {
            this.personopplysninger = existing.personopplysninger();
            this.arbeidsforhold = existing.arbeidsforhold();
            this.inntekt = existing.inntekt();
            this.ytelser = existing.ytelser();
            this.skatteopplysninger = existing.skatteopplysninger();
        }

        public Builder medPersonopplysninger(PersonopplysningerDto personopplysninger) {
            this.personopplysninger = personopplysninger;
            return this;
        }

        public Builder medArbeidsforhold(List<ArbeidsforholdDto> arbeidsforhold) {
            this.arbeidsforhold = arbeidsforhold;
            return this;
        }

        public Builder medInntekt(List<InntektsperiodeDto> inntekt) {
            this.inntekt = inntekt;
            return this;
        }

        public Builder medYtelser(List<YtelseDto> ytelser) {
            this.ytelser = ytelser;
            return this;
        }

        public Builder medSkatteopplysninger(List<SkatteopplysningDto> skatteopplysninger) {
            this.skatteopplysninger = skatteopplysninger;
            return this;
        }

        public PersonDto build() {
            return new PersonDto(personopplysninger, arbeidsforhold, inntekt, ytelser, skatteopplysninger);
        }
    }
}
