package no.nav.foreldrepenger.vtp.server.api.scenario.mapper;

import static no.nav.foreldrepenger.fpwsproxy.UtilKlasse.safeStream;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.vtp.kontrakter.v2.AdresseDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.FamilierelasjonModellDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.GeografiskTilknytningDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Kjønn;
import no.nav.foreldrepenger.vtp.kontrakter.v2.MedlemskapDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PersonstatusDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Rolle;
import no.nav.foreldrepenger.vtp.kontrakter.v2.SivilstandDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.StatsborgerskapDto;
import no.nav.foreldrepenger.vtp.testmodell.medlemskap.DekningType;
import no.nav.foreldrepenger.vtp.testmodell.medlemskap.LovvalgType;
import no.nav.foreldrepenger.vtp.testmodell.medlemskap.MedlemskapKildeType;
import no.nav.foreldrepenger.vtp.testmodell.medlemskap.MedlemskapModell;
import no.nav.foreldrepenger.vtp.testmodell.medlemskap.MedlemskapperiodeModell;
import no.nav.foreldrepenger.vtp.testmodell.medlemskap.PeriodeStatus;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseType;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AnnenPartModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.GateadresseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.GeografiskTilknytningModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Landkode;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonstatusModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SivilstandModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.StatsborgerskapModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.UstrukturertAdresseModell;

public class PersonopplysningModellMapper {

    private PersonopplysningModellMapper() {
        // Bare statiske metoder
    }

    public static Personopplysninger tilPersonOpplysninger(PersonDto søker, List<PersonDto> barnene, Map<UUID, BrukerModell> allePersoner) {
        return new Personopplysninger(
                (SøkerModell) allePersoner.get(søker.id()),
                null,
                tilRelasjoner(søker.familierelasjoner(), allePersoner),
                null,
                !barnene.isEmpty() ? tilRelasjoner(barnene.get(0).familierelasjoner(), allePersoner) : null
        );
    }

    public static Personopplysninger tilPersonOpplysninger(PersonDto søker, List<PersonDto> barnene, Map<UUID, BrukerModell> allePersoner, PersonDto annenpart) {
        return new Personopplysninger(
                (SøkerModell) allePersoner.get(søker.id()),
                (AnnenPartModell) allePersoner.get(annenpart.id()),
                tilRelasjoner(søker.familierelasjoner(), allePersoner),
                tilRelasjoner(annenpart.familierelasjoner(), allePersoner),
                !barnene.isEmpty() ? tilRelasjoner(barnene.get(0).familierelasjoner(), allePersoner) : null
        );
    }

    public static SøkerModell tilSøker(PersonDto s) {
        var søkerModell = new SøkerModell();
        // Felles
        setPerson(søkerModell, s);
        return søkerModell;
    }

    public static AnnenPartModell tilAnnenpart(PersonDto a) {
        var annenpartModell = new AnnenPartModell();
        annenpartModell.setRolle(tilRolle(a.rolle()));
        setPerson(annenpartModell, a);
        return annenpartModell;
    }

    public static BarnModell tilBarn(PersonDto b) {
        var barnModell = new BarnModell();
        barnModell.setFornavn("Ole");
        barnModell.setEtternavn("Duck");
        setPerson(barnModell, b);
        return barnModell;
    }

    public static PersonArbeidsgiver tilPersonArbeidsgiver(PersonDto p) {
        var personArbeidsgiver = new PersonArbeidsgiver();
        setPerson(personArbeidsgiver, p);
        return personArbeidsgiver;
    }

    private static void setPerson(PersonModell personModell, PersonDto person) {
        personModell.setId(person.id());
        personModell.setFødselsdato(person.fødselsdato());
        personModell.setDødsdato(person.dødsdato());
        personModell.setSpråk(person.språk());
        personModell.setKjønn(tilKjønn(person.kjønn()));
        personModell.setGeografiskTilknytning(tilGeografiskTilknytningModell(person.geografiskTilknytning()));
        personModell.setStatsborgerskap(tilStatsborgerskap(person.statsborgerskap()));
        personModell.setSivilstand(tilSivilstand(person.sivilstand()));
        personModell.setPersonstatus(tilPersonstatus(person.personstatus()));
        personModell.setAdresser(tilAdresse(person.adresser()));
        personModell.setMedlemskap(tilMedlemskap(person.medlemskap()));
    }

