package no.nav.tjeneste.virksomhet.person.v3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.vtp.felles.ConversionUtils;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseModell;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bostedsadresse;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.BostedsadressePeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Periode;
import no.nav.tjeneste.virksomhet.person.v3.metadata.Endringstyper;

public class BostedsadresseAdapter {
    public static final String ENDRET_AV = "vtp";

    List<BostedsadressePeriode> fra(List<AdresseModell> data) {

        List<BostedsadressePeriode> resultat = new ArrayList<>();

        for (AdresseModell adr : data) {

            BostedsadressePeriode adr1 = new BostedsadressePeriode();
            adr1.withEndretAv(ENDRET_AV);
            adr1.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(adr.getFom()==null?LocalDate.now():adr.getFom()));
            adr1.withEndringstype(adr.getEndringstype() == null ? Endringstyper.NY : Endringstyper.fromValue(adr.getEndringstype()));

            Bostedsadresse bostedsadresse = new Bostedsadresse();
            bostedsadresse.withEndretAv(ENDRET_AV);
            bostedsadresse.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(adr.getFom()==null?LocalDate.now():adr.getFom()));
            bostedsadresse.withEndringstype(adr.getEndringstype() == null ? Endringstyper.NY : Endringstyper.fromValue(adr.getEndringstype()));

            bostedsadresse.withStrukturertAdresse(AdresseAdapter.tilStrukturert(adr));

            adr1.withBostedsadresse(bostedsadresse);
            LocalDate fom = adr.getFom() == null ? LocalDate.of(2000, 1, 1) : adr.getFom();
            LocalDate tom = adr.getTom() == null ? LocalDate.of(2050, 1, 1) : adr.getTom();
            adr1.withPeriode(lagPeriode(fom, tom));

            resultat.add(adr1);

        }
        return resultat;
    }

    private static Periode lagPeriode(LocalDate fom, LocalDate tom) {
        Periode periode = new Periode();
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(fom));
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(tom));
        return periode;
    }
}
