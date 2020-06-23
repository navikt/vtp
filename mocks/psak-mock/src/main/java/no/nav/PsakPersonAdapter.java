package no.nav;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseType;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.GateadresseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenBostedsAdresse;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PsakPersonAdapter {
    private PsakPersonAdapter(){}

    private static final Map<String, ASBOPenPerson> PERSONER = new HashMap<>();

    public static ASBOPenPerson toASBOPerson(SøkerModell søker) {
        ASBOPenPerson asboPenPerson = new ASBOPenPerson();
        asboPenPerson.setRelasjoner(null);
        asboPenPerson.setFodselsnummer(søker.getIdent());
        asboPenPerson.setFornavn(søker.getFornavn());
        asboPenPerson.setEtternavn(søker.getEtternavn());
        asboPenPerson.setKortnavn(søker.getFornavn() + " " + søker.getEtternavn());
        asboPenPerson.setStatus(søker.getPersonstatus().getStatus());
        asboPenPerson.setStatusKode(søker.getPersonstatus().getPersonstatusType().toString());
        asboPenPerson.setDiskresjonskode(søker.getDiskresjonskode());
        asboPenPerson.setDodsdato(fetchDate(søker.getDødsdato()).orElse(null));
        asboPenPerson.setSivilstand("UGIF"); //søker.getSivilstand().getKode()); ikke støtte til relasjoner i modellen
        asboPenPerson.setSivilstandDato(fetchDate(søker.getSivilstand().getEndringstidspunkt())
                .orElse(fetchDate(søker.getFødselsdato())
                        .orElse(null)));
        asboPenPerson.setSprakKode(søker.getSpråk2Bokstaver());
        asboPenPerson.setSprakBeskrivelse(søker.getSpråk());
        asboPenPerson.setBostedsAdresse(søker.getAdresse(AdresseType.BOSTEDSADRESSE)
                .map(a -> (GateadresseModell)a)
                .map(PsakPersonAdapter::convert)
                .orElse(null));
        asboPenPerson.setErEgenansatt(false);

        PERSONER.put(asboPenPerson.getFodselsnummer(), asboPenPerson);
        return asboPenPerson;
    }

    public static Optional<ASBOPenPerson> getPreviouslyConverted(String foedselsnummer){
        return Optional.ofNullable(PERSONER.get(foedselsnummer));
    }

    private static Optional<GregorianCalendar> fetchDate(LocalDate date) {
        return Optional.ofNullable(date)
                .map(d -> GregorianCalendar.from(d.atStartOfDay(ZoneId.systemDefault())));
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
