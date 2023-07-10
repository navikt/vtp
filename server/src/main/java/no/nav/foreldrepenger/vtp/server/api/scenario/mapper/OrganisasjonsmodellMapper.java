package no.nav.foreldrepenger.vtp.server.api.scenario.mapper;

import static no.nav.foreldrepenger.fpwsproxy.UtilKlasse.safeStream;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import no.nav.foreldrepenger.vtp.kontrakter.v2.ArbeidsforholdDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.OrganisasjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PersonDto;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonDetaljerModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModeller;

public class OrganisasjonsmodellMapper {

    private OrganisasjonsmodellMapper() {
        // Bare statiske metoder
    }

    public static OrganisasjonModeller tilOrganisasjonsmodeller(PersonDto søker) {
        var organisasjonsmodellSøker = leggTilOrganisasjonerFraArbeidsforhold(søker);
        return new OrganisasjonModeller(organisasjonsmodellSøker.stream().toList());
    }

    public static OrganisasjonModeller tilOrganisasjonsmodeller(PersonDto søker, PersonDto annenpart) {
        var organisasjonsmodellSøker = leggTilOrganisasjonerFraArbeidsforhold(søker);
        var organisasjonsmodellAnnenpart = leggTilOrganisasjonerFraArbeidsforhold(annenpart);
        return new OrganisasjonModeller(Stream.concat(organisasjonsmodellSøker.stream(), organisasjonsmodellAnnenpart.stream())
                .distinct()
                .toList());
    }

    private static Set<OrganisasjonModell> leggTilOrganisasjonerFraArbeidsforhold(PersonDto søker) {
        if (søker.inntektytelse() == null || søker.inntektytelse().aareg() == null) {
            return Set.of();
        }
        return safeStream(søker.inntektytelse().aareg().arbeidsforhold()).filter(a -> a.arbeidsgiver() instanceof OrganisasjonDto)
                .map(OrganisasjonsmodellMapper::tilOrganisasjonsmodell)
                .collect(Collectors.toSet());
    }

    private static OrganisasjonModell tilOrganisasjonsmodell(ArbeidsforholdDto arbeidsforholdDto) {
        var organisasjon = (OrganisasjonDto) arbeidsforholdDto.arbeidsgiver();
        var organisasjonsdetaljer = organisasjon.organisasjonsdetaljer();
        return new OrganisasjonModell(
                organisasjon.orgnummer().value(),
                null,
                new OrganisasjonModell.Navn(new String[] {organisasjonsdetaljer.navn()}),
                new OrganisasjonDetaljerModell(
                        organisasjonsdetaljer.registreringsdato(),
                        organisasjonsdetaljer.datoSistEndret(),
                        null,
                        null
                ));
    }
}
