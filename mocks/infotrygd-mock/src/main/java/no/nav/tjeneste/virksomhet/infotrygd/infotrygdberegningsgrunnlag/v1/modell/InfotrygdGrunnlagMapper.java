package no.nav.tjeneste.virksomhet.infotrygd.infotrygdberegningsgrunnlag.v1.modell;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdArbeidsforhold;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdBeregningsgrunnlag;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdBeregningsgrunnlagPeriodeYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdEngangsstønadBeregningsgrunnlag;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdForeldrepengerBeregningsgrunnlag;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdPårørendeSykdomBeregningsgrunnlag;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdSykepengerBeregningsgrunnlag;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdVedtak;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Arbeidsforhold;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Engangsstoenad;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Foreldrepenger;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Grunnlag;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.ObjectFactory;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.PaaroerendeSykdom;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Periode;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.PeriodeYtelse;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Sykepenger;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Vedtak;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.meldinger.FinnGrunnlagListeResponse;

public class InfotrygdGrunnlagMapper {
    private ObjectFactory objectFactory = new ObjectFactory();

    public InfotrygdGrunnlagMapper() {
    }

    public FinnGrunnlagListeResponse mapInfotrygdGrunnlag(FinnGrunnlagListeResponse response, List<InfotrygdBeregningsgrunnlag> igListe) {
        for (InfotrygdBeregningsgrunnlag grunnlag : igListe) {
            if (grunnlag instanceof InfotrygdForeldrepengerBeregningsgrunnlag) {
                response.getForeldrepengerListe().add(mapFP((InfotrygdForeldrepengerBeregningsgrunnlag) grunnlag));
            } else if (grunnlag instanceof InfotrygdEngangsstønadBeregningsgrunnlag) {
                response.getEngangstoenadListe().add(mapES((InfotrygdEngangsstønadBeregningsgrunnlag) grunnlag));
            } else if (grunnlag instanceof InfotrygdSykepengerBeregningsgrunnlag) {
                response.getSykepengerListe().add(mapSP((InfotrygdSykepengerBeregningsgrunnlag) grunnlag));
            } else if (grunnlag instanceof InfotrygdPårørendeSykdomBeregningsgrunnlag) {
                response.getPaaroerendeSykdomListe().add(mapPS((InfotrygdPårørendeSykdomBeregningsgrunnlag) grunnlag));
            }
        }
        return response;
    }

    private Foreldrepenger mapFP(InfotrygdForeldrepengerBeregningsgrunnlag grunnlag) {
        Foreldrepenger foreldrepenger = objectFactory.createForeldrepenger();
        mapPeriodeYtelse(foreldrepenger, grunnlag);
        foreldrepenger.setOpprinneligIdentdato(ConversionUtils.convertToXMLGregorianCalendar(grunnlag.getOpprinneligStartdato()));
        foreldrepenger.setDekningsgrad(grunnlag.getDekningsgrad());
        foreldrepenger.setGradering(grunnlag.getGradering());
        foreldrepenger.setFoedselsdatoBarn(ConversionUtils.convertToXMLGregorianCalendar(grunnlag.getFødselsdatoBarn()));
        return foreldrepenger;
    }

    private Sykepenger mapSP(InfotrygdSykepengerBeregningsgrunnlag grunnlag) {
        Sykepenger sykepenger = objectFactory.createSykepenger();
        mapPeriodeYtelse(sykepenger, grunnlag);
        sykepenger.setInntektsgrunnlagProsent(grunnlag.getInntektsgrunnlagProsent());
        return sykepenger;
    }

    private PaaroerendeSykdom mapPS(InfotrygdPårørendeSykdomBeregningsgrunnlag grunnlag) {
        PaaroerendeSykdom psykdom = objectFactory.createPaaroerendeSykdom();
        mapPeriodeYtelse(psykdom, grunnlag);
        psykdom.setFoedselsdatoPleietrengende(ConversionUtils.convertToXMLGregorianCalendar(grunnlag.getFødselsdatoPleietrengende()));
        return psykdom;
    }

    private Engangsstoenad mapES(InfotrygdEngangsstønadBeregningsgrunnlag grunnlag) {
        Engangsstoenad es = objectFactory.createEngangsstoenad();
        mapGrunnlag(es, grunnlag);
        return es;
    }

    private void mapGrunnlag(Grunnlag grunnlag, InfotrygdBeregningsgrunnlag infotrygdGrunnlag) {
        grunnlag.setBehandlingstema(objectFactory.createBehandlingstema());
        grunnlag.getBehandlingstema().setValue(infotrygdGrunnlag.getBehandlingTema().getKode());
        grunnlag.setIdentdato(ConversionUtils.convertToXMLGregorianCalendar(infotrygdGrunnlag.getStartdato()));
        grunnlag.setPeriode(lagPeriode(infotrygdGrunnlag.getFom(), infotrygdGrunnlag.getTom()));
        grunnlag.getVedtakListe().addAll(mapVedtak(infotrygdGrunnlag));
    }

    private void mapPeriodeYtelse(PeriodeYtelse grunnlag, InfotrygdBeregningsgrunnlagPeriodeYtelse infotrygdGrunnlag) {
        mapGrunnlag(grunnlag, infotrygdGrunnlag);
        grunnlag.setArbeidskategori(objectFactory.createArbeidskategori());
        grunnlag.getArbeidskategori().setValue(infotrygdGrunnlag.getArbeidskategori().getKode());
        grunnlag.getArbeidsforholdListe().addAll(mapArbeidsforhold(infotrygdGrunnlag));
    }

    private Periode lagPeriode(LocalDate fom, LocalDate tom) {
        Periode resultat = objectFactory.createPeriode();
        resultat.setFom(ConversionUtils.convertToXMLGregorianCalendar(fom));
        resultat.setTom(ConversionUtils.convertToXMLGregorianCalendar(tom));
        return resultat;
    }

    private List<Vedtak> mapVedtak(InfotrygdBeregningsgrunnlag grunnlag) {
        List<Vedtak> resultat = new ArrayList<>();
        for (InfotrygdVedtak iVedtak : grunnlag.getVedtak()) {
            Vedtak v = objectFactory.createVedtak();
            v.setAnvistPeriode(lagPeriode(iVedtak.getFom(), iVedtak.getTom()));
            v.setUtbetalingsgrad(iVedtak.getUtbetalingsgrad());
            resultat.add(v);
        }
        return resultat;
    }

    private List<Arbeidsforhold> mapArbeidsforhold(InfotrygdBeregningsgrunnlagPeriodeYtelse grunnlag) {
        List<Arbeidsforhold> resultat = new ArrayList<>();
        for (InfotrygdArbeidsforhold iArbeid : grunnlag.getArbeidsforhold()) {
            Arbeidsforhold arb = objectFactory.createArbeidsforhold();
            arb.setInntektForPerioden(iArbeid.getBeløp());
            arb.setInntektsPeriode(objectFactory.createInntektsperiode());
            arb.getInntektsPeriode().setValue(iArbeid.getInntektsPeriodeType().getKode());
            arb.setOrgnr(iArbeid.getOrgnr());
            resultat.add(arb);
        }
        return resultat;
    }
}
