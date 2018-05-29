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

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.AktoerId;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.BostedsadressePeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Landkoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonstatusPeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PostadressePeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Statsborgerskap;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.StatsborgerskapPeriode;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonhistorikkResponse;
import no.nav.tjeneste.virksomhet.person.v3.metadata.Endringstyper;

class PersonhistorikkMockCase7 {

    public HentPersonhistorikkResponse mockedPerson(AktoerId aktoerId) {
        HentPersonhistorikkResponse response = new HentPersonhistorikkResponse();
        response.withAktoer(aktoerId);

        return mockedPerson(response);
    }

    private HentPersonhistorikkResponse mockedPerson(HentPersonhistorikkResponse response) {
        response.withPersonstatusListe(personstatusPerson7());
        response.withStatsborgerskapListe(personStatsborgerskapPerson7());
        response.withBostedsadressePeriodeListe(personNorskAdresserPerson7());
        response.withPostadressePeriodeListe(personUtenlandsadresserPerson7());

        return response;
    }

    private List<PersonstatusPeriode> personstatusPerson7() {
        List<PersonstatusPeriode> resultat = new ArrayList<>();

        PersonstatusPeriode bosa = new PersonstatusPeriode();
        bosa.withEndretAv(ENDRET_AV);
        bosa.withEndringstidspunkt(TIDENES_MORGEN);
        bosa.withEndringstype(Endringstyper.NY);
        bosa.withPersonstatus(lagPersonstatuser("BOSA"));
        bosa.withPeriode(lagPeriode(TIDENES_MORGEN, lagDato(2018, 4, 6)));

        PersonstatusPeriode utva = new PersonstatusPeriode();
        utva.withEndretAv(ENDRET_AV);
        utva.withEndringstidspunkt(lagDato(2018, 4, 7));
        utva.withEndringstype(Endringstyper.ENDRET);
        utva.withPersonstatus(lagPersonstatuser("UTVA"));
        utva.withPeriode(lagPeriode(lagDato(2018, 4, 7), ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now())));
        resultat.add(bosa);
        resultat.add(utva);

        return resultat;
    }

    private List<StatsborgerskapPeriode> personStatsborgerskapPerson7() {
        List<StatsborgerskapPeriode> resultat = new ArrayList<>();

        StatsborgerskapPeriode periode1 = new StatsborgerskapPeriode();
        periode1.withEndretAv(ENDRET_AV);
        periode1.withEndringstidspunkt(TIDENES_MORGEN);
        periode1.withPeriode(lagPeriode(TIDENES_MORGEN, lagDato(2018, 4, 6)));
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
        periode2.withEndringstidspunkt(lagDato(2018, 4, 7));
        periode2.withPeriode(lagPeriode(lagDato(2018, 4, 7), ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now())));
        periode2.withEndringstype(Endringstyper.ENDRET);

        Statsborgerskap britisk = new Statsborgerskap();
        britisk.withEndretAv(ENDRET_AV);
        britisk.withEndringstidspunkt(lagDato(2018, 4, 7));
        britisk.withEndringstype(Endringstyper.ENDRET);

        Landkoder england = new Landkoder();
        england.withKodeRef("Landkoder");
        england.setValue("GBR");
        england.setKodeRef("GBR");
        britisk.withLand(england);

        periode1.withStatsborgerskap(norsk);
        periode2.withStatsborgerskap(britisk);

        resultat.add(periode1);
        resultat.add(periode2);

        return resultat;
    }

    private List<BostedsadressePeriode> personNorskAdresserPerson7() {
        List<BostedsadressePeriode> resultat = new ArrayList<>();

        BostedsadressePeriode norskAdresse = new BostedsadressePeriode();
        norskAdresse.withEndretAv(ENDRET_AV);
        norskAdresse.withEndringstidspunkt(TIDENES_MORGEN);
        norskAdresse.withEndringstype(Endringstyper.NY);
        norskAdresse.withPeriode(lagPeriode(TIDENES_MORGEN, lagDato(2018, 4, 6)));
        norskAdresse.withBostedsadresse(lagBostedadresse(TIDENES_MORGEN, Endringstyper.NY));

        resultat.add(norskAdresse);

        return resultat;
    }

    private List<PostadressePeriode> personUtenlandsadresserPerson7() {
        List<PostadressePeriode> resultat = new ArrayList<>();

        PostadressePeriode annenAdresse = new PostadressePeriode();
        annenAdresse.withEndretAv(ENDRET_AV);
        annenAdresse.withEndringstidspunkt(lagDato(2018, 4, 7));
        annenAdresse.withEndringstype(Endringstyper.ENDRET);
        annenAdresse.withPostadresse(lagUtenlandsAdresse(lagDato(2018, 4, 7), Endringstyper.ENDRET, "GBR"));
        annenAdresse.withPeriode(lagPeriode(lagDato(2018, 4, 7), ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now())));

        resultat.add(annenAdresse);
        return resultat;
    }

}
