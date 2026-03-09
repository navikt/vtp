package no.nav.foreldrepenger.vtp.server.api.scenario.mapper.ny;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.vtp.kontrakter.v2.AdresseDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Adressebeskyttelse;
import no.nav.foreldrepenger.vtp.kontrakter.v2.ArbeidsavtaleDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.ArbeidsforholdDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Arbeidsgiver;
import no.nav.foreldrepenger.vtp.kontrakter.v2.ArenaDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.ArenaMeldekort;
import no.nav.foreldrepenger.vtp.kontrakter.v2.ArenaSakerDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.ArenaVedtakDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.FamilierelasjonModellDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.GeografiskTilknytningDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.GrunnlagDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.InfotrygdDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.InntektYtelseType;
import no.nav.foreldrepenger.vtp.kontrakter.v2.InntektsperiodeDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.MedlemskapDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.OrganisasjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PermisjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Permisjonstype;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PersonstatusDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.SivilstandDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Språk;
import no.nav.foreldrepenger.vtp.kontrakter.v2.StatsborgerskapDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.TilordnetIdentDto;
import no.nav.foreldrepenger.vtp.testmodell.identer.FiktiveFnr;
import no.nav.foreldrepenger.vtp.testmodell.identer.IdentGenerator;
import no.nav.foreldrepenger.vtp.testmodell.util.FiktivtNavn;
import no.nav.vtp.Person;
import no.nav.vtp.arbeidsforhold.Arbeidsavtale;
import no.nav.vtp.arbeidsforhold.Arbeidsforhold;
import no.nav.vtp.arbeidsforhold.Arbeidsforholdstype;
import no.nav.vtp.arbeidsforhold.Organisasjon;
import no.nav.vtp.arbeidsforhold.Permisjon;
import no.nav.vtp.arbeidsforhold.PrivatArbeidsgiver;
import no.nav.vtp.ident.Identifikator;
import no.nav.vtp.ident.Orgnummer;
import no.nav.vtp.ident.PersonIdent;
import no.nav.vtp.inntekt.Inntektsperiode;
import no.nav.vtp.personopplysninger.Adresse;
import no.nav.vtp.personopplysninger.Adresser;
import no.nav.vtp.personopplysninger.Familierelasjon;
import no.nav.vtp.personopplysninger.GeografiskTilknytning;
import no.nav.vtp.personopplysninger.Kjønn;
import no.nav.vtp.personopplysninger.Medlemskap;
import no.nav.vtp.personopplysninger.Navn;
import no.nav.vtp.personopplysninger.Personopplysninger;
import no.nav.vtp.personopplysninger.Personstatus;
import no.nav.vtp.personopplysninger.Sivilstand;
import no.nav.vtp.personopplysninger.Statsborgerskap;
import no.nav.vtp.ytelse.Ytelse;
import no.nav.vtp.ytelse.YtelseType;

import org.jspecify.annotations.NonNull;

public class PersonMapper {

    private static final IdentGenerator identGenerator = new FiktiveFnr();

    private PersonMapper() {
        /* This utility class should not be instantiated */
    }

    public static Person tilPerson(PersonDto p, Set<TilordnetIdentDto> nyidenter) {
        var personopplysninger = tilPersonopplysninger(p, nyidenter);
        var arbeidsforhold = tilArbeidsforhold(p, nyidenter);
        var inntekt = tilInntekt(p, nyidenter);
        var ytelser = tilYtelser(p);
        return new Person(personopplysninger, arbeidsforhold, inntekt, ytelser);
    }

    private static Personopplysninger tilPersonopplysninger(PersonDto p, Set<TilordnetIdentDto> nyidenter) {
        var indent = finnIdent(p.id(), nyidenter);
        return new Personopplysninger(
                indent, // TODO: Barn kan bli fort gamle her. Skal vi heller generer det i forkant? Eget kall?
                generertTilfeldigNavn(p.kjønn()),
                p.fødselsdato(),
                p.dødsdato(),
                tilSpråk(p.språk()),
                tilKjønn(p.kjønn()),
                tilGeografiskTilknytning(p.geografiskTilknytning()),
                tilFamilierelasjoner(p.familierelasjoner(), nyidenter),
                tilStatsborgerskap(p.statsborgerskap()),
                tilSivilstand(p.sivilstand()),
                tilPersonstatus(p.personstatus()),
                tilMedlemskap(p.medlemskap()),
                tilAdresser(p.adresser(), p.adressebeskyttelse()),
                p.erSkjermet()
        );
    }

