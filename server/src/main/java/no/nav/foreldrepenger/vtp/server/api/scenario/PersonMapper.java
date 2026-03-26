package no.nav.foreldrepenger.vtp.server.api.scenario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.vtp.kontrakter.person.AdresseDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.Adressebeskyttelse;
import no.nav.foreldrepenger.vtp.kontrakter.person.ArbeidsavtaleDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.ArbeidsforholdDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.Arbeidsgiver;
import no.nav.foreldrepenger.vtp.kontrakter.person.ArenaDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.ArenaMeldekort;
import no.nav.foreldrepenger.vtp.kontrakter.person.ArenaSakerDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.ArenaVedtakDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.FamilierelasjonModellDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.GeografiskTilknytningDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.GrunnlagDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.InfotrygdDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.InntektYtelseType;
import no.nav.foreldrepenger.vtp.kontrakter.person.InntektsperiodeDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.MedlemskapDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.OrganisasjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.PermisjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.Permisjonstype;
import no.nav.foreldrepenger.vtp.kontrakter.person.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.PersonstatusDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.SigrunDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.SivilstandDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.Språk;
import no.nav.foreldrepenger.vtp.kontrakter.person.StatsborgerskapDto;
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

    private PersonMapper() {
        /* This utility class should not be instantiated */
    }

    public static Person tilPerson(PersonDto p, Map<UUID, PersonIdent> identer) {
        var personopplysninger = tilPersonopplysninger(p, identer);
        var arbeidsforhold = tilArbeidsforhold(p, identer);
        var inntekt = tilInntekt(p, identer);
        var ytelser = tilYtelser(p);
        var skatteopplysninger = tilSkatteopplysnigner(p);
        return new Person(personopplysninger, arbeidsforhold, inntekt, ytelser, skatteopplysninger);
    }

    private static List<Skatteopplysning> tilSkatteopplysnigner(PersonDto person) {
        if (person.inntektytelse() == null || person.inntektytelse().sigrun() == null || person.inntektytelse().sigrun().inntektår() == null) {
            return List.of();
        }
        return person.inntektytelse().sigrun().inntektår().stream()
                .map(PersonMapper::tilSkatteopplysning)
                .toList();
    }

    private static Skatteopplysning tilSkatteopplysning(SigrunDto.InntektsårDto inntektsår) {
        return new Skatteopplysning(inntektsår.år(), inntektsår.beløp());
    }

    private static Personopplysninger tilPersonopplysninger(PersonDto p, Map<UUID, PersonIdent> identer) {
        return new Personopplysninger(
                identer.get(p.uuid()),
                p.uuid(),
                tilRolle(p.rolle()),
                generertTilfeldigNavn(p.kjønn()),
                p.fødselsdato(),
                p.dødsdato(),
                tilSpråk(p.språk()),
                tilKjønn(p.kjønn()),
                tilGeografiskTilknytning(p.geografiskTilknytning()),
                tilFamilierelasjoner(p.familierelasjoner(), identer),
                tilStatsborgerskap(p.statsborgerskap()),
                tilSivilstand(p.sivilstand()),
                tilPersonstatus(p.personstatus()),
                tilMedlemskap(p.medlemskap()),
                tilAdresser(p.adresser(), p.adressebeskyttelse()),
                p.erSkjermet()
        );
    }

    private static Rolle tilRolle(no.nav.foreldrepenger.vtp.kontrakter.person.Rolle rolle) {
        return switch (rolle) {
            case MOR -> Rolle.MOR;
            case FAR -> Rolle.FAR;
            case MEDMOR -> Rolle.MEDMOR;
            case MEDFAR -> Rolle.MEDFAR;
            case BARN -> Rolle.BARN;
            case PRIVATE_ARBEIDSGIVER -> Rolle.PRIVATE_ARBEIDSGIVER;
        };
    }

    private static Navn generertTilfeldigNavn(no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn kjønn) {
        var generetNavn = no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn.M.equals(kjønn) ? FiktivtNavn.getRandomMaleName() : FiktivtNavn.getRandomFemaleName();
        return new Navn(generetNavn.getFornavn(), null, generetNavn.getEtternavn());
    }

    private static List<Arbeidsforhold> tilArbeidsforhold(PersonDto p, Map<UUID, PersonIdent> identer) {
        if (p.inntektytelse() == null || p.inntektytelse().aareg() == null) {
            return Collections.emptyList();
        }

        return Optional.ofNullable(p.inntektytelse().aareg().arbeidsforhold())
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> tilArbeidsforhold(dto, identer))
                .toList();
    }

    private static Arbeidsforhold tilArbeidsforhold(ArbeidsforholdDto dto, Map<UUID, PersonIdent> identer) {
        return new Arbeidsforhold(
                tilArbeidsgiver(dto.arbeidsgiver(), dto.arbeidsforholdId(), identer),
                dto.ansettelsesperiodeFom(),
                dto.ansettelsesperiodeTom(),
                tilArbeidsforholdstype(dto.arbeidsforholdstype()),
                tilArbeidsavtaler(dto.arbeidsavtaler()),
                tilPermisjoner(dto.permisjoner())
        );
    }

    private static no.nav.vtp.person.arbeidsforhold.Arbeidsgiver tilArbeidsgiver(Arbeidsgiver arbeidsgiver, String arbeidsforholdId,
                                                                                 Map<UUID, PersonIdent> identer) {
        if (arbeidsgiver instanceof OrganisasjonDto(no.nav.foreldrepenger.vtp.kontrakter.person.Orgnummer orgnummer, OrganisasjonDto.OrganisasjonsdetaljerDto organisasjonsdetaljer)) {
            var detaljer = new Organisasjon.Detaljer(organisasjonsdetaljer.navn(), organisasjonsdetaljer.registreringsdato());
            return new Organisasjon(new Orgnummer(orgnummer.value()), arbeidsforholdId, detaljer);
        }
        var uuid = ((no.nav.foreldrepenger.vtp.kontrakter.person.PrivatArbeidsgiver) arbeidsgiver).uuid();
        return new PrivatArbeidsgiver(identer.get(uuid));
    }

    private static List<Inntektsperiode> tilInntekt(PersonDto p, Map<UUID, PersonIdent> identer) {
        if (p.inntektytelse() == null || p.inntektytelse().inntektskomponent() == null) {
            return Collections.emptyList();
        }

        return Optional.ofNullable(p.inntektytelse().inntektskomponent().inntektsperioder())
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> tilInntektsperiode(dto, identer))
                .toList();
    }

    private static Inntektsperiode tilInntektsperiode(InntektsperiodeDto dto, Map<UUID, PersonIdent> identer) {
        return new Inntektsperiode(
                tilArbeidsgiver(dto.arbeidsgiver(), null, identer),
                dto.fom(),
                dto.tom(),
                dto.beløp(),
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

    private static no.nav.vtp.person.personopplysninger.Språk tilSpråk(Språk språk) {
        if (språk == null) {
            return null;
        }
        return switch (språk) {
            case NB -> no.nav.vtp.person.personopplysninger.Språk.NB;
            case NN -> no.nav.vtp.person.personopplysninger.Språk.NN;
            case EN -> no.nav.vtp.person.personopplysninger.Språk.EN;
        };
    }

    private static Kjønn tilKjønn(no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn kjønn) {
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

    private static List<Familierelasjon> tilFamilierelasjoner(List<FamilierelasjonModellDto> dtos, Map<UUID, PersonIdent> identer) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(dto -> tilFamilierelasjon(dto, identer))
                .toList();
    }

    private static Familierelasjon tilFamilierelasjon(FamilierelasjonModellDto dto, Map<UUID, PersonIdent> identer) {
        return new Familierelasjon(tilRelasjon(dto.relasjon()), identer.get(dto.relatertTilId()));
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

    private static Sivilstand.Type tilSivilstander(SivilstandDto.Sivilstander sivilstand) {
        if (sivilstand == null) {
            return null;
        }
        return switch (sivilstand) {
            case ENKE -> Sivilstand.Type.ENKE_ELLER_ENKEMANN;
            case GIFT -> Sivilstand.Type.GIFT;
            case GJPA -> Sivilstand.Type.GJENLEVENDE_PARTNER;
            case GLAD -> Sivilstand.Type.GIFT;  // GLAD (Giftet, lever adskilt) maps to GIFT
            case REPA -> Sivilstand.Type.REGISTRERT_PARTNER;
            case SAMB -> Sivilstand.Type.UGIFT;  // SAMB is not directly mapped, use UGIFT
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

    private static Personstatus.Type tilPersonstatuser(PersonstatusDto.Personstatuser status) {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case ABNR -> Personstatus.Type.ABNR;
            case ADNR -> Personstatus.Type.ADNR;
            case BOSA -> Personstatus.Type.BOSA;
            case DØD -> Personstatus.Type.DØD;
            case FOSV -> Personstatus.Type.FOSV;
            case FØDR -> Personstatus.Type.FØDR;
            case UFUL -> Personstatus.Type.UFUL;
            case UREG -> Personstatus.Type.UREG;
            case UTAN -> Personstatus.Type.UTAN;
            case UTPE -> Personstatus.Type.UTPE;
            case UTVA -> Personstatus.Type.UTVA;
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
                adresseDto.matrikkelId(),
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

    private static no.nav.vtp.person.personopplysninger.Adressebeskyttelse tilAdressebeskyttelse(
            Adressebeskyttelse beskyttelse) {
        if (beskyttelse == null) {
            return null;
        }
        return switch (beskyttelse) {
            case STRENGT_FORTROLIG -> no.nav.vtp.person.personopplysninger.Adressebeskyttelse.STRENGT_FORTROLIG;
            case FORTROLIG -> no.nav.vtp.person.personopplysninger.Adressebeskyttelse.FORTROLIG;
            case UGRADERT -> no.nav.vtp.person.personopplysninger.Adressebeskyttelse.UGRADERT;
        };
    }

    private static Arbeidsforholdstype tilArbeidsforholdstype(no.nav.foreldrepenger.vtp.kontrakter.person.Arbeidsforholdstype type) {
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
