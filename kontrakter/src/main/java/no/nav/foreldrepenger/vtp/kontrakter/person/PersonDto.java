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
}
