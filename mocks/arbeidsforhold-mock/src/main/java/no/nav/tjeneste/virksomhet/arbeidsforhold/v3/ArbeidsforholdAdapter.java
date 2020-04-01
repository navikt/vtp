package no.nav.tjeneste.virksomhet.arbeidsforhold.v3;

import java.math.BigDecimal;

import javax.xml.datatype.XMLGregorianCalendar;

import no.nav.foreldrepenger.vtp.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Aktoer;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.AnsettelsesPeriode;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.AntallTimerIPerioden;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsavtale;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsforhold;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsforholdstyper;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Gyldighetsperiode;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.NorskIdent;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.ObjectFactory;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Organisasjon;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.PermisjonOgPermittering;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.PermisjonsOgPermitteringsBeskrivelse;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Person;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Yrker;

/**
 * Enkel førsteutgave i påvente av infrastruktur for å generere org.nr. Gir et svar med to arbeidsforhold.
 */

public class ArbeidsforholdAdapter {

    private ObjectFactory objectFactory = new ObjectFactory();

    public Arbeidsforhold fra(String fnr, no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold arbeidsforholdModell){

        var arbeidsforhold = objectFactory.createArbeidsforhold();
        arbeidsforhold.setArbeidsforholdID(arbeidsforholdModell.getArbeidsforholdId());
        arbeidsforhold.setArbeidsforholdIDnav(arbeidsforholdModell.getArbeidsforholdIdnav());
        arbeidsforhold.setOpprettelsestidspunkt(ConversionUtils.convertToXMLGregorianCalendar(arbeidsforholdModell.getAnsettelsesperiodeFom()));

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

        for (no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsavtale arbeidsavtale : arbeidsforholdModell.getArbeidsavtaler()) {
            arbeidsforhold.getArbeidsavtale().add(fra(arbeidsavtale));
        }

        for (no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjon permisjon : arbeidsforholdModell.getPermisjoner()) {
            arbeidsforhold.getPermisjonOgPermittering().add(fra(permisjon));
        }

        if (arbeidsforholdModell.getArbeidsgiverAktorId() != null && !arbeidsforholdModell.getArbeidsgiverAktorId().equals("")){
            arbeidsforhold.setArbeidsgiver(lagPersonAktoer(arbeidsforholdModell.getArbeidsgiverAktorId()));
        } else {
            arbeidsforhold.setArbeidsgiver(lagOrganisasjonsAktoer(arbeidsforholdModell.getArbeidsgiverOrgnr()));
        }

        if (arbeidsforholdModell.getOpplyserOrgnr() != null && arbeidsforholdModell.getOpplyserOrgnr().length() > 9){
            arbeidsforhold.setOpplysningspliktig(lagPersonAktoer(arbeidsforholdModell.getOpplyserOrgnr()));
        } else if (arbeidsforholdModell.getArbeidsgiverOrgnr() != null) {
            arbeidsforhold.setOpplysningspliktig(lagOrganisasjonsAktoer(arbeidsforholdModell.getOpplyserOrgnr()));
        }

        arbeidsforhold.setArbeidstaker((Person)lagPersonAktoer(fnr));
        arbeidsforhold.setArbeidsforholdInnrapportertEtterAOrdningen(true);

        return arbeidsforhold;
    }

    public Arbeidsavtale fra(no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsavtale arbeidsavtaleModell){
        Arbeidsavtale arbeidsavtale = objectFactory.createArbeidsavtale();

        if(arbeidsavtaleModell.getAvtaltArbeidstimerPerUke()!= null)
            arbeidsavtale.setAvtaltArbeidstimerPerUke(BigDecimal.valueOf(arbeidsavtaleModell.getAvtaltArbeidstimerPerUke()));
        arbeidsavtale.setStillingsprosent(BigDecimal.valueOf(arbeidsavtaleModell.getStillingsprosent()));
        if(arbeidsavtaleModell.getBeregnetAntallTimerPerUke() != null)
            arbeidsavtale.setBeregnetAntallTimerPrUke(BigDecimal.valueOf(arbeidsavtaleModell.getBeregnetAntallTimerPerUke()));
        if(arbeidsavtaleModell.getSisteLønnnsendringsdato() != null)
            arbeidsavtale.setSisteLoennsendringsdato(ConversionUtils.convertToXMLGregorianCalendar(arbeidsavtaleModell.getSisteLønnnsendringsdato()));
        arbeidsavtale.setFomGyldighetsperiode(ConversionUtils.convertToXMLGregorianCalendar(arbeidsavtaleModell.getFomGyldighetsperiode()));
        if(arbeidsavtaleModell.getTomGyldighetsperiode() != null)
            arbeidsavtale.setTomGyldighetsperiode(ConversionUtils.convertToXMLGregorianCalendar(arbeidsavtaleModell.getTomGyldighetsperiode()));
        Yrker yrker = new Yrker();
        yrker.setKodeRef("SnekkerKode");
        yrker.setValue("SnekkerValue");
        arbeidsavtale.setYrke(yrker);

        return arbeidsavtale;
    }

    public PermisjonOgPermittering fra(no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjon permisjonModell){
        var permisjonOgPermittering = objectFactory.createPermisjonOgPermittering();
        var permisjonsOgPermitteringsBeskrivelse = new PermisjonsOgPermitteringsBeskrivelse();
        permisjonsOgPermitteringsBeskrivelse.setKodeRef(permisjonModell.getPermisjonstype().getKode());
        permisjonOgPermittering.setPermisjonOgPermittering(permisjonsOgPermitteringsBeskrivelse);
        permisjonOgPermittering.setPermisjonsprosent(BigDecimal.valueOf(permisjonModell.getStillingsprosent()));
        var gyldighetsperiode = new Gyldighetsperiode();
        gyldighetsperiode.setFom(ConversionUtils.convertToXMLGregorianCalendar(permisjonModell.getFomGyldighetsperiode()));
        gyldighetsperiode.setTom(ConversionUtils.convertToXMLGregorianCalendar(permisjonModell.getTomGyldighetsperiode()));
        permisjonOgPermittering.setPermisjonsPeriode(gyldighetsperiode);

        return permisjonOgPermittering;
    }

    private Aktoer lagPersonAktoer(String ident){
        Person arbeidsgiverPerson = objectFactory.createPerson();
        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent(ident);
        arbeidsgiverPerson.setIdent(norskIdent);
        return arbeidsgiverPerson;
    }

    private Aktoer lagOrganisasjonsAktoer(String orgnummer){
        Organisasjon arbeidsgiverOrganisasjon = objectFactory.createOrganisasjon();
        arbeidsgiverOrganisasjon.setOrgnummer(orgnummer);
        return arbeidsgiverOrganisasjon;
    }

}
