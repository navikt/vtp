package no.nav.tjeneste.virksomhet.arena.meldekort.modell;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.util.ConversionUtils;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaMeldekort;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaSak;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaVedtak;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.SakStatus;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.VedtakStatus;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Kodeverdi;
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
                                                                       FinnMeldekortUtbetalingsgrunnlagListeResponse response,
                                                                       List<ArenaSak> arenaSaker) {

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
        List<String> temaer = temaListe.stream().map(Kodeverdi::getValue).collect(Collectors.toList());

        for (ArenaSak arenaSak : arenaSaker) {
            if (filtrerVekkSak(arenaSak, temaer)) {
                continue;
            }

            Sak sak = objectFactory.createSak();
            sak.setFagsystemSakId(arenaSak.saksnummer());
            Tema tema = objectFactory.createTema();
            tema.setValue(arenaSak.tema().name());
            sak.setTema(tema);
            sak.setSaksstatus(mapSakStatus(arenaSak.status()));

            List<ArenaVedtak> vedtak = arenaSak.vedtak().stream()
                    .filter(v -> filtrerVedtak(v, fom, tom))
                    .collect(Collectors.toList());
            sak.getVedtakListe().addAll(mapArenaVedtakListe(vedtak));

            response.getMeldekortUtbetalingsgrunnlagListe().add(sak);
        }
        return response;
    }

    private boolean filtrerVedtak(ArenaVedtak vedtak, LocalDate fom, LocalDate tom) {

        if (fom == null && tom == null) {
            return true;
        } else {
            return (fom == null || fom.isBefore(vedtak.tom()) || fom.isEqual(vedtak.tom()))
                && (tom == null || tom.isAfter(vedtak.fom()) || tom.isEqual(vedtak.fom()));
        }
    }

    private boolean filtrerVekkSak(ArenaSak arenaSak, List<String> temaer) {
        boolean filtrerVekkTema = !temaer.isEmpty() && !temaer.contains(arenaSak.tema().name());
        return filtrerVekkTema;
    }

    private Saksstatuser mapSakStatus(SakStatus status) {
        Saksstatuser sakStatus = objectFactory.createSaksstatuser();
        sakStatus.setTermnavn(status.getTermnavn());
        sakStatus.setValue(status.name());
        return sakStatus;
    }

    private List<Vedtak> mapArenaVedtakListe(List<ArenaVedtak> arenaVedtakList) {
        List<Vedtak> resultat = new ArrayList<>();

        if (arenaVedtakList == null || arenaVedtakList.isEmpty()) {
            return resultat;
        }
        for (ArenaVedtak aVedtak : arenaVedtakList) {
            Vedtak v = objectFactory.createVedtak();
            v.setVedtaksstatus(mapVedtakStatus(aVedtak.status()));
            Periode periode = objectFactory.createPeriode();
            periode.setFom(ConversionUtils.convertToXMLGregorianCalendar(aVedtak.fom()));
            periode.setTom(ConversionUtils.convertToXMLGregorianCalendar(aVedtak.tom()));
            v.setVedtaksperiode(periode);
            v.setDatoKravMottatt(ConversionUtils.convertToXMLGregorianCalendar(aVedtak.kravMottattDato()));
            v.setVedtaksdato(ConversionUtils.convertToXMLGregorianCalendar(aVedtak.vedtakDato()));
            v.getMeldekortListe().addAll(mapArenaMeldekortListe(aVedtak.meldekort()));
            v.setDagsats(aVedtak.dagsats().doubleValue());
            resultat.add(v);
        }
        return resultat;
    }

    private Vedtaksstatuser mapVedtakStatus(VedtakStatus status) {
        Vedtaksstatuser vedtakStatus = objectFactory.createVedtaksstatuser();
        vedtakStatus.setTermnavn(status.getTermnavn());
        vedtakStatus.setValue(status.name());
        return vedtakStatus;
    }

    private List<Meldekort> mapArenaMeldekortListe(List<ArenaMeldekort> meldekortList) {
        List<Meldekort> resultat = new ArrayList<>();
        if (meldekortList == null || meldekortList.isEmpty()) {
            return resultat;
        }
        for (ArenaMeldekort aMeldekort : meldekortList) {
            Meldekort meldekort = objectFactory.createMeldekort();
            meldekort.setDagsats(aMeldekort.dagsats().doubleValue());
            meldekort.setBeloep(aMeldekort.beløp().doubleValue());
            meldekort.setUtbetalingsgrad(aMeldekort.utbetalingsgrad().doubleValue());
            Periode periode = objectFactory.createPeriode();
            periode.setFom(ConversionUtils.convertToXMLGregorianCalendar(aMeldekort.fom()));
            periode.setTom(ConversionUtils.convertToXMLGregorianCalendar(aMeldekort.tom()));
            meldekort.setMeldekortperiode(periode);
            resultat.add(meldekort);
        }
        return resultat;
    }
}
