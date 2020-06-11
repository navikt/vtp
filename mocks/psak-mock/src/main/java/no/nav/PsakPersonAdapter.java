package no.nav;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseType;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.GateadresseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenBostedsAdresse;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson;
import no.nav.tjeneste.virksomhet.person.v3.AdresseAdapter;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.UstrukturertAdresse;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.Optional;

public class PsakPersonAdapter {
    private PsakPersonAdapter(){}

    public static ASBOPenPerson toASBOPerson(SøkerModell søker) {
        ASBOPenPerson asboPenPerson = new ASBOPenPerson();
        asboPenPerson.setFodselsnummer(søker.getIdent());
        asboPenPerson.setFornavn(søker.getFornavn());
        asboPenPerson.setEtternavn(søker.getEtternavn());
        asboPenPerson.setKortnavn(søker.getFornavn() + " " + søker.getEtternavn());
        asboPenPerson.setStatus(søker.getPersonstatus().getStatus());
        asboPenPerson.setStatusKode(søker.getPersonstatus().getPersonstatusType().toString());
        asboPenPerson.setDiskresjonskode(søker.getDiskresjonskode());
        asboPenPerson.setDodsdato(fetchDate(søker.getDødsdato()));
        asboPenPerson.setSivilstand(søker.getSivilstand().getKode());
        asboPenPerson.setSivilstandDato(fetchDate(søker.getSivilstand().getEndringstidspunkt()));
        asboPenPerson.setSprakKode(søker.getSpråk2Bokstaver());
        asboPenPerson.setSprakBeskrivelse(søker.getSpråk());
        asboPenPerson.setBostedsAdresse(søker.getAdresse(AdresseType.BOSTEDSADRESSE)
                .map(a -> (GateadresseModell)a)
                .map(PsakPersonAdapter::convert)
                .orElse(null));
        asboPenPerson.setErEgenansatt(false);

        return asboPenPerson;
    }

    private static GregorianCalendar fetchDate(LocalDate date) {
        return Optional.ofNullable(date)
                .map(d -> GregorianCalendar.from(d.atStartOfDay(ZoneId.systemDefault())))
                .orElse(null);
    }

    private static ASBOPenBostedsAdresse convert(GateadresseModell adresse) {
        ASBOPenBostedsAdresse asboPenBostedsAdresse = new ASBOPenBostedsAdresse();
        asboPenBostedsAdresse.setAdresseType(AdresseType.BOSTEDSADRESSE.name());
        asboPenBostedsAdresse.setBolignr(adresse.getHusnummer().toString());
        asboPenBostedsAdresse.setBoadresse1(adresse.getGatenavn() + " " + adresse.getHusnummer().toString());
        asboPenBostedsAdresse.setPostnummer(adresse.getPostnummer());
        return asboPenBostedsAdresse;
    }

}