    private static List<FamilierelasjonModell> tilRelasjoner(List<FamilierelasjonModellDto> familierelasjoner, Map<UUID, BrukerModell> allePersoner) {
        return safeStream(familierelasjoner)
                .map(f -> tilFamilierelasjonModell(allePersoner, f))
                .collect(Collectors.toList()); // //NOSONAR Unmodifiable list
    }

    private static FamilierelasjonModell tilFamilierelasjonModell(Map<UUID, BrukerModell> brukere, FamilierelasjonModellDto familierelasjonModellDto) {
        var relatertTil = brukere.get(familierelasjonModellDto.relatertTilId());
        return new FamilierelasjonModell(tilRolle(familierelasjonModellDto.relasjon()), relatertTil);
    }

    private static FamilierelasjonModell.Rolle tilRolle(FamilierelasjonModellDto.Relasjon relasjon) {
        return switch (relasjon) {
            case EKTE -> FamilierelasjonModell.Rolle.EKTE;
            case SAMBOER -> FamilierelasjonModell.Rolle.SAMB;
            case BARN -> FamilierelasjonModell.Rolle.BARN;
            case FAR -> FamilierelasjonModell.Rolle.FARA;
            case MOR -> FamilierelasjonModell.Rolle.MORA;
            case MEDMOR -> FamilierelasjonModell.Rolle.MMOR;
        };
    }

    private static FamilierelasjonModell.Rolle tilRolle(Rolle rolle) {
        return switch (rolle) {
            case MOR -> FamilierelasjonModell.Rolle.MORA;
            case FAR -> FamilierelasjonModell.Rolle.FARA;
            case MEDMOR -> FamilierelasjonModell.Rolle.MMOR;
            case MEDFAR -> FamilierelasjonModell.Rolle.FARA;
            case BARN -> FamilierelasjonModell.Rolle.BARN;
            case PRIVATE_ARBEIDSGIVER -> throw new IllegalStateException("Utviklerfeil!");
        };
    }

    private static MedlemskapModell tilMedlemskap(List<MedlemskapDto> medlemskap) {
        if (medlemskap == null) return null;
        var medlemskapModell = new MedlemskapModell();
        for (var m : medlemskap) {
            var medlemskapperiodeModell = new MedlemskapperiodeModell(
                    null,
                    m.fom(),
                    m.tom(),
                    m.fom(),
                    new Landkode(m.land().getAlpha3()),
                    tilDekningsType(m.trygdedekning()),
                    MedlemskapKildeType.ANNEN,
                    LovvalgType.ENDL,
                    PeriodeStatus.GYLD
            );
            medlemskapModell.leggTil(medlemskapperiodeModell);
        }
        return medlemskapModell;
    }

    private static DekningType tilDekningsType(MedlemskapDto.DekningsType trygdedekning) {
        return switch (trygdedekning) {
            case IHT_AVTALE -> DekningType.IHT_AVTALE;
            case FULL -> DekningType.FULL;
        };
    }

    private static List<AdresseModell> tilAdresse(List<AdresseDto> adresser) {
        return safeStream(adresser)
                .map(PersonopplysningModellMapper::tilAdresse)
                .toList();
    }

    private static AdresseModell tilAdresse(AdresseDto a) {
        var type = switch (a.adresseType()) {
            case BOSTEDSADRESSE -> AdresseType.BOSTEDSADRESSE;
            case POSTADRESSE -> AdresseType.POSTADRESSE;
            case MIDLERTIDIG_POSTADRESSE -> AdresseType.MIDLERTIDIG_POSTADRESSE;
            case UKJENT_ADRESSE -> AdresseType.UKJENT_ADRESSE;
        };
        if (a.land().equals(CountryCode.NO)) {
            return norskGateAdresse(a, type);
        } else {
            return utenlandskUstrukturertAdresse(a, type);
        }
    }

    private static UstrukturertAdresseModell utenlandskUstrukturertAdresse(AdresseDto a, AdresseType type) {
        return new UstrukturertAdresseModell(a.fom(), a.tom(), null, null, type,
                new Landkode(a.land().getAlpha3()), // TODO: Støtter bare 4 land..
                null, "President Avenue", "Central district", "Utlandet", null, null, null);
    }

    private static GateadresseModell norskGateAdresse(AdresseDto a, AdresseType type) {
        return new GateadresseModell(a.fom(), a.tom(), null, null, type, Landkode.NOR, a.matrikkelId(),
                "Haugesund ally", 15, "B", 10, "5511", null);
    }

