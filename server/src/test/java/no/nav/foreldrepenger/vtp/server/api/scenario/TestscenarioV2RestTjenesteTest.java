package no.nav.foreldrepenger.vtp.server.api.scenario;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.vtp.kontrakter.v2.AdresseDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.FamilierelasjonModellDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.GeografiskTilknytningDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.InntektYtelseModellDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.InntektYtelseType;
import no.nav.foreldrepenger.vtp.kontrakter.v2.InntektkomponentDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.InntektsperiodeDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Kjønn;
import no.nav.foreldrepenger.vtp.kontrakter.v2.MedlemskapDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.OrganisasjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Orgnummer;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PersonstatusDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PrivatArbeidsgiver;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Rolle;
import no.nav.foreldrepenger.vtp.kontrakter.v2.SivilstandDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.StatsborgerskapDto;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

class TestscenarioV2RestTjenesteTest {

    // TODO:
    @Test
    void roundtripTestAvOpprettingAvTestcenario() {
        var testscenarioV2RestTjeneste = new TestscenarioV2RestTjeneste(new DelegatingTestscenarioRepository(
                TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance())));

        var morID = UUID.randomUUID();
        var farID = UUID.randomUUID();
        var barnID = UUID.randomUUID();
        var privateAG = UUID.randomUUID();

        var mor = mor(LocalDate.now().minusYears(23))
                .familierelasjoner(List.of(
                        new FamilierelasjonModellDto(FamilierelasjonModellDto.Relasjon.EKTE, farID),
                        new FamilierelasjonModellDto(FamilierelasjonModellDto.Relasjon.BARN, barnID)
                ))
                .inntektytelse(new InntektYtelseModellDto(
                        null,
                        null,
                        new InntektkomponentDto(
                                List.of(new InntektsperiodeDto(LocalDate.now(), null, 2000,
                                        InntektYtelseType.FASTLØNN, InntektsperiodeDto.InntektFordelDto.KONTANTYTELSE, new PrivatArbeidsgiver(privateAG)))),
                        null,
                        null,
                        null))
                .id(morID)
                .build();
        var far = far(LocalDate.now().minusYears(23))
                .inntektytelse(new InntektYtelseModellDto(
                        null,
                        null,
                        new InntektkomponentDto(
                                List.of(new InntektsperiodeDto(LocalDate.now(), null, 2000,
                                        InntektYtelseType.FASTLØNN, InntektsperiodeDto.InntektFordelDto.KONTANTYTELSE, new OrganisasjonDto(new Orgnummer("99999999"), null)))),
                        null,
                        null,
                        null))
                .familierelasjoner(List.of(
                        new FamilierelasjonModellDto(FamilierelasjonModellDto.Relasjon.EKTE, morID),
                        new FamilierelasjonModellDto(FamilierelasjonModellDto.Relasjon.BARN, barnID)
                        ))
                .id(farID)
                .build();
        var barn = PersonDto.builder()
                .id(barnID)
                .rolle(Rolle.BARN)
                .fødselsdato(LocalDate.now())
                .build();
        var privatArbeidsgiver = PersonDto.builder()
                .id(privateAG)
                .rolle(Rolle.PRIVATE_ARBEIDSGIVER)
                .build();


        var response = testscenarioV2RestTjeneste.initialiserTestScenarioFraDto(List.of(mor, far, barn, privatArbeidsgiver));
        var dto = response.getEntity();
        assertThat(dto).isNotNull();
    }

    private static PersonDto.Builder mor(LocalDate fødselsdato) {
        return PersonDto.builder()
                .rolle(Rolle.MOR)
                .kjønn(Kjønn.K)
                .fødselsdato(fødselsdato)
                .språk("NB")
                .geografiskTilknytning(GeografiskTilknytningDto.norsk())
                .adresser(norskAdresse())
                .personstatus(bosattFra(fødselsdato))
                .sivilstand(ugift())
                .medlemskap(norskMedlemskap())
                .statsborgerskap(norskStatsborgerskap())
                ;
    }

    private static PersonDto.Builder far(LocalDate fødselsdato) {
        return PersonDto.builder()
                .rolle(Rolle.FAR)
                .kjønn(Kjønn.M)
                .fødselsdato(fødselsdato)
                .språk("NB")
                .geografiskTilknytning(GeografiskTilknytningDto.norsk())
                .adresser(norskAdresse())
                .personstatus(bosattFra(fødselsdato))
                .sivilstand(ugift())
                .medlemskap(norskMedlemskap())
                .statsborgerskap(norskStatsborgerskap())
                ;
    }

    private static List<StatsborgerskapDto> norskStatsborgerskap() {
        return List.of(new StatsborgerskapDto(CountryCode.NO));
    }

    private static List<MedlemskapDto> norskMedlemskap() {
        return List.of(); // Her skal bare utenlandske medlemskap spesifiseres
    }

    private static List<SivilstandDto> gift() {
        return List.of(new SivilstandDto(SivilstandDto.Sivilstander.GIFT, LocalDate.now().minusYears(4), null));
    }

    private static List<SivilstandDto> ugift() {
        return List.of(new SivilstandDto(SivilstandDto.Sivilstander.UGIF, null, null));
    }

    private static List<PersonstatusDto> bosattFra(LocalDate fom) {
        return List.of(new PersonstatusDto(PersonstatusDto.Personstatuser.BOSA, fom, null));
    }

    private static List<AdresseDto> norskAdresse() {
        var adresse = new AdresseDto(
                AdresseDto.AdresseType.BOSTEDSADRESSE,
                CountryCode.NO,
                "000000002",   // TODO: Hva skjer om jeg alltid setter denne?
                LocalDate.now().minusYears(5),
                null
        );

        return List.of(adresse);
    }
}
