package no.nav.pdl.oversetter;

import static no.nav.pdl.oversetter.PersonAdapter.DATO_FORMATTERER;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.GateadresseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Landkode;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PostboksadresseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.UstrukturertAdresseModell;
import no.nav.pdl.Bostedsadresse;
import no.nav.pdl.Kontaktadresse;
import no.nav.pdl.KontaktadresseType;
import no.nav.pdl.Matrikkeladresse;
import no.nav.pdl.Metadata;
import no.nav.pdl.Person;
import no.nav.pdl.PostadresseIFrittFormat;
import no.nav.pdl.Postboksadresse;
import no.nav.pdl.UtenlandskAdresseIFrittFormat;
import no.nav.pdl.Vegadresse;

public class AdresseAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(AdresseAdapter.class);

    private AdresseAdapter() {
    }

    private static Date getTom(GateadresseModell adr) {
        return adr.getTom() == null ? toDate(LocalDate.of(2050, 1, 1)) : toDate(adr.getTom());
    }

    private static Date getFom(GateadresseModell adr) {
        return adr.getFom() == null ?  toDate(LocalDate.of(2000, 1, 1)) : toDate(adr.getFom());
    }

    private static Kontaktadresse tilPostadresse(UstrukturertAdresseModell adr) {
        if (adr.getLand() != Landkode.NOR) {
            return new Kontaktadresse.Builder()
                    .setUtenlandskAdresseIFrittFormat(UtenlandskAdresseIFrittFormat.builder()
                            .setAdresselinje1(adr.getAdresseLinje1())
                            .setAdresselinje2(adr.getAdresseLinje2())
                            .setAdresselinje3(adr.getAdresseLinje3())
                            .setLandkode(adr.getLandkode())
                            .build())
                    .setType(KontaktadresseType.Utland)
                    .build();
        } else {
            return new Kontaktadresse.Builder()
                    .setPostadresseIFrittFormat(PostadresseIFrittFormat.builder()
                            .setAdresselinje1(adr.getAdresseLinje1())
                            .setAdresselinje2(adr.getAdresseLinje2())
                            .setAdresselinje3(adr.getAdresseLinje3())
                            .setPostnummer(adr.getPostNr())
                            .build())
                    .setType(KontaktadresseType.Innland)
                    .build();
        }
    }

    public static Postboksadresse fraPostbokadresse(PostboksadresseModell adr) {
        var adresse = new Postboksadresse();
        adresse.setPostboks(adr.getPostboksanlegg());
        adresse.setPostnummer(adr.getPostboksnummer());
        //adresse.setPostbokseier(bla);

        return adresse;
    }


    private static Bostedsadresse fraGateadresse(GateadresseModell gateaddresse) {
        Bostedsadresse adresse = new Bostedsadresse();
        // TODO: Finne ut fornuftige datoer. Har ikke vært i bruk tidligere
        // TODO: Brukes "tilleggsadresser" mer?
        // TODO: Skal gateaddresse.getLandkode() nå plasseres i Kontaktadresse?
        adresse.setAngittFlyttedato(LocalDate.now().format(DATO_FORMATTERER));
        adresse.setGyldigFraOgMed(getFom(gateaddresse));
        adresse.setGyldigTilOgMed(getTom(gateaddresse));
        adresse.setCoAdressenavn(null);
        adresse.setVegadresse(Vegadresse.builder()
                .setAdressenavn(gateaddresse.getGatenavn())
                .setHusbokstav(gateaddresse.getHusbokstav())
                .setHusnummer(Integer.toString(gateaddresse.getHusnummer()))
                .setPostnummer(gateaddresse.getPostnummer())
                .setMatrikkelId(gateaddresse.getMatrikkelId())
                //.setPoststed() - ikke i bruk lenger?
                .build());
        adresse.setMatrikkeladresse(Matrikkeladresse.builder()
                .setKommunenummer(gateaddresse.getKommunenummer())
                .setPostnummer(gateaddresse.getPostnummer())
                .setMatrikkelId(gateaddresse.getMatrikkelId())
                .build());
        //adresse.setUtenlandskAdresse(bla);
        //adresse.setUkjentBosted(bla);
        //adresse.setFolkeregistermetadata(bla);
        adresse.setMetadata(Metadata.builder()
                .setMaster("PDL")
                .setHistorisk(false)
                .build());

        return adresse;
    }

    public static Person setAdresser(Person pers, List<AdresseModell> adresser) {
        for (AdresseModell a : adresser) {
            switch (a.getAdresseType()) {
                case BOSTEDSADRESSE:
                    if (a instanceof GateadresseModell gateadresseModell) {
                        var bostedsadresse = fraGateadresse(gateadresseModell);
                        pers.setBostedsadresse(List.of(bostedsadresse));
                        break;
                    }
                    if (a instanceof PostboksadresseModell postboksadresseModell) {
                        var postboksadresse = fraPostbokadresse(postboksadresseModell);
                        var kontaktadresse = new Kontaktadresse.Builder().setPostboksadresse(postboksadresse).build();
                        pers.setKontaktadresse(List.of(kontaktadresse));
                        break;
                    }
                    LOG.info("Ukjent adressetype: {}", a);
                    break;
                case MIDLERTIDIG_POSTADRESSE:
                    // TODO: Hvordan skal denne oversettes med PDL?
                    // MidlertidigPostadresse midlertidig = tilMidlertidigPostadresse(a);
                    // bruker.setMidlertidigPostadresse(midlertidig);
                    break;
                case POSTADRESSE:
                    // TODO: Hvordan skal denne oversettes med PDL?
                    var kontaktadresse = tilPostadresse((UstrukturertAdresseModell) a);
                    pers.setKontaktadresse(List.of(kontaktadresse));
                    break;
                default:
                    LOG.info("Ukjent adressetype: {}", a);
            }
        }
        setNonnullableAdresserSomIkkeErSatt(pers);
        return pers;
    }

    private static Date toDate(LocalDate dateTime) {
        return Date.from(dateTime.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private static void setNonnullableAdresserSomIkkeErSatt(Person pers) {
        if (pers.getBostedsadresse() == null) {
            pers.setBostedsadresse(List.of());
        }
        if (pers.getOppholdsadresse() == null) {
            pers.setOppholdsadresse(List.of());
        }
        if (pers.getKontaktadresse() == null) {
            pers.setKontaktadresse(List.of());
        }
        if (pers.getDeltBosted() == null) {
            pers.setDeltBosted(List.of());
        }

    }
}
