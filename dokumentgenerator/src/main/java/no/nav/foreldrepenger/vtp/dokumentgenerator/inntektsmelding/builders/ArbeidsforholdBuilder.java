package no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.builders;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakUtsettelseKodeliste;
import no.seres.xsd.nav.inntektsmelding_m._20181211.Arbeidsforhold;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakBeregnetInntektEndringKodeliste;
import no.seres.xsd.nav.inntektsmelding_m._20181211.*;

import javax.xml.bind.JAXBElement;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Deprecated
public class ArbeidsforholdBuilder {
    private ObjectFactory objectFactory = new ObjectFactory();
//    private Arbeidsforhold arbeidsforholdKladd;

    private String arbeidsforholdId = null;
    private ÅrsakBeregnetInntektEndringKodeliste aarsakVedEndring;
    private BigDecimal beregnetInntektBelop;
    private List<UtsettelseAvForeldrepenger> utsettelseAvForeldrepengerList;
    private List<GraderingIForeldrepenger> graderingIForeldrepengerList;
    private List<Periode> avtaltFerieListeList;


    public ArbeidsforholdBuilder() {
        utsettelseAvForeldrepengerList = new ArrayList<>();
        graderingIForeldrepengerList = new ArrayList<>();
        avtaltFerieListeList = new ArrayList<>();
    }
    public ArbeidsforholdBuilder medBeregnetInntekt(BigDecimal beregnetInntektBelop) {
        this.beregnetInntektBelop = beregnetInntektBelop;
        return this;
    }
    public ArbeidsforholdBuilder medÅrsakVedEndring(ÅrsakBeregnetInntektEndringKodeliste aarsakVedEndring){
        this.aarsakVedEndring = aarsakVedEndring;
        return this;
    }
    public ArbeidsforholdBuilder medUtsettelse(String årsak, LocalDate periodeFom, LocalDate periodeTom){
        utsettelseAvForeldrepengerList.add(createUtsettelseAvForeldrepenger(map(årsak), periodeFom, periodeTom));
        return this;
    }
    public ArbeidsforholdBuilder medUtsettelse(ÅrsakUtsettelseKodeliste aarsakTilUtsettelse, LocalDate periodeFom, LocalDate periodeTom){
        utsettelseAvForeldrepengerList.add(createUtsettelseAvForeldrepenger(aarsakTilUtsettelse, periodeFom, periodeTom));
        return this;
    }
    public ArbeidsforholdBuilder medUtsettelse(List<UtsettelseAvForeldrepenger> utsettelseAvForeldrepengerList){
        this.utsettelseAvForeldrepengerList = utsettelseAvForeldrepengerList;
        return this;
    }
    public ArbeidsforholdBuilder medGradering(BigDecimal arbeidsprosent, LocalDate fom, LocalDate tom) {
        graderingIForeldrepengerList.add(createGraderingIForeldrepenger(arbeidsprosent, fom, tom));
        return this;
    }
    public ArbeidsforholdBuilder medGradering(List<GraderingIForeldrepenger> graderingIForeldrepengerList){
        this.graderingIForeldrepengerList = graderingIForeldrepengerList;
        return this;
    }
    public ArbeidsforholdBuilder medAvtaltFerie(List<Periode> avtaltFerieListeList){
        this.avtaltFerieListeList = avtaltFerieListeList;
        return this;
    }
    public ArbeidsforholdBuilder medArbeidsforholdId(String arbeidsforholdId) {
        this.arbeidsforholdId = arbeidsforholdId;
        return this;
    }

