package no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.modell;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Arbeidsforhold;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Engangsstoenad;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Foreldrepenger;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Grunnlag;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.PaaroerendeSykdom;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Periode;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.PeriodeYtelse;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Sykepenger;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.ObjectFactory;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.informasjon.Vedtak;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.meldinger.FinnGrunnlagListeResponse;
import no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell.InfotrygdArbeid;
import no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell.InfotrygdGrunnlag;
import no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell.InfotrygdVedtak;


public class InfotrygdGrunnlagMapper {
    private ObjectFactory objectFactory = new ObjectFactory();
    private static final String FORELDREPENGER_TEMA = "FP";
    private static final String SYKEPENGER_TEMA = "SP";
    private static final String PAARORENDE_TEMA = "PS";
    private static final String ENGSTONAD_TEMA = "ES";

    public InfotrygdGrunnlagMapper() {
    }

    public FinnGrunnlagListeResponse mapInfotrygdGrunnlag(FinnGrunnlagListeResponse response, List<InfotrygdGrunnlag> igListe) {
        for (InfotrygdGrunnlag grunnlag : igListe) {
            switch (grunnlag.getTema()) {
                case FORELDREPENGER_TEMA:
                    response.getForeldrepengerListe().add(mapFP(grunnlag));
                    break;
                case SYKEPENGER_TEMA:
                    response.getSykepengerListe().add(mapSP(grunnlag));
                    break;
                case PAARORENDE_TEMA:
                    response.getPaaroerendeSykdomListe().add(mapPS(grunnlag));
                    break;
                case ENGSTONAD_TEMA:
                    response.getEngangstoenadListe().add(mapES(grunnlag));
                    break;
                default:
                    break;
            }
        }
        return response;
    }

    private Foreldrepenger mapFP(InfotrygdGrunnlag grunnlag) {
        Foreldrepenger foreldrepenger = objectFactory.createForeldrepenger();
        mapPeriodeYtelse(foreldrepenger, grunnlag);
        foreldrepenger.setOpprinneligIdentdato(ConversionUtils.convertToXMLGregorianCalendar(grunnlag.getOpprinneligStartDato()));
        foreldrepenger.setDekningsgrad(grunnlag.getDekningProsent().intValue());
        foreldrepenger.setDekningsgrad(grunnlag.getGradering().intValue());
        foreldrepenger.setFoedselsdatoBarn(ConversionUtils.convertToXMLGregorianCalendar(grunnlag.getFoedselsDatoAnnenpart()));
        return foreldrepenger;
    }

    private Sykepenger mapSP(InfotrygdGrunnlag grunnlag) {
        Sykepenger sykepenger = objectFactory.createSykepenger();
        mapPeriodeYtelse(sykepenger, grunnlag);
        sykepenger.setInntektsgrunnlagProsent(grunnlag.getDekningProsent().intValue());
        return sykepenger;
    }

    private PaaroerendeSykdom  mapPS(InfotrygdGrunnlag grunnlag) {
        PaaroerendeSykdom psykdom = objectFactory.createPaaroerendeSykdom();
        mapPeriodeYtelse(psykdom, grunnlag);
        psykdom.setFoedselsdatoPleietrengende(ConversionUtils.convertToXMLGregorianCalendar(grunnlag.getFoedselsDatoAnnenpart()));
        return psykdom;
    }

    private Engangsstoenad mapES(InfotrygdGrunnlag grunnlag) {
        Engangsstoenad es = objectFactory.createEngangsstoenad();
        mapGrunnlag(es, grunnlag);
        return es;
    }

    private void mapGrunnlag(Grunnlag grunnlag, InfotrygdGrunnlag infotrygdGrunnlag) {
        grunnlag.setBehandlingstema(objectFactory.createBehandlingstema());
        grunnlag.getBehandlingstema().setValue(infotrygdGrunnlag.getBehandlingstema());
        grunnlag.setIdentdato(ConversionUtils.convertToXMLGregorianCalendar(infotrygdGrunnlag.getStartDato()));
        grunnlag.setPeriode(lagPeriode(infotrygdGrunnlag.getPeriodeFom(), infotrygdGrunnlag.getPeriodeTom()));
        grunnlag.getVedtakListe().addAll(mapVedtak(infotrygdGrunnlag));
    }

    private void mapPeriodeYtelse(PeriodeYtelse grunnlag, InfotrygdGrunnlag infotrygdGrunnlag) {
        mapGrunnlag(grunnlag, infotrygdGrunnlag);
        grunnlag.setArbeidskategori(objectFactory.createArbeidskategori());
        grunnlag.getArbeidskategori().setValue(infotrygdGrunnlag.getArbeidsKategori());
        grunnlag.getArbeidsforholdListe().addAll(mapArbeidsforhold(infotrygdGrunnlag));
    }

    private Periode lagPeriode(LocalDate fom, LocalDate tom) {
        Periode resultat = objectFactory.createPeriode();
        resultat.setFom(ConversionUtils.convertToXMLGregorianCalendar(fom));
        resultat.setTom(ConversionUtils.convertToXMLGregorianCalendar(tom));
        return resultat;
    }

    private List<Vedtak> mapVedtak(InfotrygdGrunnlag grunnlag) {
        List<Vedtak> resultat = new ArrayList<>();
        if (grunnlag.getInfotrygdVedtakList() == null) {
            return resultat;
        }
        for (InfotrygdVedtak iVedtak : grunnlag.getInfotrygdVedtakList()) {
            Vedtak v = objectFactory.createVedtak();
            v.setAnvistPeriode(lagPeriode(iVedtak.getAnvistFom(), iVedtak.getAnvistTom()));
            v.setUtbetalingsgrad(iVedtak.getUtbetalingGrad().intValue());
            resultat.add(v);
        }
        return resultat;
    }

    private List<Arbeidsforhold> mapArbeidsforhold(InfotrygdGrunnlag grunnlag) {
        List<Arbeidsforhold> resultat = new ArrayList<>();
        if (grunnlag.getInfotrygdArbeidList() == null) {
            return resultat;
        }
        for (InfotrygdArbeid iArbeid : grunnlag.getInfotrygdArbeidList()) {
            Arbeidsforhold arb = objectFactory.createArbeidsforhold();
            arb.setInntektForPerioden(iArbeid.getBeløp());
            arb.setInntektsPeriode(objectFactory.createInntektsperiode());
            arb.getInntektsPeriode().setValue(iArbeid.getInntektPeriodeType());
            arb.setOrgnr(iArbeid.getOrgnr());
            resultat.add(arb);
        }
        return resultat;
    }
}