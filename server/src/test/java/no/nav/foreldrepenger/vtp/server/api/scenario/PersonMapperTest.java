package no.nav.foreldrepenger.vtp.server.api.scenario;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.kontrakter.person.BrregDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.InntektYtelseModellDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn;
import no.nav.foreldrepenger.vtp.kontrakter.person.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.Rolle;
import no.nav.vtp.person.ident.PersonIdent;

class PersonMapperTest {

    @Test
    void mapperRegistrerteNæringsvirksomheterFraBrreg() {
        var uuid = UUID.randomUUID();
        var virksomhet = new BrregDto.VirksomhetDto(
                "999999999", "VTP FISKE", "ENK", "Enkeltpersonforetak", "03.110", "Hav- og kystfiske");
        var dto = person(uuid)
                .inntektytelse(InntektYtelseModellDto.builder()
                        .brreg(new BrregDto(List.of(virksomhet)))
                        .build())
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
    void manglendeInntektYtelseGirIngenRegistrerteNæringsvirksomheter() {
        var uuid = UUID.randomUUID();

        var person = PersonMapper.tilPerson(person(uuid).build(), Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.registrerteNæringsvirksomheter()).isEmpty();
    }

    @Test
    void nullVirksomhetslisteGirIngenRegistrerteNæringsvirksomheter() {
        var uuid = UUID.randomUUID();
        var dto = person(uuid)
                .inntektytelse(InntektYtelseModellDto.builder().brreg(new BrregDto(null)).build())
                .build();

        var person = PersonMapper.tilPerson(dto, Map.of(uuid, new PersonIdent("12345678901")), Optional.empty());

        assertThat(person.registrerteNæringsvirksomheter()).isEmpty();
    }

    private static PersonDto.Builder person(UUID uuid) {
        return PersonDto.builder()
                .uuid(uuid)
                .rolle(Rolle.MOR)
                .kjønn(Kjønn.K);
    }
}
