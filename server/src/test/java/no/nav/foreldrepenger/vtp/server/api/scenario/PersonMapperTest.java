package no.nav.foreldrepenger.vtp.server.api.scenario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.kontrakter.person.v2.AdresseDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.GeografiskTilknytningDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.InntektsperiodeDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.Kjønn;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.MedlemskapDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.OrganisasjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.PersonopplysningerDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.RegistrertNæringsvirksomhetDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.Rolle;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.Språk;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.StatsborgerskapDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.YtelseDto;
import no.nav.vtp.person.ident.PersonIdent;
import no.nav.vtp.person.inntekt.Inntektsperiode;
import no.nav.vtp.person.personopplysninger.Landkode;

class PersonMapperTest {

    @Test
    void normalisererAlpha3LandkoderTilStoreBokstaverNårDomenemodellenOpprettes() {
        var uuid = UUID.randomUUID();
        var personopplysninger = PersonopplysningerDto.builder()
                .uuid(uuid)
                .geografiskTilknytning(new GeografiskTilknytningDto("nor",
                        GeografiskTilknytningDto.GeografiskTilknytningType.LAND))
                .statsborgerskap(List.of(new StatsborgerskapDto("swe")))
                .medlemskap(List.of(new MedlemskapDto(null, null, "deu", MedlemskapDto.DekningsType.FULL)))
                .adresser(List.of(new AdresseDto(AdresseDto.AdresseType.BOSTEDSADRESSE, "nld", null, null, null)))
                .build();
        var dto = PersonDto.builder().personopplysninger(personopplysninger).build();

        var person = PersonMapper.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.personopplysninger().geografiskTilknytning().land()).isEqualTo(Landkode.NORGE);
        assertThat(person.personopplysninger().statsborgerskap().getFirst().land()).isEqualTo("SWE");
        assertThat(person.personopplysninger().medlemskap().getFirst().land()).isEqualTo("DEU");
        assertThat(person.personopplysninger().adresser().adresser().getFirst().land()).isEqualTo("NLD");
    }

    @Test
    void avviserAlpha2LandkodeNårDomenemodellenOpprettes() {
        var uuid = UUID.randomUUID();
        var personopplysninger = PersonopplysningerDto.builder()
                .uuid(uuid)
                .geografiskTilknytning(new GeografiskTilknytningDto("NO", GeografiskTilknytningDto.GeografiskTilknytningType.LAND))
                .build();
        var dto = PersonDto.builder().personopplysninger(personopplysninger).build();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> PersonMapper.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty()))
                .withMessage("Ugyldig landkode 'NO'. Kun landkoder med 3 tegn støttes");
    }

    @Test
    void ytelseMedDagsatsGenererInntektsperiode() {
        var uuid = UUID.randomUUID();
        var dto = personMedYtelse(uuid, YtelseDto.YtelseType.SYKEPENGER, 1_000).build();

        var person = PersonMapper.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.inntekt()).hasSize(1);
        assertThat(person.inntekt().getFirst().ytelseType()).isEqualTo(Inntektsperiode.YtelseType.SYKEPENGER);
    }

    @Test
    void uføretrygdGenererIkkeInntektsperiodeSelvMedDagsats() {
        var uuid = UUID.randomUUID();
        var dto = personMedYtelse(uuid, YtelseDto.YtelseType.UFØREPENSJON, 1_000).build();

        var person = PersonMapper.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.inntekt()).isEmpty();
        assertThat(person.ytelser()).hasSize(1);
    }

    @Test
    void ytelseUtenDagsatsGenererIkkeInntektsperiode() {
        var uuid = UUID.randomUUID();
        var dto = personMedYtelse(uuid, YtelseDto.YtelseType.DAGPENGER, null).build();

        var person = PersonMapper.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.inntekt()).isEmpty();
    }

    @Test
    void dagsatsKonverteresTilMånedligBeløpMed260VirkedagerPerÅr() {
        var uuid = UUID.randomUUID();
        var dto = personMedYtelse(uuid, YtelseDto.YtelseType.ARBEIDSAVKLARINGSPENGER, 1_000).build();

        var person = PersonMapper.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        var forventetMånedsbeløp = Math.round(1_000 * 260f / 12);
        assertThat(person.inntekt().getFirst().beløp()).isEqualTo(forventetMånedsbeløp);
    }

    @Test
    void avledetInntektsperiodeHarNavSomArbeidsgiver() {
        var uuid = UUID.randomUUID();
        var dto = personMedYtelse(uuid, YtelseDto.YtelseType.PLEIEPENGER, 800).build();

        var person = PersonMapper.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

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

        var person = PersonMapper.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.inntekt()).singleElement()
                .extracting(Inntektsperiode::ytelseType)
                .isEqualTo(Inntektsperiode.YtelseType.FASTLØNN);
    }

    @Test
    void mapperRegistrerteNæringsvirksomheterFraBrreg() {
        var uuid = UUID.randomUUID();
        var virksomhet = new RegistrertNæringsvirksomhetDto(
                "999999999", "VTP FISKE", "ENK", "Enkeltpersonforetak", "03.110", "Hav- og kystfiske");
        var dto = person(uuid)
                .registrerteNæringsvirksomheter(List.of(virksomhet))
                .build();

        var person = PersonMapper.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.registrerteNæringsvirksomheter()).singleElement().satisfies(mapped -> {
            assertThat(mapped.organisasjonsnummer()).isEqualTo("999999999");
            assertThat(mapped.navn()).isEqualTo("VTP FISKE");
            assertThat(mapped.organisasjonsformKode()).isEqualTo("ENK");
            assertThat(mapped.organisasjonsformBeskrivelse()).isEqualTo("Enkeltpersonforetak");
            assertThat(mapped.næringskode()).isEqualTo("03.110");
            assertThat(mapped.næringskodeBeskrivelse()).isEqualTo("Hav- og kystfiske");
        });
    }

    @Test
    void manglendeBrregDataGirIngenRegistrerteNæringsvirksomheter() {
        var uuid = UUID.randomUUID();

        var person = PersonMapper.tilPerson(person(uuid).build(), Map.of(uuid, new PersonIdent("12345678901")),
                Optional.empty());

        assertThat(person.registrerteNæringsvirksomheter()).isEmpty();
    }

    @Test
    void nullVirksomhetslisteGirIngenRegistrerteNæringsvirksomheter() {
        var uuid = UUID.randomUUID();
        var dto = person(uuid)
                .registrerteNæringsvirksomheter(null)
                .build();

        var person = PersonMapper.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.registrerteNæringsvirksomheter()).isEmpty();
    }

    private static PersonDto.Builder personMedYtelse(UUID uuid, YtelseDto.YtelseType type, Integer dagsats) {
        return person(uuid).ytelse(type, LocalDate.now().minusMonths(1), LocalDate.now(), dagsats, 100);
    }

    private static PersonDto.Builder person(UUID uuid) {
        return PersonDto.builder()
                .personopplysninger(PersonopplysningerDto.builder()
                        .uuid(uuid)
                        .rolle(Rolle.MOR)
                        .kjønn(Kjønn.K)
                        .språk(Språk.NB)
                        .fødselsdato(LocalDate.now().minusYears(30))
                        .build());
    }
}
