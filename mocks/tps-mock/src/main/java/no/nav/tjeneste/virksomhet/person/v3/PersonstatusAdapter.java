package no.nav.tjeneste.virksomhet.person.v3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonstatusModell;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Periode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonstatusPeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personstatuser;
import no.nav.tjeneste.virksomhet.person.v3.metadata.Endringstyper;

public class PersonstatusAdapter {
    public static final String ENDRET_AV = "fpmock2";

    public List<PersonstatusPeriode> fra(List<PersonstatusModell> allePersonstatus) {
        List<PersonstatusPeriode> resultat = new ArrayList<>();
        for (PersonstatusModell ps : allePersonstatus) {
            PersonstatusPeriode personstatusPeriode = new PersonstatusPeriode();
            personstatusPeriode.withEndretAv(ENDRET_AV);
            personstatusPeriode.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now()));
            personstatusPeriode.withEndringstype(ps.getEndringstype() == null ? Endringstyper.NY : Endringstyper.fromValue(ps.getEndringstype()));
            personstatusPeriode.withPersonstatus(lagPersonstatuser(ps.getStatus()));

            LocalDate fom = ps.getFom() == null ? LocalDate.of(2000, 1, 1) : ps.getFom();
            LocalDate tom = ps.getTom() == null ? LocalDate.of(2050, 1, 1) : ps.getTom();
            personstatusPeriode.withPeriode(lagPeriode(fom, tom));
            
            resultat.add(personstatusPeriode);
        }
        return resultat;
    }

    private static Personstatuser lagPersonstatuser(String personstatus) {
        Personstatuser personstatuser = new Personstatuser();
        personstatuser.setKodeverksRef("Personstatuser");
        personstatuser.setKodeRef(personstatus);
        personstatuser.setValue(personstatus);
        return personstatuser;
    }

    private static Periode lagPeriode(LocalDate fom, LocalDate tom) {
        Periode periode = new Periode();
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(fom));
        periode.withTom(ConversionUtils.convertToXMLGregorianCalendar(tom));
        return periode;
    }
}
