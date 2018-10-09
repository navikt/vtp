package no.nav.tjeneste.virksomhet.arbeidsforhold.v3;

import java.math.BigDecimal;

import javax.xml.datatype.XMLGregorianCalendar;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.AnsettelsesPeriode;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.AntallTimerIPerioden;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsavtale;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsforhold;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsforholdstyper;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Gyldighetsperiode;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.NorskIdent;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.ObjectFactory;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Organisasjon;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Person;

/**
 * Enkel førsteutgave i påvente av infrastruktur for å generere org.nr. Gir et svar med to arbeidsforhold.
 */

public class ArbeidsforholdAdapter {

    private ObjectFactory objectFactory = new ObjectFactory();

    public Arbeidsforhold fra(String fnr, no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold arbeidsforholdModell){

        Arbeidsforhold arbeidsforhold = objectFactory.createArbeidsforhold();
        arbeidsforhold.setArbeidsforholdID(arbeidsforholdModell.getArbeidsforholdId());
        arbeidsforhold.setArbeidsforholdIDnav(arbeidsforholdModell.getArbeidsforholdIdnav());

        Arbeidsforholdstyper aftype = objectFactory.createArbeidsforholdstyper();
        aftype.setKodeRef(arbeidsforholdModell.getArbeidsforholdstype().getKode());
        arbeidsforhold.setArbeidsforholdstype(aftype);

        Gyldighetsperiode enansperiode = objectFactory.createGyldighetsperiode();
        enansperiode.setFom(ConversionUtils.convertToXMLGregorianCalendar(arbeidsforholdModell.getAnsettelsesperiodeFom()));
        if(arbeidsforholdModell.getAnsettelsesperiodeTom() != null){
            enansperiode.setTom(ConversionUtils.convertToXMLGregorianCalendar(arbeidsforholdModell.getAnsettelsesperiodeTom()));
        }
        AnsettelsesPeriode ansperiode = objectFactory.createAnsettelsesPeriode();
        ansperiode.setPeriode(enansperiode);
        arbeidsforhold.setAnsettelsesPeriode(ansperiode);

        for (no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsavtale arbeidsavtale : arbeidsforholdModell.getArbeidsavtaler()) {
            arbeidsforhold.getArbeidsavtale().add(fra(arbeidsavtale));
        }

        Organisasjon arbeidsgiver = objectFactory.createOrganisasjon();
        arbeidsgiver.setOrgnummer(arbeidsforholdModell.getArbeidsgiverOrgnr());
        arbeidsforhold.setArbeidsgiver(arbeidsgiver);

        Organisasjon opplyser = objectFactory.createOrganisasjon();
        opplyser.setOrgnummer(arbeidsforholdModell.getOpplyserOrgnr());
        arbeidsforhold.setOpplysningspliktig(opplyser);

        Person person = objectFactory.createPerson();
        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent(fnr);
        person.setIdent(norskIdent);
        arbeidsforhold.setArbeidstaker(person);
        arbeidsforhold.setArbeidsforholdInnrapportertEtterAOrdningen(true);

        return arbeidsforhold;
    }

    public Arbeidsavtale fra(no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsavtale arbeidsavtaleModell){
        Arbeidsavtale arbeidsavtale = objectFactory.createArbeidsavtale();

        arbeidsavtale.setAvtaltArbeidstimerPerUke(lagBD(arbeidsavtaleModell.getAvtaltArbeidstimerPerUke()));
        arbeidsavtale.setStillingsprosent(lagBD(arbeidsavtaleModell.getStillingsprosent()));
        arbeidsavtale.setBeregnetAntallTimerPrUke(lagBD(arbeidsavtaleModell.getBeregnetAntallTimerPerUke()));
        arbeidsavtale.setSisteLoennsendringsdato(ConversionUtils.convertToXMLGregorianCalendar(arbeidsavtaleModell.getSisteLønnnsendringsdato()));
        arbeidsavtale.setFomGyldighetsperiode(ConversionUtils.convertToXMLGregorianCalendar(arbeidsavtaleModell.getFomGyldighetsperiode()));


        return arbeidsavtale;
    }



    private BigDecimal lagBD(String number) {
        BigDecimal bd = new BigDecimal(number);
        return bd;
    }

    private BigDecimal lagBD(Integer number) {
        BigDecimal bd = new BigDecimal(number);
        return bd;
    }

    AntallTimerIPerioden lagTimePostering(String timer, XMLGregorianCalendar gperiode) {
        AntallTimerIPerioden postering = objectFactory.createAntallTimerIPerioden();
        postering.setAntallTimer(lagBD(timer));
        postering.setRapporteringsperiode(gperiode);

        return postering;
    }
}
