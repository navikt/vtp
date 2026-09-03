package no.nav.foreldrepenger.vtp.server.api.scenario;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn;
import no.nav.foreldrepenger.vtp.kontrakter.person.Rolle;
import no.nav.foreldrepenger.vtp.kontrakter.person.Språk;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.InntektsperiodeDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.OrganisasjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.PersonopplysningerDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.YtelseDto;
import no.nav.vtp.person.ident.PersonIdent;
import no.nav.vtp.person.inntekt.Inntektsperiode;

class PersonMapperV2Test {

    private static PersonDto.Builder personMedYtelse(UUID uuid, YtelseDto.YtelseType type, Integer dagsats) {
        return PersonDto.builder()
                .personopplysninger(PersonopplysningerDto.builder()
                        .uuid(uuid)
                        .rolle(Rolle.MOR)
                        .kjønn(Kjønn.K)
                        .språk(Språk.NB)
                        .fødselsdato(LocalDate.now().minusYears(30))
                        .build())
                .ytelse(type, LocalDate.now().minusMonths(1), LocalDate.now(), dagsats, 100);
    }

    @Test
    void ytelseMedDagsatsGenererInntektsperiode() {
        var uuid = UUID.randomUUID();
        var dto = personMedYtelse(uuid, YtelseDto.YtelseType.SYKEPENGER, 1_000).build();

        var person = PersonMapperV2.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.inntekt()).hasSize(1);
        assertThat(person.inntekt().getFirst().ytelseType()).isEqualTo(Inntektsperiode.YtelseType.SYKEPENGER);
    }

    @Test
    void uføretrygdGenererIkkeInntektsperiodeSelvMedDagsats() {
        var uuid = UUID.randomUUID();
        var dto = personMedYtelse(uuid, YtelseDto.YtelseType.UFØREPENSJON, 1_000).build();

        var person = PersonMapperV2.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.inntekt()).isEmpty();
        assertThat(person.ytelser()).hasSize(1);
    }

    @Test
    void ytelseUtenDagsatsGenererIkkeInntektsperiode() {
        var uuid = UUID.randomUUID();
        var dto = personMedYtelse(uuid, YtelseDto.YtelseType.DAGPENGER, null).build();

        var person = PersonMapperV2.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.inntekt()).isEmpty();
    }

    @Test
    void dagsatsKonverteresTilMånedligBeløpMed260VirkedagerPerÅr() {
        var uuid = UUID.randomUUID();
        var dto = personMedYtelse(uuid, YtelseDto.YtelseType.ARBEIDSAVKLARINGSPENGER, 1_000).build();

        var person = PersonMapperV2.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        var forventetMånedsbeløp = Math.round(1_000 * 260f / 12);
        assertThat(person.inntekt().getFirst().beløp()).isEqualTo(forventetMånedsbeløp);
    }

    @Test
    void avledetInntektsperiodeHarNavSomArbeidsgiver() {
        var uuid = UUID.randomUUID();
        var dto = personMedYtelse(uuid, YtelseDto.YtelseType.PLEIEPENGER, 800).build();

        var person = PersonMapperV2.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        var arbeidsgiver = person.inntekt().getFirst().arbeidsgiver();
        assertThat(arbeidsgiver.identifikator()).isEqualTo("991013628");
    }

    @Test
    void eksplisittLønnsinntektBeholderYtelseType() {
        var uuid = UUID.randomUUID();
        var arbeidsgiver = new OrganisasjonDto(new OrganisasjonDto.Orgnummer("123456789"), null);
        var inntekt = new InntektsperiodeDto(arbeidsgiver, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31), 50_000,
                InntektsperiodeDto.YtelseType.FASTLØNN, InntektsperiodeDto.FordelType.KONTANTYTELSE);
        var dto = personMedYtelse(uuid, YtelseDto.YtelseType.DAGPENGER, null).inntekt(inntekt).build();

        var person = PersonMapperV2.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.inntekt()).singleElement()
                .extracting(Inntektsperiode::ytelseType)
                .isEqualTo(Inntektsperiode.YtelseType.FASTLØNN);
    }

    @Test
    void registrerteNæringsvirksomheterErTommeNårV2IkkeStøtterBrreg() {
        var uuid = UUID.randomUUID();
        var dto = personMedYtelse(uuid, YtelseDto.YtelseType.DAGPENGER, null).build();

        var person = PersonMapperV2.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.registrerteNæringsvirksomheter()).isEmpty();
    }
}
