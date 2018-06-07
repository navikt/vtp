package no.nav.tjeneste.virksomhet.person.v3;

import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.ENDRET_AV;
import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.TIDENES_MORGEN;
import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.lagBostedadresse;
import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.lagDato;
import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.lagPeriode;
import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.lagPersonstatuser;
import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.lagUtenlandsAdresse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.AktoerId;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.BostedsadressePeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Landkoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonstatusPeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PostadressePeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Statsborgerskap;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.StatsborgerskapPeriode;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonhistorikkResponse;
import no.nav.tjeneste.virksomhet.person.v3.metadata.Endringstyper;

/**
 * Hardkodet testsett for case 2, ref https://jira.adeo.no/browse/REG-296
 */
class PersonhistorikkMockCase2 {

    public static final XMLGregorianCalendar SYTTENDE_MAI = lagDato(2018, 4, 17);

    public HentPersonhistorikkResponse mockedPerson(AktoerId aktoerId) {
        HentPersonhistorikkResponse response = new HentPersonhistorikkResponse();
        response.withAktoer(aktoerId);

        return mockedPerson(response);
    }

    private HentPersonhistorikkResponse mockedPerson(HentPersonhistorikkResponse response) {
        response.withPersonstatusListe(personstatusPerson2());
        response.withStatsborgerskapListe(personStatsborgerskapPerson2());
        response.withBostedsadressePeriodeListe(personNorskAdresserPerson2());
        response.withPostadressePeriodeListe(personUtenlandsadresserPerson2());

        return response;
    }

    private List<PersonstatusPeriode> personstatusPerson2() {
        List<PersonstatusPeriode> resultat = new ArrayList<>();

        PersonstatusPeriode bosa = new PersonstatusPeriode();
        bosa.withEndretAv(ENDRET_AV);
        bosa.withEndringstidspunkt(TIDENES_MORGEN);
        bosa.withEndringstype(Endringstyper.NY);
        bosa.withPersonstatus(lagPersonstatuser("BOSA"));
        bosa.withPeriode(lagPeriode(TIDENES_MORGEN, lagDato(2018, 4, 16)));

        PersonstatusPeriode utva = new PersonstatusPeriode();
        utva.withEndretAv(ENDRET_AV);
        utva.withEndringstidspunkt(SYTTENDE_MAI);
        utva.withEndringstype(Endringstyper.ENDRET);
        utva.withPersonstatus(lagPersonstatuser("UTVA"));
        utva.withPeriode(lagPeriode(SYTTENDE_MAI, ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now())));

        resultat.add(bosa);
        resultat.add(utva);

        return resultat;
    }

    private List<StatsborgerskapPeriode> personStatsborgerskapPerson2() {
        List<StatsborgerskapPeriode> resultat = new ArrayList<>();

        StatsborgerskapPeriode periode1 = new StatsborgerskapPeriode();
        periode1.withEndretAv(ENDRET_AV);
        periode1.withEndringstidspunkt(TIDENES_MORGEN);
        periode1.withPeriode(lagPeriode(TIDENES_MORGEN, lagDato(2018, 4, 16)));
        periode1.withEndringstype(Endringstyper.NY);

        Statsborgerskap norsk = new Statsborgerskap();
        norsk.withEndretAv(ENDRET_AV);
        norsk.withEndringstidspunkt(TIDENES_MORGEN);
        norsk.withEndringstype(Endringstyper.NY);
        Landkoder norge = new Landkoder();
        norge.withKodeRef("Landkoder");
        norge.setValue("NOR");
        norge.setKodeRef("NOR");
        norsk.withLand(norge);

        StatsborgerskapPeriode periode2 = new StatsborgerskapPeriode();
        periode2.withEndretAv(ENDRET_AV);
        periode2.withEndringstidspunkt(SYTTENDE_MAI);
        periode2.withPeriode(lagPeriode(SYTTENDE_MAI, ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now())));
        periode2.withEndringstype(Endringstyper.ENDRET);

        Statsborgerskap amerikansk = new Statsborgerskap();
        amerikansk.withEndretAv(ENDRET_AV);
        amerikansk.withEndringstidspunkt(TIDENES_MORGEN);
        amerikansk.withEndringstype(Endringstyper.ENDRET);
        Landkoder usa = new Landkoder();
        usa.withKodeRef("Landkoder");
        usa.setValue("USA");
        usa.setKodeRef("USA");
        amerikansk.withLand(usa);

        periode1.withStatsborgerskap(norsk);
        periode2.withStatsborgerskap(amerikansk);

        resultat.add(periode1);
        resultat.add(periode2);

        return resultat;
    }

    private List<BostedsadressePeriode> personNorskAdresserPerson2() {
        List<BostedsadressePeriode> resultat = new ArrayList<>();

        BostedsadressePeriode norskAdresse = new BostedsadressePeriode();
        norskAdresse.withEndretAv(ENDRET_AV);
        norskAdresse.withEndringstidspunkt(TIDENES_MORGEN);
        norskAdresse.withEndringstype(Endringstyper.NY);
        norskAdresse.withPeriode(lagPeriode(TIDENES_MORGEN, lagDato(2018, 4, 4)));
        norskAdresse.withBostedsadresse(lagBostedadresse(TIDENES_MORGEN, Endringstyper.NY));


        BostedsadressePeriode norskAdresse2 = new BostedsadressePeriode();
        norskAdresse2.withEndretAv(ENDRET_AV);
        norskAdresse2.withEndringstidspunkt(lagDato(2018, 4, 5));
        norskAdresse2.withEndringstype(Endringstyper.ENDRET);
        norskAdresse2.withPeriode(lagPeriode(lagDato(2018, 4, 5), lagDato(2018, 4, 16)));
        norskAdresse2.withBostedsadresse(lagBostedadresse(lagDato(2018, 4, 5), Endringstyper.ENDRET));

        resultat.add(norskAdresse);
        resultat.add(norskAdresse2);

        return resultat;
    }

    private List<PostadressePeriode> personUtenlandsadresserPerson2() {
        List<PostadressePeriode> resultat = new ArrayList<>();

        PostadressePeriode annenAdresse = new PostadressePeriode();
        annenAdresse.withEndretAv(ENDRET_AV);
        annenAdresse.withEndringstidspunkt(SYTTENDE_MAI);
        annenAdresse.withEndringstype(Endringstyper.ENDRET);
        annenAdresse.withPostadresse(lagUtenlandsAdresse(SYTTENDE_MAI, Endringstyper.ENDRET, "USA"));
        annenAdresse.withPeriode(lagPeriode(SYTTENDE_MAI, ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now())));

        resultat.add(annenAdresse);
        return resultat;
    }

}
