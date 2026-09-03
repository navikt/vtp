package no.nav.foreldrepenger.vtp.server.api.scenario;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import no.nav.foreldrepenger.vtp.kontrakter.person.v2.AdresseDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.ArbeidsavtaleDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.ArbeidsforholdDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.ArbeidsgiverDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.FamilierelasjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.GeografiskTilknytningDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.InntektsperiodeDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.Landkode;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.MedlemskapDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.OrganisasjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.PermisjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.PersonopplysningerDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.PersonstatusDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.PrivatArbeidsgiverDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.RegistrertNæringsvirksomhetDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.SkatteopplysningDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.SivilstandDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.StatsborgerskapDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.YtelseDto;
import no.nav.vtp.person.Person;
import no.nav.vtp.person.arbeidsforhold.Arbeidsavtale;
import no.nav.vtp.person.arbeidsforhold.Arbeidsforhold;
import no.nav.vtp.person.arbeidsforhold.Arbeidsforholdstype;
import no.nav.vtp.person.arbeidsforhold.Organisasjon;
import no.nav.vtp.person.arbeidsforhold.Permisjon;
import no.nav.vtp.person.arbeidsforhold.PrivatArbeidsgiver;
import no.nav.vtp.person.ident.Orgnummer;
import no.nav.vtp.person.ident.PersonIdent;
import no.nav.vtp.person.inntekt.Inntektsperiode;
import no.nav.vtp.person.næring.RegistrertNæringsvirksomhet;
import no.nav.vtp.person.personopplysninger.Adresse;
import no.nav.vtp.person.personopplysninger.Adresser;
import no.nav.vtp.person.personopplysninger.Familierelasjon;
import no.nav.vtp.person.personopplysninger.GeografiskTilknytning;
import no.nav.vtp.person.personopplysninger.Kjønn;
import no.nav.vtp.person.personopplysninger.Medlemskap;
import no.nav.vtp.person.personopplysninger.Navn;
import no.nav.vtp.person.personopplysninger.Personopplysninger;
import no.nav.vtp.person.personopplysninger.Personstatus;
import no.nav.vtp.person.personopplysninger.Rolle;
import no.nav.vtp.person.personopplysninger.Sivilstand;
import no.nav.vtp.person.personopplysninger.Statsborgerskap;
import no.nav.vtp.person.skatt.Skatteopplysning;

import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

public class PersonMapper {

    private static final Organisasjon NAV_YTELSE_BETALING = new Organisasjon(
            new Orgnummer("991013628"),
            null,
            new Organisasjon.Detaljer("NAV UTBETALING YTELSE", LocalDate.of(2003, 1, 1))
    );

    private PersonMapper() {
        // utility
    }

    public static Person tilPerson(PersonDto p, Map<UUID, PersonIdent> identer, Optional<Person> eksisterende) {
        var personopplysninger = tilPersonopplysninger(p.personopplysninger(), identer, eksisterende);
        var arbeidsforhold = tilArbeidsforhold(p.arbeidsforhold(), identer);
        var inntekt = tilInntekt(p.inntekt(), p.ytelser(), identer);
        var ytelser = tilYtelser(p.ytelser());
        var skatteopplysninger = tilSkatteopplysninger(p.skatteopplysninger());
        var registrerteNæringsvirksomheter = tilRegistrerteNæringsvirksomheter(p.registrerteNæringsvirksomheter());
        return new Person(personopplysninger, arbeidsforhold, inntekt, ytelser, skatteopplysninger, registrerteNæringsvirksomheter);
    }

    private static List<RegistrertNæringsvirksomhet> tilRegistrerteNæringsvirksomheter(
            List<RegistrertNæringsvirksomhetDto> virksomheter) {
        return virksomheter.stream()
                .map(virksomhet -> new RegistrertNæringsvirksomhet(
                        virksomhet.organisasjonsnummer(),
                        virksomhet.navn(),
                        virksomhet.organisasjonsformKode(),
                        virksomhet.organisasjonsformBeskrivelse(),
                        virksomhet.næringskode(),
                        virksomhet.næringskodeBeskrivelse()))
                .toList();
    }

    private static Personopplysninger tilPersonopplysninger(PersonopplysningerDto p, Map<UUID, PersonIdent> identer,
                                                            Optional<Person> eksisterende) {
        var navn = eksisterende
                .map(e -> e.personopplysninger().navn())
                .orElseGet(() -> generertTilfeldigNavn(p.kjønn()));
        return new Personopplysninger(
                identer.get(p.uuid()),
                p.uuid(),
                tilRolle(p.rolle()),
                navn,
                p.fødselsdato(),
                p.dødsdato(),
                tilSpråk(p.språk()),
                tilKjønn(p.kjønn()),
                tilGeografiskTilknytning(p.geografiskTilknytning()),
                tilFamilierelasjoner(p.familierelasjoner(), identer),
                tilStatsborgerskap(p.statsborgerskap()),
                tilSivilstander(p.sivilstand()),
                tilPersonstatus(p.personstatus()),
                tilMedlemskap(p.medlemskap()),
                tilAdresser(p.adresser(), p.adressebeskyttelse()),
                p.erSkjermet()
        );
    }