    private static List<PersonstatusModell> tilPersonstatus(List<PersonstatusDto> personstatus) {
        return safeStream(personstatus)
                .map(PersonopplysningModellMapper::tilPersonstatus)
                .toList();
    }

    private static PersonstatusModell tilPersonstatus(PersonstatusDto p) {
        var personstatus = switch (p.personstatus()) {
            case ABNR -> PersonstatusModell.Personstatuser.ABNR;
            case ADNR -> PersonstatusModell.Personstatuser.ADNR;
            case BOSA -> PersonstatusModell.Personstatuser.BOSA;
            case DØD -> PersonstatusModell.Personstatuser.DØD;
            case FOSV -> PersonstatusModell.Personstatuser.FOSV;
            case FØDR -> PersonstatusModell.Personstatuser.FØDR;
            case UFUL -> PersonstatusModell.Personstatuser.UFUL;
            case UREG -> PersonstatusModell.Personstatuser.UREG;
            case UTAN -> PersonstatusModell.Personstatuser.UTAN;
            case UTPE -> PersonstatusModell.Personstatuser.UTPE;
            case UTVA -> PersonstatusModell.Personstatuser.UTVA;
        };
        return new PersonstatusModell(personstatus, p.fom(), p.tom());
    }

    private static List<SivilstandModell> tilSivilstand(List<SivilstandDto> sivilstand) {
        return safeStream(sivilstand)
                .map(PersonopplysningModellMapper::tilSivilstand)
                .toList();
    }

    private static SivilstandModell tilSivilstand(SivilstandDto sivilstandDto) {
        var siviltilstand = switch (sivilstandDto.sivilstand()) {
            case ENKE -> SivilstandModell.Sivilstander.ENKE;
            case GIFT -> SivilstandModell.Sivilstander.GIFT;
            case GJPA -> SivilstandModell.Sivilstander.GJPA;
            case GLAD -> SivilstandModell.Sivilstander.GLAD;
            case REPA -> SivilstandModell.Sivilstander.REPA;
            case SAMB -> SivilstandModell.Sivilstander.SAMB;
            case SEPA -> SivilstandModell.Sivilstander.SEPA;
            case SEPR -> SivilstandModell.Sivilstander.SEPR;
            case SKIL -> SivilstandModell.Sivilstander.SKIL;
            case SKPA -> SivilstandModell.Sivilstander.SKPA;
            case UGIF -> SivilstandModell.Sivilstander.UGIF;
        };
        return new SivilstandModell(siviltilstand, sivilstandDto.fom(), sivilstandDto.tom());
    }

    private static List<StatsborgerskapModell> tilStatsborgerskap(List<StatsborgerskapDto> statsborgerskap) {
        return safeStream(statsborgerskap)
                .map(PersonopplysningModellMapper::tilStatsborgerskap)
                .toList();
    }

    private static StatsborgerskapModell tilStatsborgerskap(StatsborgerskapDto statsborgerskap) {
        return new StatsborgerskapModell(new Landkode(statsborgerskap.land().getAlpha3())); // TODO: Støtter bare 4 land..
    }

    private static GeografiskTilknytningModell tilGeografiskTilknytningModell(GeografiskTilknytningDto g) {
        if (g == null) {
            return null;
        }
        var geografiskTilknytningmodell = new GeografiskTilknytningModell();
        geografiskTilknytningmodell.setKode(g.land().getAlpha3());
        geografiskTilknytningmodell.setType(tilGeografiskTilknytningType(g.type()));
        return geografiskTilknytningmodell;
    }

    private static GeografiskTilknytningModell.GeografiskTilknytningType tilGeografiskTilknytningType(GeografiskTilknytningDto.GeografiskTilknytningType type) {
        return switch (type) {
            case BYDEL -> GeografiskTilknytningModell.GeografiskTilknytningType.Bydel;
            case KOMMUNE -> GeografiskTilknytningModell.GeografiskTilknytningType.Kommune;
            case LAND -> GeografiskTilknytningModell.GeografiskTilknytningType.Land;
        };
    }

    private static BrukerModell.Kjønn tilKjønn(Kjønn kjønn) {
        if (kjønn == null) {
            return null;
        }
        return switch (kjønn) {
            case M -> BrukerModell.Kjønn.M;
            case K -> BrukerModell.Kjønn.K;
        };
    }

}