    private Periode createPeriode(LocalDate periodeFom, LocalDate periodeTom) {
        Periode periode = objectFactory.createPeriode();

        periode.setTom(objectFactory.createPeriodeTom(periodeTom));
        periode.setFom(objectFactory.createPeriodeFom(periodeFom));

        return periode;
    }
    private ÅrsakUtsettelseKodeliste map(String årsak) {
        if (FordelingErketyper.UTSETTELSETYPE_LOVBESTEMT_FERIE.equals(årsak)) {
            return ÅrsakUtsettelseKodeliste.LOVBESTEMT_FERIE;
        }
        if (FordelingErketyper.UTSETTELSETYPE_ARBEID.equals(årsak)) {
            return ÅrsakUtsettelseKodeliste.ARBEID;
        }
        throw new IllegalStateException("Ukjent utsettelseårsak " + årsak);
    }
    private GraderingIForeldrepenger createGraderingIForeldrepenger(BigDecimal arbeidstidIProsent, LocalDate periodeFom, LocalDate periodeTom) {
        GraderingIForeldrepenger graderingIForeldrepenger = new GraderingIForeldrepenger();
        graderingIForeldrepenger.setPeriode(objectFactory.createGraderingIForeldrepengerPeriode(
                createPeriode(periodeFom, periodeTom)));
        graderingIForeldrepenger.setArbeidstidprosent(
                objectFactory.createGraderingIForeldrepengerArbeidstidprosent(arbeidstidIProsent.toBigInteger()));
        return graderingIForeldrepenger;
    }
    private UtsettelseAvForeldrepenger createUtsettelseAvForeldrepenger(ÅrsakUtsettelseKodeliste aarsakTilUtsettelse, LocalDate periodeFom, LocalDate periodeTom) {
        UtsettelseAvForeldrepenger utsettelseAvForeldrepenger = objectFactory.createUtsettelseAvForeldrepenger();
        utsettelseAvForeldrepenger.setAarsakTilUtsettelse(objectFactory
                .createUtsettelseAvForeldrepengerAarsakTilUtsettelse(aarsakTilUtsettelse.value()));
        utsettelseAvForeldrepenger.setPeriode(objectFactory.createUtsettelseAvForeldrepengerPeriode(createPeriode(periodeFom, periodeTom)));
        return utsettelseAvForeldrepenger;
    }

    public Arbeidsforhold build() {
        Arbeidsforhold arbeidsforhold = objectFactory.createArbeidsforhold();

        arbeidsforhold.setArbeidsforholdId(objectFactory.createArbeidsforholdArbeidsforholdId(arbeidsforholdId));

        if (utsettelseAvForeldrepengerList != null && utsettelseAvForeldrepengerList.size() > 0) {
            UtsettelseAvForeldrepengerListe utsettelseAvForeldrepengerListe = objectFactory.createUtsettelseAvForeldrepengerListe();
            utsettelseAvForeldrepengerListe.getUtsettelseAvForeldrepenger().addAll(utsettelseAvForeldrepengerList);
            arbeidsforhold.setUtsettelseAvForeldrepengerListe(
                    objectFactory.createArbeidsforholdUtsettelseAvForeldrepengerListe(utsettelseAvForeldrepengerListe));
        }
        if (avtaltFerieListeList != null && avtaltFerieListeList.size() > 0) {
            AvtaltFerieListe avtaltFerieListe = objectFactory.createAvtaltFerieListe();
            avtaltFerieListe.getAvtaltFerie().addAll(avtaltFerieListeList);
            arbeidsforhold.setAvtaltFerieListe(
                    objectFactory.createArbeidsforholdAvtaltFerieListe(avtaltFerieListe));
        }
        if (graderingIForeldrepengerList != null && graderingIForeldrepengerList.size() > 0) {
            GraderingIForeldrepengerListe graderingIForeldrepengerListe = objectFactory.createGraderingIForeldrepengerListe();
            graderingIForeldrepengerListe.getGraderingIForeldrepenger().addAll(graderingIForeldrepengerList);
            arbeidsforhold.setGraderingIForeldrepengerListe(
                    objectFactory.createArbeidsforholdGraderingIForeldrepengerListe(graderingIForeldrepengerListe));
        }

        Objects.requireNonNull(beregnetInntektBelop, "Beregnet inntekt kan ikke være null");

        Inntekt inntekt = objectFactory.createInntekt();
        inntekt.setBeloep(objectFactory.createInntektBeloep(beregnetInntektBelop));
        if (this.aarsakVedEndring != null) {
            inntekt.setAarsakVedEndring(objectFactory.createInntektAarsakVedEndring(aarsakVedEndring.value()));
        }
        arbeidsforhold.setBeregnetInntekt(objectFactory.createArbeidsforholdBeregnetInntekt(inntekt));

        return arbeidsforhold;
    }

}
