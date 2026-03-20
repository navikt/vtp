package no.nav.foreldrepenger.vtp.server.api.scenario;

import java.util.List;

import no.nav.foreldrepenger.vtp.kontrakter.person.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold.ArbeidsavtaleDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold.ArbeidsforholdDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold.ArbeidsgiverDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold.OrganisasjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold.PermisjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold.PrivatArbeidsgiverDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.inntekt.InntektsperiodeDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.AdresseDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.AdresserDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.FamilierelasjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.GeografiskTilknytningDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.MedlemskapDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.PersonopplysningerDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.PersonstatusDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.SivilstandDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.StatsborgerskapDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.skatt.SkatteopplysningDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.ytelse.YtelseDto;
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
import no.nav.vtp.person.ytelse.Beregningsgrunnlag;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

public class PersonMapper {

    private PersonMapper() {
    }

    public static Person tilPerson(PersonDto dto) {
        return new Person(
                tilPersonopplysninger(dto.personopplysninger()),
                tilArbeidsforhold(dto.arbeidsforhold()),
                tilInntekt(dto.inntekt()),
                tilYtelser(dto.ytelser()),
                tilSkatteopplysninger(dto.skatteopplysninger())
        );
    }

    // -- Personopplysninger --

    private static Personopplysninger tilPersonopplysninger(PersonopplysningerDto p) {
        return new Personopplysninger(
                new PersonIdent(p.fnr()),
                tilRolle(p.rolle()),
                generertTilfeldigNavn(p.kjønn()),
                p.fødselsdato(),
                p.dødsdato(),
                tilSpråk(p.språk()),
                tilKjønn(p.kjønn()),
                tilGeografiskTilknytning(p.geografiskTilknytning()),
                tilFamilierelasjoner(p.familierelasjoner()),
                tilStatsborgerskap(p.statsborgerskap()),
                tilSivilstand(p.sivilstand()),
                tilPersonstatus(p.personstatus()),
                tilMedlemskap(p.medlemskap()),
                tilAdresser(p.adresser()),
                p.erSkjermet()
        );
    }

    private static Navn generertTilfeldigNavn(no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.Kjønn kjønn) {
        var generetNavn = no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.Kjønn.M.equals(kjønn) ? FiktivtNavn.getRandomMaleName() : FiktivtNavn.getRandomFemaleName();
        return new Navn(generetNavn.getFornavn(), null, generetNavn.getEtternavn());
    }

    private static Rolle tilRolle(no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.Rolle rolle) {
        if (rolle == null) return Rolle.MOR;
        return switch (rolle) {
            case MOR -> Rolle.MOR;
            case FAR -> Rolle.FAR;
            case MEDMOR -> Rolle.MEDMOR;
            case MEDFAR -> Rolle.MEDFAR;
            case BARN -> Rolle.BARN;
            case PRIVATE_ARBEIDSGIVER -> Rolle.PRIVATE_ARBEIDSGIVER;
        };
    }

    private static no.nav.vtp.person.personopplysninger.Språk tilSpråk(no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.Språk språk) {
        if (språk == null) return null;
        return switch (språk) {
            case NB -> no.nav.vtp.person.personopplysninger.Språk.NB;
            case NN -> no.nav.vtp.person.personopplysninger.Språk.NN;
            case EN -> no.nav.vtp.person.personopplysninger.Språk.EN;
        };
    }

    private static Kjønn tilKjønn(no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.Kjønn kjønn) {
        if (kjønn == null) return null;
        return switch (kjønn) {
            case M -> Kjønn.M;
            case K -> Kjønn.K;
        };
    }

    private static GeografiskTilknytning tilGeografiskTilknytning(GeografiskTilknytningDto dto) {
        if (dto == null) return null;
        return new GeografiskTilknytning(dto.land(), tilGeoType(dto.type()));
    }

    private static GeografiskTilknytning.GeografiskTilknytningType tilGeoType(GeografiskTilknytningDto.GeografiskTilknytningType type) {
        if (type == null) return null;
        return switch (type) {
            case BYDEL -> GeografiskTilknytning.GeografiskTilknytningType.BYDEL;
            case KOMMUNE -> GeografiskTilknytning.GeografiskTilknytningType.KOMMUNE;
            case LAND -> GeografiskTilknytning.GeografiskTilknytningType.LAND;
        };
    }