    private static Rolle tilRolle(no.nav.foreldrepenger.vtp.kontrakter.person.v2.Rolle rolle) {
        if (rolle == null) {
            return null;
        }
        return Rolle.valueOf(rolle.name());
    }

    private static Navn generertTilfeldigNavn(no.nav.foreldrepenger.vtp.kontrakter.person.v2.Kjønn kjønn) {
        var generetNavn = no.nav.foreldrepenger.vtp.kontrakter.person.v2.Kjønn.M.equals(kjønn)
                ? FiktivtNavn.getRandomMaleName()
                : FiktivtNavn.getRandomFemaleName();
        return new Navn(generetNavn.fornavn(), null, generetNavn.etternavn());
    }

    private static no.nav.vtp.person.personopplysninger.Språk tilSpråk(no.nav.foreldrepenger.vtp.kontrakter.person.v2.Språk språk) {
        if (språk == null) {
            return null;
        }
        return switch (språk) {
            case NB -> no.nav.vtp.person.personopplysninger.Språk.NB;
            case NN -> no.nav.vtp.person.personopplysninger.Språk.NN;
            case EN -> no.nav.vtp.person.personopplysninger.Språk.EN;
        };
    }

    private static Kjønn tilKjønn(no.nav.foreldrepenger.vtp.kontrakter.person.v2.Kjønn kjønn) {
        if (kjønn == null) {
            return null;
        }
        return switch (kjønn) {
            case M -> Kjønn.M;
            case K -> Kjønn.K;
        };
    }

    private static GeografiskTilknytning tilGeografiskTilknytning(GeografiskTilknytningDto dto) {
        if (dto == null) {
            return null;
        }
        return new GeografiskTilknytning(Landkode.normaliser(dto.land()), tilGeografiskTilknytningType(dto.type()));
    }

    private static GeografiskTilknytning.GeografiskTilknytningType tilGeografiskTilknytningType(
            GeografiskTilknytningDto.GeografiskTilknytningType type) {
        if (type == null) {
            return null;
        }
        return GeografiskTilknytning.GeografiskTilknytningType.valueOf(type.name());
    }

