package no.nav.tjeneste.virksomhet.person.v3;

import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.ENDRET_AV;
import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.TIDENES_MORGEN;
import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.lagBostedadresse;
import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.lagDato;
import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.lagPeriode;
import static no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl.lagPersonstatuser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.AktoerId;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.BostedsadressePeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Landkoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonstatusPeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Statsborgerskap;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.StatsborgerskapPeriode;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonhistorikkResponse;
import no.nav.tjeneste.virksomhet.person.v3.metadata.Endringstyper;

class PersonhistorikkMockCase6 {

    public HentPersonhistorikkResponse mockedPerson(AktoerId aktoerId) {
        HentPersonhistorikkResponse response = new HentPersonhistorikkResponse();
        response.withAktoer(aktoerId);

        return mockedPerson(response);
    }

    private HentPersonhistorikkResponse mockedPerson(HentPersonhistorikkResponse response) {
        response.withPersonstatusListe(personstatusPerson6());
        response.withStatsborgerskapListe(personStatsborgerskapPerson6());
        response.withBostedsadressePeriodeListe(personNorskAdresserPerson6());

        return response;
    }

    private List<PersonstatusPeriode> personstatusPerson6() {
        List<PersonstatusPeriode> resultat = new ArrayList<>();

        PersonstatusPeriode bosa = new PersonstatusPeriode();
        bosa.withEndretAv(ENDRET_AV);
        bosa.withEndringstidspunkt(TIDENES_MORGEN);
        bosa.withEndringstype(Endringstyper.NY);
        bosa.withPersonstatus(lagPersonstatuser("FOSV"));
        bosa.withPeriode(lagPeriode(TIDENES_MORGEN, lagDato(2018, 4, 6)));

        PersonstatusPeriode død = new PersonstatusPeriode();
        død.withEndretAv(ENDRET_AV);
        død.withEndringstidspunkt(lagDato(2018, 4, 7));
        død.withEndringstype(Endringstyper.ENDRET);
        død.withPersonstatus(lagPersonstatuser("DØD"));
        død.withPeriode(lagPeriode(lagDato(2018, 4, 7), ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now())));
        resultat.add(bosa);
        resultat.add(død);

        return resultat;
    }

    private List<StatsborgerskapPeriode> personStatsborgerskapPerson6() {
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

        periode1.withStatsborgerskap(norsk);

        resultat.add(periode1);

        return resultat;
    }

    private List<BostedsadressePeriode> personNorskAdresserPerson6() {
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

}
