package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import java.util.ArrayList;
import java.util.List;

/**
 * v2 av scenario-kontrakten for person-oppsett. Eksponeres via POST /rest/api/testscenarios/v2/personer.
 * v1 (no.nav.foreldrepenger.vtp.kontrakter.person.PersonDto, /opprett) berøres ikke.
 */
public record PersonDto(PersonopplysningerDto personopplysninger,
                        List<ArbeidsforholdDto> arbeidsforhold,
                        List<InntektsperiodeDto> inntekt,
                        List<YtelseDto> ytelser,
                        List<SkatteopplysningDto> skatteopplysninger) {

    public PersonDto {
        arbeidsforhold = arbeidsforhold != null ? arbeidsforhold : List.of();
        inntekt = inntekt != null ? inntekt : List.of();
        ytelser = ytelser != null ? ytelser : List.of();
        skatteopplysninger = skatteopplysninger != null ? skatteopplysninger : List.of();
    }

    private PersonDto(Builder b) {
        this(b.personopplysninger, b.arbeidsforhold, b.inntekt, b.ytelser, b.skatteopplysninger);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PersonopplysningerDto personopplysninger;
        private List<ArbeidsforholdDto> arbeidsforhold = new ArrayList<>();
        private List<InntektsperiodeDto> inntekt = new ArrayList<>();
        private List<YtelseDto> ytelser = new ArrayList<>();
        private List<SkatteopplysningDto> skatteopplysninger = new ArrayList<>();

        Builder() {
        }

        public Builder personopplysninger(PersonopplysningerDto personopplysninger) {
            this.personopplysninger = personopplysninger;
            return this;
        }

        public Builder arbeidsforhold(List<ArbeidsforholdDto> arbeidsforhold) {
            this.arbeidsforhold = arbeidsforhold;
            return this;
        }

        public Builder arbeidsforhold(ArbeidsforholdDto arbeidsforhold) {
            this.arbeidsforhold.add(arbeidsforhold);
            return this;
        }

        public Builder inntekt(List<InntektsperiodeDto> inntekt) {
            this.inntekt = inntekt;
            return this;
        }

        public Builder inntekt(InntektsperiodeDto inntekt) {
            this.inntekt.add(inntekt);
            return this;
        }

        public Builder ytelser(List<YtelseDto> ytelser) {
            this.ytelser = ytelser;
            return this;
        }

        public Builder ytelse(YtelseDto.YtelseType type, java.time.LocalDate fom, java.time.LocalDate tom, Integer dagsats,
                              Integer utbetalingsgrad) {
            this.ytelser.add(new YtelseDto(type, fom, tom, dagsats, utbetalingsgrad));
            return this;
        }

        public Builder ytelse(YtelseDto.YtelseType type, java.time.LocalDate fom, java.time.LocalDate tom) {
            return ytelse(type, fom, tom, null, null);
        }

        public Builder skatteopplysninger(List<SkatteopplysningDto> skatteopplysninger) {
            this.skatteopplysninger = skatteopplysninger;
            return this;
        }

        public PersonDto build() {
            return new PersonDto(this);
        }
    }
}