    private static PersonIdent finnIdent(UUID uuid, Set<TilordnetIdentDto> nyidenter) {
        var fnr = nyidenter.stream()
                .filter(tilordnetIdentDto -> tilordnetIdentDto.id().equals(uuid))
                .findFirst()
                .orElseThrow()
                .fnr();
        return new PersonIdent(fnr);
    }

    private static Navn generertTilfeldigNavn(no.nav.foreldrepenger.vtp.kontrakter.v2.Kjønn kjønn) {
        var generetNavn = no.nav.foreldrepenger.vtp.kontrakter.v2.Kjønn.M.equals(kjønn) ? FiktivtNavn.getRandomMaleName() : FiktivtNavn.getRandomFemaleName();
        return new Navn(generetNavn.getFornavn(), null, generetNavn.getEtternavn());
    }

    private static List<Arbeidsforhold> tilArbeidsforhold(PersonDto p, Set<TilordnetIdentDto> nyidenter) {
        if (p.inntektytelse() == null || p.inntektytelse().aareg() == null) {
            return Collections.emptyList();
        }

        return Optional.ofNullable(p.inntektytelse().aareg().arbeidsforhold())
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> tilArbeidsforhold(dto, nyidenter))
                .toList();
    }

    private static Arbeidsforhold tilArbeidsforhold(ArbeidsforholdDto dto, Set<TilordnetIdentDto> nyidenter) {
        return new Arbeidsforhold(
                tilArbeidsgiver(dto.arbeidsgiver(), dto.arbeidsforholdId(), nyidenter),
                dto.ansettelsesperiodeFom(),
                dto.ansettelsesperiodeTom(),
                tilArbeidsforholdstype(dto.arbeidsforholdstype()),
                tilArbeidsavtaler(dto.arbeidsavtaler()),
                tilPermisjoner(dto.permisjoner())
        );
    }

    private static no.nav.vtp.arbeidsforhold.Arbeidsgiver tilArbeidsgiver(Arbeidsgiver arbeidsgiver, String arbeidsforholdId, Set<TilordnetIdentDto> nyidenter) {
        if (arbeidsgiver instanceof OrganisasjonDto(no.nav.foreldrepenger.vtp.kontrakter.v2.Orgnummer orgnummer, OrganisasjonDto.OrganisasjonsdetaljerDto organisasjonsdetaljer)) {
            var detaljer = new Organisasjon.Detaljer(organisasjonsdetaljer.navn(), organisasjonsdetaljer.registreringsdato());
            return new Organisasjon(new Orgnummer(orgnummer.value()), arbeidsforholdId, detaljer);
        }
        var ident = finnIdent(((no.nav.foreldrepenger.vtp.kontrakter.v2.PrivatArbeidsgiver) arbeidsgiver).uuid(), nyidenter);
        return new PrivatArbeidsgiver(ident);
    }

    private static List<Inntektsperiode> tilInntekt(PersonDto p, Set<TilordnetIdentDto> nyidenter) {
        if (p.inntektytelse() == null || p.inntektytelse().inntektskomponent() == null) {
            return Collections.emptyList();
        }

        return Optional.ofNullable(p.inntektytelse().inntektskomponent().inntektsperioder())
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> tilInntektsperiode(dto, nyidenter))
                .toList();
    }

    private static Inntektsperiode tilInntektsperiode(InntektsperiodeDto dto, Set<TilordnetIdentDto> nyidenter) {
        return new Inntektsperiode(
                tilArbeidsgiver(dto.arbeidsgiver(), null, nyidenter),
                dto.fom(),
                dto.tom(),
                dto.beløp(),
                tilInntektType(dto.inntektType()),
                tilYtelseType(dto.inntektYtelseType()),
                tilInntektFordel(dto.inntektFordel())
        );
    }

    private static List<Ytelse> tilYtelser(PersonDto p) {
        if (p.inntektytelse() == null) {
            return Collections.emptyList();
        }

        var ytelser = new ArrayList<Ytelse>();

        // Map Arena ytelser
        if (p.inntektytelse().arena() != null && p.inntektytelse().arena().saker() != null) {
            ytelser.addAll(tilYtelserFraArena(p.inntektytelse().arena()));
        }

        // Map Infotrygd ytelser
        if (p.inntektytelse().infotrygd() != null && p.inntektytelse().infotrygd().ytelser() != null) {
            ytelser.addAll(tilYtelserFraInfotrygd(p.inntektytelse().infotrygd()));
        }

        // Map Pesys ytelser
        if (p.inntektytelse().pesys() != null && Boolean.TRUE.equals(p.inntektytelse().pesys().harUføretrygd())) {
            ytelser.add(tilYtelserFraPesys());
        }

        return ytelser;
    }

    private static List<Ytelse> tilYtelserFraArena(ArenaDto arena) {
        return arena.saker().stream()
                .map(sak -> tilYtelseFraArenaVedtak(sak.tema(), sak.vedtak().getFirst()))
                .toList();
    }

    private static Ytelse tilYtelseFraArenaVedtak(ArenaSakerDto.YtelseTema tema,
                                                  ArenaVedtakDto vedtak) {
        var ytelseType = tilYtelseTypeFraArenaTema(tema);

        // Calculate total utbetalt from meldekort
        Integer totalUtbetalt = null;
        if (vedtak.meldekort() != null && !vedtak.meldekort().isEmpty()) {
            totalUtbetalt = vedtak.meldekort().stream()
                    .map(ArenaMeldekort::beløp)
                    .filter(Objects::nonNull)
                    .reduce(0, Integer::sum);
        }

        return new Ytelse(
                ytelseType,
                vedtak.fom(),
                vedtak.tom(),
                vedtak.dagsats(),
                totalUtbetalt,
                Collections.emptyList() // Arena doesn't have beregningsgrunnlag in the DTO
        );
    }

    private static YtelseType tilYtelseTypeFraArenaTema(ArenaSakerDto.YtelseTema tema) {
        return switch (tema) {
            case AAP -> YtelseType.ARBEIDSAVKLARINGSPENGER;
            case DAG -> YtelseType.DAGPENGER;
            default -> throw new IllegalStateException("Ikke støttet ytelse i arena!");
        };
    }

    private static List<Ytelse> tilYtelserFraInfotrygd(InfotrygdDto infotrygd) {
        return infotrygd.ytelser().stream()
                .map(PersonMapper::tilYtelseFraInfotrygdGrunnlag)
                .collect(Collectors.toList());
    }

    private static Ytelse tilYtelseFraInfotrygdGrunnlag(GrunnlagDto grunnlag) {
        var ytelseType = tilYtelseTypeFraInfotrygdYtelse(grunnlag.ytelse());

        // Calculate total utbetalingsgrad from vedtak
        Integer totalUtbetalingsgrad = null;
        if (grunnlag.vedtak() != null && !grunnlag.vedtak().isEmpty()) {
            var avgUtbetalingsgrad = grunnlag.vedtak().stream()
                    .map(GrunnlagDto.Vedtak::utbetalingsgrad)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .average();
            totalUtbetalingsgrad = avgUtbetalingsgrad.isPresent() ? (int) avgUtbetalingsgrad.getAsDouble() : null;
        }

        return new Ytelse(
                ytelseType,
                grunnlag.fom(),
                grunnlag.tom(),
                null, // Todo: Har vi en dagsats?
                totalUtbetalingsgrad,
                Collections.emptyList() // TODO: TREX til denne?
        );
    }

    private static YtelseType tilYtelseTypeFraInfotrygdYtelse(GrunnlagDto.Ytelse ytelse) {
        return switch (ytelse) {
            case SP -> YtelseType.SYKEPENGER;
            default -> throw new IllegalStateException("Ikke støttet ytelse!");
        };
    }

    private static Ytelse tilYtelserFraPesys() {
        return new Ytelse(YtelseType.UFØREPENSJON, LocalDate.now().minusYears(5), null, null, null, null);
    }

    private static no.nav.vtp.personopplysninger.Språk tilSpråk(Språk språk) {
        if (språk == null) {
            return null;
        }
        return switch (språk) {
            case NB -> no.nav.vtp.personopplysninger.Språk.NB;
            case NN -> no.nav.vtp.personopplysninger.Språk.NN;
            case EN -> no.nav.vtp.personopplysninger.Språk.EN;
        };
    }

    private static Kjønn tilKjønn(no.nav.foreldrepenger.vtp.kontrakter.v2.Kjønn kjønn) {
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
        return new GeografiskTilknytning(
                dto.land(),
                tilGeografiskTilknytningType(dto.type())
        );
    }

    private static GeografiskTilknytning.GeografiskTilknytningType tilGeografiskTilknytningType(
            GeografiskTilknytningDto.GeografiskTilknytningType type) {
        if (type == null) {
            return null;
        }
        return switch (type) {
            case BYDEL -> GeografiskTilknytning.GeografiskTilknytningType.BYDEL;
            case KOMMUNE -> GeografiskTilknytning.GeografiskTilknytningType.KOMMUNE;
            case LAND -> GeografiskTilknytning.GeografiskTilknytningType.LAND;
        };
    }

    private static List<Familierelasjon> tilFamilierelasjoner(List<FamilierelasjonModellDto> dtos, Set<TilordnetIdentDto> identer) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(dto -> tilFamilierelasjon(dto, identer))
                .collect(Collectors.toList());
    }

    private static Familierelasjon tilFamilierelasjon(FamilierelasjonModellDto dto, Set<TilordnetIdentDto> identer) {
        return new Familierelasjon(tilRelasjon(dto.relasjon()), finnIdent(dto.relatertTilId(), identer));
    }

    private static Familierelasjon.Relasjon tilRelasjon(FamilierelasjonModellDto.Relasjon relasjon) {
        if (relasjon == null) {
            return null;
        }
        return switch (relasjon) {
            case EKTE -> Familierelasjon.Relasjon.EKTE;
            case SAMBOER -> Familierelasjon.Relasjon.SAMBOER;
            case BARN -> Familierelasjon.Relasjon.BARN;
            case FAR -> Familierelasjon.Relasjon.FAR;
            case MOR -> Familierelasjon.Relasjon.MOR;
            case MEDMOR -> Familierelasjon.Relasjon.MEDMOR;
        };
    }

    private static List<Statsborgerskap> tilStatsborgerskap(List<StatsborgerskapDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(dto -> new Statsborgerskap(dto.land()))
                .collect(Collectors.toList());
    }

    private static List<Sivilstand> tilSivilstand(List<SivilstandDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(PersonMapper::tilSivilstand)
                .collect(Collectors.toList());
    }

    private static Sivilstand tilSivilstand(SivilstandDto dto) {
        return new Sivilstand(
                tilSivilstander(dto.sivilstand()),
                dto.fom(),
                dto.tom()
        );
    }

    private static Sivilstand.Sivilstander tilSivilstander(SivilstandDto.Sivilstander sivilstand) {
        if (sivilstand == null) {
            return null;
        }
        return switch (sivilstand) {
            case ENKE -> Sivilstand.Sivilstander.ENKE_ELLER_ENKEMANN;
            case GIFT -> Sivilstand.Sivilstander.GIFT;
            case GJPA -> Sivilstand.Sivilstander.GJENLEVENDE_PARTNER;
            case GLAD -> Sivilstand.Sivilstander.GIFT;  // GLAD (Giftet, lever adskilt) maps to GIFT
            case REPA -> Sivilstand.Sivilstander.REGISTRERT_PARTNER;
            case SAMB -> Sivilstand.Sivilstander.UGIFT;  // SAMB is not directly mapped, use UGIFT
            case SEPA -> Sivilstand.Sivilstander.SEPARERT_PARTNER;
            case SEPR -> Sivilstand.Sivilstander.SEPARERT;
            case SKIL -> Sivilstand.Sivilstander.SKILT;
            case SKPA -> Sivilstand.Sivilstander.SKILT_PARTNER;
            case UGIF -> Sivilstand.Sivilstander.UGIFT;
        };
    }

    private static List<Personstatus> tilPersonstatus(List<PersonstatusDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(PersonMapper::tilPersonstatus)
                .collect(Collectors.toList());
    }

    private static Personstatus tilPersonstatus(PersonstatusDto dto) {
        return new Personstatus(
                tilPersonstatuser(dto.personstatus()),
                dto.fom(),
                dto.tom()
        );
    }

    private static Personstatus.Personstatuser tilPersonstatuser(PersonstatusDto.Personstatuser status) {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case ABNR -> Personstatus.Personstatuser.ABNR;
            case ADNR -> Personstatus.Personstatuser.ADNR;
            case BOSA -> Personstatus.Personstatuser.BOSA;
            case DØD -> Personstatus.Personstatuser.DØD;
            case FOSV -> Personstatus.Personstatuser.FOSV;
            case FØDR -> Personstatus.Personstatuser.FØDR;
            case UFUL -> Personstatus.Personstatuser.UFUL;
            case UREG -> Personstatus.Personstatuser.UREG;
            case UTAN -> Personstatus.Personstatuser.UTAN;
            case UTPE -> Personstatus.Personstatuser.UTPE;
            case UTVA -> Personstatus.Personstatuser.UTVA;
        };
    }

    private static List<Medlemskap> tilMedlemskap(List<MedlemskapDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(PersonMapper::tilMedlemskap)
                .collect(Collectors.toList());
    }

    private static Medlemskap tilMedlemskap(MedlemskapDto dto) {
        return new Medlemskap(
                dto.fom(),
                dto.tom(),
                dto.land(),
                tilDekningsType(dto.trygdedekning())
        );
    }

    private static Medlemskap.DekningsType tilDekningsType(MedlemskapDto.DekningsType type) {
        if (type == null) {
            return null;
        }
        return switch (type) {
            case IHT_AVTALE -> Medlemskap.DekningsType.IHT_AVTALE;
            case FULL -> Medlemskap.DekningsType.FULL;
        };
    }

    private static Adresser tilAdresser(List<AdresseDto> adresser, Adressebeskyttelse adressebeskyttelse) {
        return new Adresser(tilAdresser(adresser), tilAdressebeskyttelse(adressebeskyttelse));
    }

    private static List<Adresse> tilAdresser(List<AdresseDto> adresser) {
        return adresser.stream()
                .map(PersonMapper::tilAdresse)
                .toList();
    }

    private static Adresse tilAdresse(AdresseDto adresseDto) {
        return new Adresse(
                tilAdresseType(adresseDto.adresseType()),
                adresseDto.land(),
                adresseDto.fom(),
                adresseDto.tom()
        );
    }

    private static Adresse.AdresseType tilAdresseType(AdresseDto.AdresseType adresseType) {
        return switch (adresseType) {
            case BOSTEDSADRESSE -> Adresse.AdresseType.BOSTEDSADRESSE;
            case POSTADRESSE -> Adresse.AdresseType.POSTADRESSE;
        };
    }

    private static no.nav.vtp.personopplysninger.Adressebeskyttelse tilAdressebeskyttelse(
            Adressebeskyttelse beskyttelse) {
        if (beskyttelse == null) {
            return null;
        }
        return switch (beskyttelse) {
            case STRENGT_FORTROLIG -> no.nav.vtp.personopplysninger.Adressebeskyttelse.STRENGT_FORTROLIG;
            case FORTROLIG -> no.nav.vtp.personopplysninger.Adressebeskyttelse.FORTROLIG;
            case UGRADERT -> no.nav.vtp.personopplysninger.Adressebeskyttelse.UGRADERT;
        };
    }

    private static Identifikator tilIdentifikator(Arbeidsgiver arbeidsgiver) {
        if (arbeidsgiver == null) {
            return null;
        }
        if (arbeidsgiver instanceof OrganisasjonDto org) {
            return new Orgnummer(org.orgnummer().value());
        }
        // TODO: Handle PrivatArbeidsgiver
        return null;
    }

    private static Arbeidsforholdstype tilArbeidsforholdstype(no.nav.foreldrepenger.vtp.kontrakter.v2.Arbeidsforholdstype type) {
        if (type == null) {
            return null;
        }
        return switch (type) {
            case ORDINÆRT_ARBEIDSFORHOLD -> Arbeidsforholdstype.ORDINÆRT_ARBEIDSFORHOLD;
            case MARITIMT_ARBEIDSFORHOLD -> Arbeidsforholdstype.MARITIMT_ARBEIDSFORHOLD;
            case FRILANSER_OPPDRAGSTAKER_MED_MER -> Arbeidsforholdstype.FRILANSER_OPPDRAGSTAKER_MED_MER;
            case FORENKLET_OPPGJØRSORDNING -> Arbeidsforholdstype.FORENKLET_OPPGJØRSORDNING;
        };
    }

    private static List<Arbeidsavtale> tilArbeidsavtaler(List<ArbeidsavtaleDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(PersonMapper::tilArbeidsavtale)
                .collect(Collectors.toList());
    }

    private static Arbeidsavtale tilArbeidsavtale(ArbeidsavtaleDto dto) {
        return new Arbeidsavtale(
                dto.avtaltArbeidstimerPerUke(),
                dto.stillingsprosent(),
                dto.beregnetAntallTimerPerUke(),
                dto.sisteLønnsendringsdato(),
                dto.fomGyldighetsperiode(),
                dto.tomGyldighetsperiode()
        );
    }

    private static List<Permisjon> tilPermisjoner(List<PermisjonDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(PersonMapper::tilPermisjon)
                .collect(Collectors.toList());
    }

    private static Permisjon tilPermisjon(PermisjonDto dto) {
        return new Permisjon(
                dto.fomGyldighetsperiode(),
                dto.tomGyldighetsperiode(),
                dto.stillingsprosent(),
                tilPermisjonstype(dto.permisjonstype())
        );
    }

    private static Permisjon.Permisjonstype tilPermisjonstype(Permisjonstype type) {
        if (type == null) {
            return null;
        }
        return switch (type) {
            case PERMISJON -> Permisjon.Permisjonstype.PERMISJON;
            case PERMISJON_MED_FORELDREPENGER -> Permisjon.Permisjonstype.PERMISJON_MED_FORELDREPENGER;
            case PERMISJON_VED_MILITÆRTJENESTE -> Permisjon.Permisjonstype.PERMISJON_VED_MILITÆRTJENESTE;
            case PERMITTERING -> Permisjon.Permisjonstype.PERMITTERING;
            case UTDANNINGSPERMISJON -> Permisjon.Permisjonstype.UTDANNINGSPERMISJON;
            case UTDANNINGSPERMISJON_IKKE_LOVFESTET -> Permisjon.Permisjonstype.UTDANNINGSPERMISJON_IKKE_LOVFESTET;
            case UTDANNINGSPERMISJON_LOVFESTET -> Permisjon.Permisjonstype.UTDANNINGSPERMISJON_LOVFESTET;
            case VELFERDSPERMISJON -> Permisjon.Permisjonstype.VELFERDSPERMISJON;
            case ANNEN_PERMISJON_IKKE_LOVFESTET -> Permisjon.Permisjonstype.ANNEN_PERMISJON_IKKE_LOVFESTET;
            case ANNEN_PERMISJON_LOVFESTET -> Permisjon.Permisjonstype.ANNEN_PERMISJON_LOVFESTET;
        };
    }

    private static Inntektsperiode.Type tilInntektType(InntektsperiodeDto.InntektTypeDto type) {
        if (type == null) {
            return null;
        }
        return switch (type) {
            case LØNNSINNTEKT -> Inntektsperiode.Type.LØNNSINNTEKT;
            case NÆRINGSINNTEKT -> Inntektsperiode.Type.NÆRINGSINNTEKT;
            case PENSJON_ELLER_TRYGD -> Inntektsperiode.Type.PENSJON_ELLER_TRYGD;
            case YTELSE_FRA_OFFENTLIGE -> Inntektsperiode.Type.YTELSE_FRA_OFFENTLIGE;
        };
    }

    private static Inntektsperiode.YtelseType tilYtelseType(InntektYtelseType type) {
        if (type == null) {
            return null;
        }
        // Map from DTO enum to domain enum - they should have matching values
        return Inntektsperiode.YtelseType.valueOf(type.name());
    }

    private static Inntektsperiode.FordelType tilInntektFordel(InntektsperiodeDto.InntektFordelDto fordel) {
        if (fordel == null) {
            return null;
        }
        return switch (fordel) {
            case KONTANTYTELSE -> Inntektsperiode.FordelType.KONTANTYTELSE;
            case UTGIFTSGODTGJØRELSE -> Inntektsperiode.FordelType.UTGIFTSGODTGJØRELSE;
            case NATURALYTELSE -> Inntektsperiode.FordelType.NATURALYTELSE;
        };
    }
}