    private static List<Familierelasjon> tilFamilierelasjoner(List<FamilierelasjonDto> dtos, Map<UUID, PersonIdent> identer) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(dto -> new Familierelasjon(tilRelasjon(dto.relasjon()), identer.get(dto.relatertTilId())))
                .toList();
    }

    private static Familierelasjon.Relasjon tilRelasjon(FamilierelasjonDto.Relasjon relasjon) {
        if (relasjon == null) {
            return null;
        }
        return Familierelasjon.Relasjon.valueOf(relasjon.name());
    }

    private static List<Statsborgerskap> tilStatsborgerskap(List<StatsborgerskapDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(dto -> new Statsborgerskap(Landkode.normaliser(dto.land())))
                .toList();
    }

    private static List<Sivilstand> tilSivilstander(List<SivilstandDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(PersonMapper::tilSivilstand)
                .toList();
    }

    private static Sivilstand tilSivilstand(SivilstandDto dto) {
        return new Sivilstand(tilSivilstander(dto.sivilstand()), dto.fom(), dto.tom());
    }

    private static Sivilstand.Type tilSivilstander(SivilstandDto.Sivilstander sivilstand) {
        if (sivilstand == null) {
            return null;
        }
        return switch (sivilstand) {
            case ENKE -> Sivilstand.Type.ENKE_ELLER_ENKEMANN;
            case GIFT -> Sivilstand.Type.GIFT;
            case GJPA -> Sivilstand.Type.GJENLEVENDE_PARTNER;
            case GLAD -> Sivilstand.Type.GIFT;
            case REPA -> Sivilstand.Type.REGISTRERT_PARTNER;
            case SAMB -> Sivilstand.Type.UGIFT;
            case SEPA -> Sivilstand.Type.SEPARERT_PARTNER;
            case SEPR -> Sivilstand.Type.SEPARERT;
            case SKIL -> Sivilstand.Type.SKILT;
            case SKPA -> Sivilstand.Type.SKILT_PARTNER;
            case UGIF -> Sivilstand.Type.UGIFT;
        };
    }

    private static List<Personstatus> tilPersonstatus(List<PersonstatusDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(dto -> new Personstatus(tilPersonstatuser(dto.personstatus()), dto.fom(), dto.tom()))
                .toList();
    }

    private static Personstatus.Type tilPersonstatuser(PersonstatusDto.Personstatuser status) {
        if (status == null) {
            return null;
        }
        return Personstatus.Type.valueOf(status.name());
    }

    private static List<Medlemskap> tilMedlemskap(List<MedlemskapDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(dto -> new Medlemskap(dto.fom(), dto.tom(), Landkode.normaliser(dto.land()), tilDekningsType(dto.trygdedekning())))
                .toList();
    }

    private static Medlemskap.DekningsType tilDekningsType(MedlemskapDto.DekningsType type) {
        if (type == null) {
            return null;
        }
        return Medlemskap.DekningsType.valueOf(type.name());
    }

    private static Adresser tilAdresser(List<AdresseDto> adresser, no.nav.foreldrepenger.vtp.kontrakter.person.v2.Adressebeskyttelse beskyttelse) {
        var mapped = adresser == null ? List.<Adresse>of() : adresser.stream().map(PersonMapper::tilAdresse).toList();
        return new Adresser(mapped, tilAdressebeskyttelse(beskyttelse));
    }

    private static Adresse tilAdresse(AdresseDto dto) {
        return new Adresse(
                dto.adresseType() != null ? Adresse.AdresseType.valueOf(dto.adresseType().name()) : null,
                dto.matrikkelId(),
                Landkode.normaliser(dto.land()),
                dto.fom(),
                dto.tom()
        );
    }

    private static no.nav.vtp.person.personopplysninger.Adressebeskyttelse tilAdressebeskyttelse(
            no.nav.foreldrepenger.vtp.kontrakter.person.v2.Adressebeskyttelse beskyttelse) {
        if (beskyttelse == null) {
            return null;
        }
        return switch (beskyttelse) {
            case STRENGT_FORTROLIG -> no.nav.vtp.person.personopplysninger.Adressebeskyttelse.STRENGT_FORTROLIG;
            case FORTROLIG -> no.nav.vtp.person.personopplysninger.Adressebeskyttelse.FORTROLIG;
            case UGRADERT -> no.nav.vtp.person.personopplysninger.Adressebeskyttelse.UGRADERT;
        };
    }

    private static List<Arbeidsforhold> tilArbeidsforhold(List<ArbeidsforholdDto> arbeidsforhold, Map<UUID, PersonIdent> identer) {
        if (arbeidsforhold == null) {
            return Collections.emptyList();
        }
        return arbeidsforhold.stream()
                .map(dto -> new Arbeidsforhold(
                        tilArbeidsgiver(dto.arbeidsgiver(), dto.arbeidsforholdId(), identer),
                        dto.ansettelsesperiodeFom(),
                        dto.ansettelsesperiodeTom(),
                        tilArbeidsforholdstype(dto.arbeidsforholdstype()),
                        tilArbeidsavtaler(dto.arbeidsavtaler()),
                        tilPermisjoner(dto.permisjoner())
                ))
                .toList();
    }

    private static no.nav.vtp.person.arbeidsforhold.Arbeidsgiver tilArbeidsgiver(ArbeidsgiverDto arbeidsgiver,
                                                                                  String arbeidsforholdId,
                                                                                  Map<UUID, PersonIdent> identer) {
        if (arbeidsgiver instanceof OrganisasjonDto o) {
            var detaljer = new Organisasjon.Detaljer(
                    o.detaljer() != null ? o.detaljer().navn() : null,
                    o.detaljer() != null ? o.detaljer().registreringsdato() : null
            );
            return new Organisasjon(new Orgnummer(o.orgnummer().value()), arbeidsforholdId, detaljer);
        }
        if (arbeidsgiver instanceof PrivatArbeidsgiverDto p) {
            return new PrivatArbeidsgiver(identer.get(p.uuid()));
        }
        throw new IllegalStateException("Ukjent arbeidsgiver-type: " + arbeidsgiver);
    }

    private static Arbeidsforholdstype tilArbeidsforholdstype(no.nav.foreldrepenger.vtp.kontrakter.person.v2.Arbeidsforholdstype type) {
        if (type == null) {
            return null;
        }
        return Arbeidsforholdstype.valueOf(type.name());
    }

    private static List<Arbeidsavtale> tilArbeidsavtaler(List<ArbeidsavtaleDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(dto -> new Arbeidsavtale(
                        dto.avtaltArbeidstimerPerUke(),
                        dto.stillingsprosent(),
                        dto.beregnetAntallTimerPerUke(),
                        dto.sisteLønnsendringsdato(),
                        dto.fomGyldighetsperiode(),
                        dto.tomGyldighetsperiode()
                ))
                .toList();
    }

    private static List<Permisjon> tilPermisjoner(List<PermisjonDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(dto -> new Permisjon(
                        dto.fomGyldighetsperiode(),
                        dto.tomGyldighetsperiode(),
                        dto.stillingsprosent(),
                        dto.permisjonstype() != null ? Permisjon.Permisjonstype.valueOf(dto.permisjonstype().name()) : null
                ))
                .toList();
    }

    private static List<Ytelse> tilYtelser(List<YtelseDto> ytelser) {
        if (ytelser == null) {
            return Collections.emptyList();
        }
        return ytelser.stream()
                .map(dto -> new Ytelse(
                        YtelseType.valueOf(dto.type().name()),
                        dto.fom(),
                        dto.tom(),
                        dto.dagsats(),
                        dto.utbetalingsgrad()
                ))
                .toList();
    }

    private static List<Skatteopplysning> tilSkatteopplysninger(List<SkatteopplysningDto> skatteopplysninger) {
        if (skatteopplysninger == null) {
            return Collections.emptyList();
        }
        return skatteopplysninger.stream()
                .map(dto -> new Skatteopplysning(dto.år(), dto.beløp()))
                .toList();
    }

    /**
     * Viderefører dagens mønster (Arena/Infotrygd i v1): alle ytelser genererer en Inntektsperiode,
     * unntatt uføretrygd (samme unntak som Pesys-flyten har i dag). Beløpet kommer fra testenes
     * egen dagsats — ingen hardkodede konstanter (i motsetning til v1s infotrygd()-hack med fast 10_000).
     */
    private static List<Inntektsperiode> tilInntekt(List<InntektsperiodeDto> inntekt,
                                                    List<YtelseDto> ytelser,
                                                    Map<UUID, PersonIdent> identer) {
        var eksplisitt = inntekt == null ? Stream.<Inntektsperiode>empty()
                : inntekt.stream().map(dto -> tilInntektsperiode(dto, identer));
        var avledet = ytelser == null ? Stream.<Inntektsperiode>empty()
                : ytelser.stream()
                        .map(PersonMapper::tilInntektsperiodeFraYtelse)
                        .filter(Objects::nonNull);
        return Stream.concat(eksplisitt, avledet).toList();
    }

    private static Inntektsperiode tilInntektsperiode(InntektsperiodeDto dto, Map<UUID, PersonIdent> identer) {
        return new Inntektsperiode(
                tilArbeidsgiver(dto.arbeidsgiver(), null, identer),
                dto.fom(),
                dto.tom(),
                dto.beløp(),
                dto.ytelseType() != null ? Inntektsperiode.YtelseType.valueOf(dto.ytelseType().name()) : null,
                dto.inntektFordel() != null ? Inntektsperiode.FordelType.valueOf(dto.inntektFordel().name()) : null
        );
    }

    /** Uføretrygd unntas (som i dagens Pesys-flyt), og uten dagsats kan ingen inntektsperiode avledes. */
    private static Inntektsperiode tilInntektsperiodeFraYtelse(YtelseDto dto) {
        if (dto.type() == YtelseDto.YtelseType.UFØREPENSJON || dto.dagsats() == null) {
            return null;
        }
        return new Inntektsperiode(
                NAV_YTELSE_BETALING,
                dto.fom(),
                dto.tom(),
                tilMånedligBeløp(dto.dagsats()),
                tilInntektsperiodeYtelseType(dto.type()),
                Inntektsperiode.FordelType.KONTANTYTELSE
        );
    }

    /**
     * Dagsats -> månedsbeløp basert på NAVs standard 260 virkedager per år. Inntektskomponent-mocken
     * gjenbruker samme beløp for hver måned i perioden, så beløpet må representere en jevn månedlig
     * inntekt uavhengig av periodens faktiske lengde.
     */
    private static Integer tilMånedligBeløp(Integer dagsats) {
        return Math.round(dagsats * 260f / 12);
    }

    private static Inntektsperiode.YtelseType tilInntektsperiodeYtelseType(YtelseDto.YtelseType type) {
        return switch (type) {
            case ARBEIDSAVKLARINGSPENGER -> Inntektsperiode.YtelseType.AAP;
            case DAGPENGER -> Inntektsperiode.YtelseType.DAGPENGER;
            case SYKEPENGER -> Inntektsperiode.YtelseType.SYKEPENGER;
            case PLEIEPENGER -> Inntektsperiode.YtelseType.PLEIEPENGER;
            case OMSORGSPENGER -> Inntektsperiode.YtelseType.OMSORGSPENGER;
            case OPPLÆRINGSPENGER -> Inntektsperiode.YtelseType.OPPLÆRINGSPENGER;
            case FORELDREPENGER -> Inntektsperiode.YtelseType.FORELDREPENGER;
            case SVANGERSKAPSPENGER -> Inntektsperiode.YtelseType.SVANGERSKAPSPENGER;
            case UFØREPENSJON -> throw new IllegalStateException("Uføretrygd skal være filtrert bort før dette punktet");
        };
    }
}
