package no.nav.tjeneste.virksomhet.person.v3;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.GateadresseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PostboksadresseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.UstrukturertAdresseModell;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bostedsadresse;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Gateadresse;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Gyldighetsperiode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Landkoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.MidlertidigPostadresse;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.MidlertidigPostadresseNorge;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.MidlertidigPostadresseUtland;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Periode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Postadresse;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PostadressePeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Postadressetyper;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PostboksadresseNorsk;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Postnummer;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.StrukturertAdresse;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.UstrukturertAdresse;
import no.nav.tjeneste.virksomhet.person.v3.metadata.Endringstyper;

public class AdresseAdapter {
    public static final String ENDRET_AV = "fpmock2";

    public static StrukturertAdresse tilStrukturert(AdresseModell adr) {
        if ((adr instanceof GateadresseModell)) {
            return fraGateadresse((GateadresseModell) adr);
        }
        if ((adr instanceof PostboksadresseModell)) {
            return fraPostbokadresse((PostboksadresseModell) adr);
        }
        throw new UnsupportedOperationException("Har ikke implementert støtte for å konvertere fra " + adr.getClass());
    }

    public static no.nav.tjeneste.virksomhet.person.v3.informasjon.UstrukturertAdresse tilUstrukturert(AdresseModell adr) {
        if (adr instanceof UstrukturertAdresseModell) {
            return fraUstrukturertAdresse((UstrukturertAdresseModell) adr);
        }
        throw new UnsupportedOperationException("Har ikke implementert støtte for å konvertere fra " + adr.getClass());
    }

    public static PostadressePeriode tilPostadressePeriode(UstrukturertAdresseModell adr) {
        PostadressePeriode annenAdresse = new PostadressePeriode();
        annenAdresse.withEndretAv(ENDRET_AV);
        LocalDate fom = getFom(adr);
        LocalDate tom = getTom(adr);
        annenAdresse.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(fom));
        annenAdresse.withEndringstype(adr.getEndringstype() == null ? Endringstyper.NY : Endringstyper.fromValue(adr.getEndringstype()));
        annenAdresse.withPeriode(lagPeriode(fom, tom));

        Postadresse postadresse = tilPostadresse(adr);
        annenAdresse.withPostadresse(postadresse);