    private static List<Familierelasjon> tilFamilierelasjoner(List<FamilierelasjonDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(PersonMapper::tilFamilierelasjon).toList();
    }

    private static Familierelasjon tilFamilierelasjon(FamilierelasjonDto dto) {
        return new Familierelasjon(tilRelasjon(dto.relasjon()), new PersonIdent(dto.relatertTilId()));
    }

    private static Familierelasjon.Relasjon tilRelasjon(FamilierelasjonDto.Relasjon relasjon) {
        if (relasjon == null) return null;
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
        if (dtos == null) return List.of();
        return dtos.stream().map(dto -> new Statsborgerskap(dto.land())).toList();
    }

    private static List<Sivilstand> tilSivilstand(List<SivilstandDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(PersonMapper::tilSivilstand).toList();
    }

    private static Sivilstand tilSivilstand(SivilstandDto dto) {
        return new Sivilstand(tilSivilstandType(dto.sivilstand()), dto.fom(), dto.tom());
    }

    private static Sivilstand.Type tilSivilstandType(SivilstandDto.Type type) {
        if (type == null) return null;
        return Sivilstand.Type.valueOf(type.name());
    }

    private static List<Personstatus> tilPersonstatus(List<PersonstatusDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(PersonMapper::tilPersonstatus).toList();
    }

    private static Personstatus tilPersonstatus(PersonstatusDto dto) {
        return new Personstatus(tilPersonstatusType(dto.personstatus()), dto.fom(), dto.tom());
    }

    private static Personstatus.Type tilPersonstatusType(PersonstatusDto.Type type) {
        if (type == null) return null;
        return Personstatus.Type.valueOf(type.name());
    }

    private static List<Medlemskap> tilMedlemskap(List<MedlemskapDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(PersonMapper::tilMedlemskap).toList();
    }

    private static Medlemskap tilMedlemskap(MedlemskapDto dto) {
        return new Medlemskap(dto.fom(), dto.tom(), dto.land(), tilDekningsType(dto.trygdedekning()));
    }

    private static Medlemskap.DekningsType tilDekningsType(MedlemskapDto.DekningsType type) {
        if (type == null) return null;
        return switch (type) {
            case IHT_AVTALE -> Medlemskap.DekningsType.IHT_AVTALE;
            case FULL -> Medlemskap.DekningsType.FULL;
        };
    }

    private static Adresser tilAdresser(AdresserDto adresser) {
        if (adresser == null) return new Adresser(List.of(), null);
        return new Adresser(
                tilAdresseListe(adresser.adresser()),
                tilAdressebeskyttelse(adresser.adressebeskyttelse())
        );
    }

    private static List<Adresse> tilAdresseListe(List<AdresseDto> adresser) {
        if (adresser == null) return List.of();
        return adresser.stream().map(PersonMapper::tilAdresse).toList();
    }

    private static Adresse tilAdresse(AdresseDto dto) {
        return new Adresse(tilAdresseType(dto.adresseType()), dto.matrikkelId(), dto.land(), dto.fom(), dto.tom());
    }

    private static Adresse.AdresseType tilAdresseType(AdresseDto.AdresseType type) {
        return switch (type) {
            case BOSTEDSADRESSE -> Adresse.AdresseType.BOSTEDSADRESSE;
            case POSTADRESSE -> Adresse.AdresseType.POSTADRESSE;
        };
    }

    private static no.nav.vtp.person.personopplysninger.Adressebeskyttelse tilAdressebeskyttelse(
            no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger.Adressebeskyttelse beskyttelse) {
        if (beskyttelse == null) return null;
        return switch (beskyttelse) {
            case FORTROLIG -> no.nav.vtp.person.personopplysninger.Adressebeskyttelse.FORTROLIG;
            case STRENGT_FORTROLIG -> no.nav.vtp.person.personopplysninger.Adressebeskyttelse.STRENGT_FORTROLIG;
            case UGRADERT -> no.nav.vtp.person.personopplysninger.Adressebeskyttelse.UGRADERT;
        };
    }

    // -- Arbeidsforhold --

    private static List<Arbeidsforhold> tilArbeidsforhold(List<ArbeidsforholdDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(PersonMapper::tilArbeidsforhold).toList();
    }

