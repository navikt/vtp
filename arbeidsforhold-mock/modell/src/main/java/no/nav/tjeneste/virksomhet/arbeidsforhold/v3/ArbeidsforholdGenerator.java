package no.nav.tjeneste.virksomhet.arbeidsforhold.v3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.datatype.XMLGregorianCalendar;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;

import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Organisasjon;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Person;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.NorskIdent;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Yrker;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Avloenningstyper;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Gyldighetsperiode;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.AnsettelsesPeriode;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsavtale;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.AntallTimerIPerioden;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsforholdstyper;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsforhold;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.ObjectFactory;

/**
 * Enkel førsteutgave i påvente av infrastruktur for å generere org.nr. Gir et svar med to arbeidsforhold.
 */

public class ArbeidsforholdGenerator {

    private ObjectFactory objectFactory = new ObjectFactory();

    public List<Arbeidsforhold> hentArbeidsforhold(NorskIdent ident, LocalDate fom, LocalDate tom) {
        List<Arbeidsforhold> returliste = new ArrayList<>();
        returliste.add(lagEksempelFastArbeidsforhold(ident));
        //returliste.add(lagEksempelTimeArbeidsforhold(ident));

        return returliste;
    }

    private BigDecimal lagBD(String number) {
        BigDecimal bd = new BigDecimal(number);
        return bd;
    }

    private Arbeidsavtale lagEksempelArbeidsavtale (boolean fast) {
        Arbeidsavtale arbeidsavtale = objectFactory.createArbeidsavtale();

        Yrker yrke = objectFactory.createYrker();
        Avloenningstyper loenn = objectFactory.createAvloenningstyper();
        if (fast) {
            yrke.setKodeRef("9133117");
            yrke.setValue("KJØKKENMEDHJELPER");
            loenn.setKodeRef("fast");
            loenn.setValue("Fastlønn");
            arbeidsavtale.setAvtaltArbeidstimerPerUke(lagBD("10.0"));
            arbeidsavtale.setStillingsprosent(lagBD("27"));
            arbeidsavtale.setBeregnetAntallTimerPrUke(lagBD("2.7"));
        } else {
            yrke.setKodeRef("5221126");
            yrke.setValue("BUTIKKMEDARBEIDER");
            loenn.setKodeRef("time");
            loenn.setValue("Timelønn");
            arbeidsavtale.setAvtaltArbeidstimerPerUke(lagBD("37.5"));
            arbeidsavtale.setStillingsprosent(lagBD("60"));
            arbeidsavtale.setBeregnetAntallTimerPrUke(lagBD("22"));
        }

        arbeidsavtale.setYrke(yrke);
        arbeidsavtale.setAvloenningstype(loenn);

        return arbeidsavtale;
    }

    private Arbeidsforhold lagEksempelFastArbeidsforhold (NorskIdent ident) {
        Arbeidsforhold arbeidsforhold = objectFactory.createArbeidsforhold();
        arbeidsforhold.setArbeidsforholdID("A01--Altinn");
        arbeidsforhold.setArbeidsforholdIDnav(32994858);

        Arbeidsforholdstyper aftype = objectFactory.createArbeidsforholdstyper();
        aftype.setKodeRef("ordinaertArbeidsforhold");
        aftype.setValue("Ordinært Arbeidsforhold");
        arbeidsforhold.setArbeidsforholdstype(aftype);

        Gyldighetsperiode enansperiode = objectFactory.createGyldighetsperiode();
        enansperiode.setFom(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2003, 8, 1)));
        AnsettelsesPeriode ansperiode = objectFactory.createAnsettelsesPeriode();
        ansperiode.setPeriode(enansperiode);
        arbeidsforhold.setAnsettelsesPeriode(ansperiode);

        arbeidsforhold.getArbeidsavtale().add(lagEksempelArbeidsavtale(true));

        Organisasjon arbeidsgiver = objectFactory.createOrganisasjon();
        arbeidsgiver.setOrgnummer("976037286");
        arbeidsforhold.setArbeidsgiver(arbeidsgiver);

        Organisasjon opplyser = objectFactory.createOrganisasjon();
        opplyser.setOrgnummer("976030788");
        arbeidsforhold.setOpplysningspliktig(opplyser);

        Person person = objectFactory.createPerson();
        person.setIdent(ident);
        arbeidsforhold.setArbeidstaker(person);
        arbeidsforhold.setArbeidsforholdInnrapportertEtterAOrdningen(true);

        return arbeidsforhold;
    }

    private Arbeidsforhold lagEksempelTimeArbeidsforhold (NorskIdent ident) {
        Arbeidsforhold arbeidsforhold = objectFactory.createArbeidsforhold();
        arbeidsforhold.setArbeidsforholdID("810986498192000104516");
        arbeidsforhold.setArbeidsforholdIDnav(34778270);

        Gyldighetsperiode enansperiode = objectFactory.createGyldighetsperiode();
        enansperiode.setFom(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2009, 8, 27)));
        AnsettelsesPeriode ansperiode = objectFactory.createAnsettelsesPeriode();
        ansperiode.setPeriode(enansperiode);
        arbeidsforhold.setAnsettelsesPeriode(ansperiode);

        Arbeidsforholdstyper aftype = objectFactory.createArbeidsforholdstyper();
        aftype.setKodeRef("ordinaertArbeidsforhold");
        aftype.setValue("Ordinært Arbeidsforhold");
        arbeidsforhold.setArbeidsforholdstype(aftype);

        arbeidsforhold.getAntallTimerForTimeloennet().add(lagTimePostering("140", ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2017, 5, 2))));
        arbeidsforhold.getAntallTimerForTimeloennet().add(lagTimePostering("170", ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2017, 8, 2))));

        arbeidsforhold.getArbeidsavtale().add(lagEksempelArbeidsavtale(false));

        Organisasjon arbeidsgiver = objectFactory.createOrganisasjon();
        arbeidsgiver.setOrgnummer("986507035");
        arbeidsforhold.setArbeidsgiver(arbeidsgiver);

        Organisasjon opplyser = objectFactory.createOrganisasjon();
        opplyser.setOrgnummer("986498192");
        arbeidsforhold.setOpplysningspliktig(opplyser);

        Person person = objectFactory.createPerson();
        person.setIdent(ident);
        arbeidsforhold.setArbeidstaker(person);
        arbeidsforhold.setArbeidsforholdInnrapportertEtterAOrdningen(true);

        return arbeidsforhold;
    }

    AntallTimerIPerioden lagTimePostering(String timer, XMLGregorianCalendar gperiode) {
        AntallTimerIPerioden postering = objectFactory.createAntallTimerIPerioden();
        postering.setAntallTimer(lagBD(timer));
        postering.setRapporteringsperiode(gperiode);

        return postering;
    }
}
