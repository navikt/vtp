package no.nav.tjeneste.virksomhet.arena.meldekort.modell;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.ArenaMeldekort;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.ArenaSak;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.ArenaVedtak;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.SakStatus;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.VedtakStatus;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Meldekort;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.ObjectFactory;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Periode;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Sak;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Saksstatuser;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Tema;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Vedtak;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Vedtaksstatuser;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeRequest;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeResponse;

public class ArenaMUMapper {
    private ObjectFactory objectFactory = new ObjectFactory();

    public ArenaMUMapper() {
    }

    public FinnMeldekortUtbetalingsgrunnlagListeResponse mapArenaSaker(FinnMeldekortUtbetalingsgrunnlagListeRequest request,
                                                                       FinnMeldekortUtbetalingsgrunnlagListeResponse response, List<ArenaSak> arenaSaker) {

        final LocalDate fom;
        final LocalDate tom;
        if (request.getPeriode() != null) {
            fom = ConversionUtils.convertToLocalDate(request.getPeriode().getFom());
            tom = ConversionUtils.convertToLocalDate(request.getPeriode().getTom());
        } else {
            fom = null;
            tom = null;
        }
        List<Tema> temaListe = request.getTemaListe();
        List<String> temaer = temaListe.stream().map(a -> a.getValue()).collect(Collectors.toList());

        for (ArenaSak arenaSak : arenaSaker) {
            if (filtrerVekkSak(arenaSak, temaer)) {
                continue;
            }
            
            Sak sak = objectFactory.createSak();
            sak.setFagsystemSakId(arenaSak.getSaksnummer());
            Tema tema = objectFactory.createTema();
            tema.setValue(arenaSak.getTema().getKode());
            sak.setTema(tema);
            sak.setSaksstatus(mapSakStatus(arenaSak.getStatus()));
            
            List<ArenaVedtak> vedtak = arenaSak.getVedtak().stream().filter(v -> filtrerVedtak(v, fom, tom)).collect(Collectors.toList());
            sak.getVedtakListe().addAll(mapArenaVedtakListe(vedtak));
            
            response.getMeldekortUtbetalingsgrunnlagListe().add(sak);
        }
        return response;
    }

    private boolean filtrerVedtak(ArenaVedtak vedtak, LocalDate fom, LocalDate tom) {

        if (fom == null && tom == null) {
            return true;
        } else {
            return (fom == null || fom.isBefore(vedtak.getFom()) || fom.isEqual(vedtak.getFom()))
                && (tom == null || tom.isAfter(vedtak.getTom()) || tom.isEqual(vedtak.getTom()));
        }
    }

    private boolean filtrerVekkSak(ArenaSak arenaSak, List<String> temaer) {
        boolean filtrerVekkTema = !temaer.isEmpty() && !temaer.contains(arenaSak.getTema().getKode());
        return filtrerVekkTema;
    }

    private Saksstatuser mapSakStatus(SakStatus status) {
        Saksstatuser sakStatus = objectFactory.createSaksstatuser();
        sakStatus.setTermnavn(status.getTermnavn());
        sakStatus.setValue(status.getKode());
        return sakStatus;
    }

    private List<Vedtak> mapArenaVedtakListe(List<ArenaVedtak> arenaVedtakList) {
        List<Vedtak> resultat = new ArrayList<>();

        if (arenaVedtakList == null || arenaVedtakList.isEmpty()) {
            return resultat;
        }
        for (ArenaVedtak aVedtak : arenaVedtakList) {
            Vedtak v = objectFactory.createVedtak();
            v.setVedtaksstatus(mapVedtakStatus(aVedtak.getStatus()));
            Periode periode = objectFactory.createPeriode();
            periode.setFom(ConversionUtils.convertToXMLGregorianCalendar(aVedtak.getFom()));
            periode.setTom(ConversionUtils.convertToXMLGregorianCalendar(aVedtak.getTom()));
            v.setVedtaksperiode(periode);
            v.setDatoKravMottatt(ConversionUtils.convertToXMLGregorianCalendar(aVedtak.getKravMottattDato()));
            v.setVedtaksdato(ConversionUtils.convertToXMLGregorianCalendar(aVedtak.getVedtakDato()));
            v.getMeldekortListe().addAll(mapArenaMeldekortListe(aVedtak.getMeldekort()));
            resultat.add(v);
        }
        return resultat;
    }

    private Vedtaksstatuser mapVedtakStatus(VedtakStatus status) {
        Vedtaksstatuser vedtakStatus = objectFactory.createVedtaksstatuser();
        vedtakStatus.setTermnavn(status.getTermnavn());
        vedtakStatus.setValue(status.getKode());
        return vedtakStatus;
    }

    private List<Meldekort> mapArenaMeldekortListe(List<ArenaMeldekort> meldekortList) {
        List<Meldekort> resultat = new ArrayList<>();
        if (meldekortList == null || meldekortList.isEmpty()) {
            return resultat;
        }
        for (ArenaMeldekort aMeldekort : meldekortList) {
            Meldekort meldekort = objectFactory.createMeldekort();
            meldekort.setDagsats(aMeldekort.getDagsats().doubleValue());
            meldekort.setBeloep(aMeldekort.getBeløp().doubleValue());
            meldekort.setUtbetalingsgrad(aMeldekort.getUtbetalingsgrad().doubleValue());
            Periode periode = objectFactory.createPeriode();
            periode.setFom(ConversionUtils.convertToXMLGregorianCalendar(aMeldekort.getFom()));
            periode.setTom(ConversionUtils.convertToXMLGregorianCalendar(aMeldekort.getTom()));
            meldekort.setMeldekortperiode(periode);
            resultat.add(meldekort);
        }
        return resultat;
    }
}