    private static Arbeidsforhold tilArbeidsforhold(ArbeidsforholdDto dto) {
        return new Arbeidsforhold(
                tilArbeidsgiver(dto.arbeidsgiver(), dto.arbeidsforholdId()),
                dto.ansettelsesperiodeFom(),
                dto.ansettelsesperiodeTom(),
                tilArbeidsforholdstype(dto.arbeidsforholdstype()),
                tilArbeidsavtaler(dto.arbeidsavtaler()),
                tilPermisjoner(dto.permisjoner())
        );
    }

    private static no.nav.vtp.person.arbeidsforhold.Arbeidsgiver tilArbeidsgiver(ArbeidsgiverDto arbeidsgiver, String arbeidsforholdId) {
        if (arbeidsgiver instanceof OrganisasjonDto(String orgnummer, OrganisasjonDto.OrganisasjonsdetaljerDto organisasjonsdetaljer)) {
            var detaljer = new Organisasjon.Detaljer(organisasjonsdetaljer.navn(), organisasjonsdetaljer.registreringsdato());
            return new Organisasjon(new Orgnummer(orgnummer), arbeidsforholdId, detaljer);
        }
        var ident = new PersonIdent(((PrivatArbeidsgiverDto) arbeidsgiver).fnr());
        return new PrivatArbeidsgiver(ident);
    }

    private static Arbeidsforholdstype tilArbeidsforholdstype(no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold.Arbeidsforholdstype type) {
        if (type == null) return null;
        return Arbeidsforholdstype.valueOf(type.name());
    }

    private static List<Arbeidsavtale> tilArbeidsavtaler(List<ArbeidsavtaleDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(PersonMapper::tilArbeidsavtale).toList();
    }

    private static Arbeidsavtale tilArbeidsavtale(ArbeidsavtaleDto dto) {
        return new Arbeidsavtale(dto.avtaltArbeidstimerPerUke(), dto.stillingsprosent(), dto.beregnetAntallTimerPerUke(),
                dto.sisteLønnsendringsdato(), dto.fomGyldighetsperiode(), dto.tomGyldighetsperiode());
    }

    private static List<Permisjon> tilPermisjoner(List<PermisjonDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(PersonMapper::tilPermisjon).toList();
    }

    private static Permisjon tilPermisjon(PermisjonDto dto) {
        return new Permisjon(dto.fom(), dto.tom(), dto.stillingsprosent(),
                dto.permisjonstype() == null ? null : Permisjon.Permisjonstype.valueOf(dto.permisjonstype().name()));
    }

    // -- Inntekt --

    private static List<Inntektsperiode> tilInntekt(List<InntektsperiodeDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(PersonMapper::tilInntektsperiode).toList();
    }

    private static Inntektsperiode tilInntektsperiode(InntektsperiodeDto dto) {
        return new Inntektsperiode(
                tilArbeidsgiver(dto.arbeidsgiver(), null),
                dto.fom(), dto.tom(), dto.beløp(),
                dto.ytelseType() == null ? null : Inntektsperiode.YtelseType.valueOf(dto.ytelseType().name()),
                dto.inntektFordel() == null ? null : Inntektsperiode.FordelType.valueOf(dto.inntektFordel().name())
        );
    }

    // -- Ytelser --

    private static List<Ytelse> tilYtelser(List<YtelseDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(PersonMapper::tilYtelse).toList();
    }

    private static Ytelse tilYtelse(YtelseDto dto) {
        return new Ytelse(
                dto.ytelse() == null ? null : YtelseType.valueOf(dto.ytelse().name()),
                dto.fra(), dto.til(), dto.dagsats(), dto.utbetalt(),
                tilBeregningsgrunnlag(dto.beregningsgrunnlag())
        );
    }

    private static List<Beregningsgrunnlag> tilBeregningsgrunnlag(List<no.nav.foreldrepenger.vtp.kontrakter.person.ytelse.Beregningsgrunnlag> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> new Beregningsgrunnlag(Beregningsgrunnlag.Kategori.valueOf(dto.kategori().name()), dto.beløp()))
                .toList();
    }

    // -- Skatteopplysninger --

    private static List<Skatteopplysning> tilSkatteopplysninger(List<SkatteopplysningDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(dto -> new Skatteopplysning(dto.år(), dto.beløp())).toList();
    }
}
