package no.nav.tjeneste.virksomhet.person.v3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.StatsborgerskapModell;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Landkoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Periode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Statsborgerskap;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.StatsborgerskapPeriode;
import no.nav.tjeneste.virksomhet.person.v3.metadata.Endringstyper;

public class StatsborgerskapAdapter {
    public static final String ENDRET_AV = "fpmock2";

    List<StatsborgerskapPeriode> fra(List<StatsborgerskapModell> data) {

        List<StatsborgerskapPeriode> resultat = new ArrayList<>();

        for (no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.StatsborgerskapModell st : data) {
            StatsborgerskapPeriode periode1 = new StatsborgerskapPeriode();
            periode1.withEndretAv(ENDRET_AV);
            periode1.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(st.getFom() == null ? LocalDate.now() : st.getFom()));
            periode1.withEndringstype(st.getEndringstype() == null ? Endringstyper.NY : Endringstyper.fromValue(st.getEndringstype()));

            LocalDate fom = st.getFom() == null ? LocalDate.of(2000, 1, 1) : st.getFom();
            LocalDate tom = st.getTom() == null ? LocalDate.of(2050, 1, 1) : st.getTom();
            periode1.withPeriode(lagPeriode(fom, tom));

            Statsborgerskap statsborgerskap = fra(st);
            periode1.withStatsborgerskap(statsborgerskap);

            resultat.add(periode1);

        }
        return resultat;
    }

    public Statsborgerskap fra(StatsborgerskapModell st) {
        Statsborgerskap statsborgerskap = new Statsborgerskap();
        statsborgerskap.withEndretAv(ENDRET_AV);
        statsborgerskap.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(st.getFom()));
        statsborgerskap.withEndringstype(Endringstyper.NY);

        Landkoder land = new Landkoder();
        land.withKodeRef("Landkoder");
        land.setValue(st.getLandkode());

        statsborgerskap.withLand(land);
        return statsborgerskap;
    }

    static Periode lagPeriode(LocalDate fom, LocalDate tom) {
        Periode periode = new Periode();
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(fom));
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(tom));
        return periode;
    }
}
