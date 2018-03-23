package no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.modell;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Meldekort;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.ObjectFactory;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Periode;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Sak;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Saksstatuser;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Tema;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Vedtak;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Vedtaksstatuser;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeResponse;


public class ArenaMUMapper {
    private ObjectFactory objectFactory = new ObjectFactory();

    public ArenaMUMapper() {
    }

    public FinnMeldekortUtbetalingsgrunnlagListeResponse mapArenaSaker(FinnMeldekortUtbetalingsgrunnlagListeResponse response, List<ArenaMUSak> arenaSaker) {
        for (ArenaMUSak arenaSak : arenaSaker) {
            Sak sak = objectFactory.createSak();
            sak.setFagsystemSakId(arenaSak.getSaksnummer());
            Tema tema = objectFactory.createTema();
            tema.setValue(arenaSak.getTema());
            sak.setTema(tema);
            Saksstatuser status = objectFactory.createSaksstatuser();
            status.setValue(arenaSak.getSakStatus());
            sak.setSaksstatus(status);
            sak.getVedtakListe().addAll(mapArenaVedtakListe(arenaSak.getArenaVedtak()));
            response.getMeldekortUtbetalingsgrunnlagListe().add(sak);
        }
        return response;
    }

    private List<Vedtak> mapArenaVedtakListe(List<ArenaMUVedtak> arenaVedtakList) {
        List<Vedtak> resultat = new ArrayList<>();

        if (arenaVedtakList == null || arenaVedtakList.isEmpty()) {
            return resultat;
        }
        for (ArenaMUVedtak aVedtak : arenaVedtakList) {
            Vedtak v = objectFactory.createVedtak();
            Vedtaksstatuser status = objectFactory.createVedtaksstatuser();
            status.setValue(aVedtak.getVedtakStatus());
            v.setVedtaksstatus(status);
            Periode periode = objectFactory.createPeriode();
            periode.setFom(datoFraString(aVedtak.getVedtakFom()));
            periode.setTom(datoFraString(aVedtak.getVedtakTom()));
            v.setVedtaksperiode(periode);
            v.setDatoKravMottatt(datoFraString(aVedtak.getKravMottattDato()));
            v.setVedtaksdato(datoFraString(aVedtak.getVedtakDato()));
            v.getMeldekortListe().addAll(mapArenaMeldekortListe(aVedtak.getMeldekort()));
            resultat.add(v);
        }
        return resultat;
    }

    private List<Meldekort> mapArenaMeldekortListe(List<ArenaMUMeldekort> meldekortList) {
        List<Meldekort> resultat = new ArrayList<>();
        if (meldekortList == null || meldekortList.isEmpty()) {
            return resultat;
        }
        for (ArenaMUMeldekort aMeldekort : meldekortList) {
            Meldekort meldekort = objectFactory.createMeldekort();
            meldekort.setDagsats(aMeldekort.getDagsats().doubleValue());
            meldekort.setBeloep(aMeldekort.getBeløp().doubleValue());
            meldekort.setUtbetalingsgrad(aMeldekort.getUtbetalingsGrad().doubleValue());
            Periode periode = objectFactory.createPeriode();
            periode.setFom(datoFraString(aMeldekort.getMeldekortFom()));
            periode.setTom(datoFraString(aMeldekort.getMeldekortTom()));
            meldekort.setMeldekortperiode(periode);
            resultat.add(meldekort);
        }
        return resultat;
    }

    private XMLGregorianCalendar datoFraString(String s) {
        if (s == null) {
            return null;
        }
        return ConversionUtils.convertToXMLGregorianCalendar(LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}