        return annenAdresse;
    }

    private static LocalDate getTom(UstrukturertAdresseModell adr) {
        return adr.getTom() == null ? LocalDate.of(2050, 1, 1) : adr.getTom();
    }

    private static LocalDate getFom(UstrukturertAdresseModell adr) {
        return adr.getFom() == null ? LocalDate.of(2000, 1, 1) : adr.getFom();
    }

    private static Postadresse tilPostadresse(UstrukturertAdresseModell adr) {
        LocalDate dt = getFom(adr);
        Postadresse postadresse = new Postadresse();
        postadresse.withEndretAv(ENDRET_AV);
        postadresse.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(dt));
        postadresse.withEndringstype(adr.getEndringstype() == null ? Endringstyper.NY : Endringstyper.fromValue(adr.getEndringstype()));
        postadresse.withUstrukturertAdresse(tilUstrukturert(adr));
        return postadresse;
    }

    private static no.nav.tjeneste.virksomhet.person.v3.informasjon.UstrukturertAdresse fraUstrukturertAdresse(UstrukturertAdresseModell adr) {
        UstrukturertAdresse adresse = new UstrukturertAdresse();
        Landkoder landkoder = new Landkoder();
        landkoder.setKodeverksRef("Landkoder");
        landkoder.setKodeRef(adr.getLandkode());
        landkoder.setValue(adr.getLandkode());

        adresse.withLandkode(landkoder);
        adresse.withAdresselinje1(adr.getAdresseLinje1());
        adresse.withAdresselinje2(adr.getAdresseLinje2());
        adresse.withAdresselinje3(adr.getAdresseLinje3());
        adresse.withAdresselinje4(adr.getAdresseLinje4());
        adresse.withPostnr(adr.getPostNr());
        adresse.withPoststed(adr.getPoststed());
        return adresse;
    }

    public static StrukturertAdresse fraPostbokadresse(PostboksadresseModell adr) {

        Landkoder landkoder = new Landkoder();
        landkoder.setKodeverksRef("Landkoder");
        landkoder.setKodeRef(adr.getLandkode());
        landkoder.setValue(adr.getLandkode());

        Postnummer postnummer = new Postnummer();
        postnummer.setKodeverksRef("Postnummer");
        postnummer.setKodeRef(adr.getPoststed());
        postnummer.setValue(adr.getPoststed());

        PostboksadresseNorsk post = new PostboksadresseNorsk();
        post.setLandkode(landkoder);
        post.setPostboksanlegg(adr.getPostboksanlegg());
        post.setPostboksnummer(adr.getPostboksnummer());
        post.setPoststed(postnummer);

        return post;
    }

    private static StrukturertAdresse fraGateadresse(GateadresseModell gateaddresse) {

        Landkoder landkoder = new Landkoder();
        landkoder.setKodeverksRef("Landkoder");
        landkoder.setKodeRef(gateaddresse.getLandkode());
        landkoder.setValue(gateaddresse.getLandkode());

        Gateadresse gt = new Gateadresse();

        gt.withLandkode(landkoder);
        gt.setGatenavn(gateaddresse.getGatenavn());
        gt.setGatenummer(gateaddresse.getGatenummer());
        gt.setHusbokstav(gateaddresse.getHusbokstav());
        gt.setHusnummer(gateaddresse.getHusnummer());

        Postnummer postnummer = new Postnummer();
        postnummer.setKodeverksRef("Postnummer");
        postnummer.setKodeRef(gateaddresse.getPostnummer());
        postnummer.setValue(gateaddresse.getPostnummer());
        gt.setPoststed(postnummer);

        gt.setKommunenummer(gateaddresse.getKommunenummer());
        gt.setTilleggsadresse(gateaddresse.getTilleggsadresse());
        gt.setTilleggsadresseType(gateaddresse.getTilleggsadresseType());
        return gt;
    }

    private static Periode lagPeriode(LocalDate fom, LocalDate tom) {
        Periode periode = new Periode();
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(fom));
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(tom));
        return periode;
    }

    private static Gyldighetsperiode lagGyldighetsperiode(LocalDate fom, LocalDate tom) {
        Gyldighetsperiode periode = new Gyldighetsperiode();
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(fom));
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(tom));
        return periode;
    }

    public void setAdresser(Bruker bruker, PersonModell person) {
        List<AdresseModell> adresser = person.getAdresser();
        
        // sett gjeldende til første adresse
        Postadressetyper postadressetyper = new Postadressetyper();
        postadressetyper.setValue(person.getGjeldendeadresseType());
        bruker.setGjeldendePostadressetype(postadressetyper);
        
        for (AdresseModell a : adresser) {
            switch (a.getAdresseType()) {
                case BOSTEDSADRESSE:
                    Bostedsadresse adresse = tilBostedsadresse(a);
                    bruker.setBostedsadresse(adresse);
                    break;
                case MIDLERTIDIG_POSTADRESSE:
                    MidlertidigPostadresse midlertidig = tilMidlertidigPostadresse(a);
                    bruker.setMidlertidigPostadresse(midlertidig);
                    break;
                case POSTADRESSE:
                    bruker.setPostadresse(tilPostadresse((UstrukturertAdresseModell) a));
                    break;
                default:
                    System.out.println("Ukjent adressetype: " + a);
            }
        }
    }

    private Bostedsadresse tilBostedsadresse(AdresseModell a) {
        Bostedsadresse adresse = new Bostedsadresse();
        adresse.setStrukturertAdresse(tilStrukturert(a));
        return adresse;
    }

    private MidlertidigPostadresse tilMidlertidigPostadresse(AdresseModell a) {
        MidlertidigPostadresse midlertidig;
        if ("NOR".equals(a.getLandkode())) {
            MidlertidigPostadresseNorge midl = new MidlertidigPostadresseNorge();
            midl.setEndretAv(ENDRET_AV);
            midl.setEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(a.getFom()));
            midl.setPostleveringsPeriode(lagGyldighetsperiode(a.getFom(), a.getTom()));
            midl.setStrukturertAdresse(tilStrukturert(a));
            midlertidig = midl;
        } else {
            MidlertidigPostadresseUtland midl = new MidlertidigPostadresseUtland();
            midl.setPostleveringsPeriode(lagGyldighetsperiode(a.getFom(), a.getTom()));
            midl.setEndretAv(ENDRET_AV);
            midl.setEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(a.getFom()));
            midl.setUstrukturertAdresse(tilUstrukturert(a));
            midlertidig = midl;
        }
        return midlertidig;
    }
}
