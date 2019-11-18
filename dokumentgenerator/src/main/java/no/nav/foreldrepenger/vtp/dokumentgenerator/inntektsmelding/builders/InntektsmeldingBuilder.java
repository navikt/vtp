package no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.builders;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.inntektsmelding.xml.kodeliste._20180702.*;
import no.seres.xsd.nav.inntektsmelding_m._20181211.*;
import no.seres.xsd.nav.inntektsmelding_m._20181211.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InntektsmeldingBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingBuilder.class);
    private InntektsmeldingM inntektsmeldingKladd;
    private ArbeidsforholdBuilder arbeidsforholdBuilderKladd;
    private SkjemainnholdBuilder skjemainnholdBuilderKladd;



    public InntektsmeldingBuilder() {
        inntektsmeldingKladd = new InntektsmeldingM();
        arbeidsforholdBuilderKladd = new ArbeidsforholdBuilder();
        skjemainnholdBuilderKladd = new SkjemainnholdBuilder();
    }
    public InntektsmeldingBuilder medPleiepengerPeriodeListe(PleiepengerPeriodeListe pleiepengerPeriodeListe) {
        this.skjemainnholdBuilderKladd.medPleiepengerPeriodeListe(pleiepengerPeriodeListe);
        return this;
    }
    public InntektsmeldingBuilder medOmsorgspenger(Omsorgspenger omsorgspenger) {
        this.skjemainnholdBuilderKladd.medOmsorgspenger(omsorgspenger);
        return this;
    }
    public InntektsmeldingBuilder medArbeidsgiver(String virksomhetsnummer, String kontaktinformasjonTLF){
        this.skjemainnholdBuilderKladd.medArbeidsgiver(virksomhetsnummer, kontaktinformasjonTLF);
        return this;
    }
    public InntektsmeldingBuilder medArbeidsgiver(Arbeidsgiver arbeidsgiver){
        this.skjemainnholdBuilderKladd.medArbeidsgiver(arbeidsgiver);
        return this;
    }
    public InntektsmeldingBuilder medArbeidsgiverPrivat(String arbeidsgiverFnr, String kontaktinformasjonTLF) {
        skjemainnholdBuilderKladd.medArbeidsgiverPrivat(arbeidsgiverFnr, kontaktinformasjonTLF);
        return this;
    }
    public InntektsmeldingBuilder medArbeidsgiverPrivat(ArbeidsgiverPrivat arbeidsgiverPrivat) {
        this.skjemainnholdBuilderKladd.medArbeidsgiverPrivat(arbeidsgiverPrivat);
        return this;
    }
    public InntektsmeldingBuilder medOpphoerAvNaturalytelseListe(BigDecimal belopPrMnd, LocalDate fom, NaturalytelseKodeliste kodelisteNaturalytelse) {
        this.skjemainnholdBuilderKladd.medOpphoerAvNaturalytelseListe(belopPrMnd, fom, kodelisteNaturalytelse);
        return this;
    }
    public InntektsmeldingBuilder medOpphoerAvNaturalytelseListe(OpphoerAvNaturalytelseListe opphoerAvNaturalytelseListe) {
        this.skjemainnholdBuilderKladd.medOpphoerAvNaturalytelseListe(opphoerAvNaturalytelseListe);
        return this;
    }
    public InntektsmeldingBuilder medGjenopptakelseNaturalytelseListe(GjenopptakelseNaturalytelseListe gjenopptakelseNaturalytelseListe) {
        this.skjemainnholdBuilderKladd.medGjenopptakelseNaturalytelseListe(gjenopptakelseNaturalytelseListe);
        return this;
    }
    public InntektsmeldingBuilder medArbeidstakerFNR(String arbeidstakerFNR) {
        this.skjemainnholdBuilderKladd.medArbeidstakerFNR(arbeidstakerFNR);
        return this;
    }
    public InntektsmeldingBuilder medRefusjon(BigDecimal refusjonsBelopPerMnd) {
        this.skjemainnholdBuilderKladd.medRefusjon(refusjonsBelopPerMnd);
        return this;
    }
    public InntektsmeldingBuilder medRefusjon(BigDecimal refusjonsBelopPerMnd,
                                              LocalDate refusjonsOpphordato,
                                              Map<LocalDate, BigDecimal> endringRefusjonMap) {
        skjemainnholdBuilderKladd.medRefusjon(refusjonsBelopPerMnd, refusjonsOpphordato, endringRefusjonMap);
        return this;
    }
    public InntektsmeldingBuilder medRefusjon(BigDecimal refusjonsBelopPerMnd,
                                              LocalDate refusjonsOpphordato,
                                              List<EndringIRefusjon> endringIRefusjonList) {
        this.skjemainnholdBuilderKladd.medRefusjon(refusjonsBelopPerMnd, refusjonsOpphordato, endringIRefusjonList);
        return this;
    }
    public InntektsmeldingBuilder medRefusjon(Refusjon refusjon) {
        this.skjemainnholdBuilderKladd.medRefusjon(refusjon);
        return this;
    }
    public InntektsmeldingBuilder medSykepengerIArbeidsgiverperioden(SykepengerIArbeidsgiverperioden sykepengerIArbeidsgiverperioden) {
        this.skjemainnholdBuilderKladd.medSykepengerIArbeidsgiverperioden(sykepengerIArbeidsgiverperioden);
        return this;
    }
    public InntektsmeldingBuilder medNaerRelasjon(Boolean naerRelasjon) {
        this.skjemainnholdBuilderKladd.medNaerRelasjon(naerRelasjon);
        return this;
    }
    public InntektsmeldingBuilder medAarsakTilInnsending(ÅrsakInnsendingKodeliste aarsakTilInnsending) {
        this.skjemainnholdBuilderKladd.medAarsakTilInnsending(aarsakTilInnsending);
        return this;
    }
    public InntektsmeldingBuilder medStartdatoForeldrepengerperiodenFOM(LocalDate startidspunktForeldrepenger) {
        this.skjemainnholdBuilderKladd.medStartdatoForeldrepengerperiodenFOM(startidspunktForeldrepenger);
        return this;
    }
    public InntektsmeldingBuilder medYtelse(YtelseKodeliste ytelse) {
        this.skjemainnholdBuilderKladd.medYtelse(ytelse);
        return this;
    }
    public InntektsmeldingBuilder medAvsendersystem(Avsendersystem avsendersystem) {
        this.skjemainnholdBuilderKladd.medAvsendersystem(avsendersystem);
        return this;
    }
    public InntektsmeldingBuilder medAvsendersystem(String avsenderSystem, String systemVersjon) {
        this.skjemainnholdBuilderKladd.medAvsendersystem(avsenderSystem, systemVersjon);
        return this;
    }
    public InntektsmeldingBuilder medArbeidsforhold(Arbeidsforhold arbeidsforhold) {
        this.skjemainnholdBuilderKladd.medArbeidsforhold(arbeidsforhold);
        return this;
    }

    public InntektsmeldingBuilder medBeregnetInntekt(BigDecimal beregnetInntektBelop) {
        this.arbeidsforholdBuilderKladd.medBeregnetInntekt(beregnetInntektBelop);
        return this;
    }
    public InntektsmeldingBuilder medÅrsakVedEndring(ÅrsakBeregnetInntektEndringKodeliste aarsakVedEndring){
        this.arbeidsforholdBuilderKladd.medÅrsakVedEndring(aarsakVedEndring);
        return this;
    }
    public InntektsmeldingBuilder medUtsettelse(String årsak, LocalDate periodeFom, LocalDate periodeTom){
        this.arbeidsforholdBuilderKladd.medUtsettelse(årsak, periodeFom, periodeTom);
        return this;
    }
    public InntektsmeldingBuilder medUtsettelse(ÅrsakUtsettelseKodeliste aarsakTilUtsettelse, LocalDate periodeFom, LocalDate periodeTom){
        arbeidsforholdBuilderKladd.medUtsettelse(aarsakTilUtsettelse, periodeFom, periodeTom);
        return this;
    }
    public InntektsmeldingBuilder medUtsettelse(List<UtsettelseAvForeldrepenger> utsettelseAvForeldrepengerList){
        this.arbeidsforholdBuilderKladd.medUtsettelse(utsettelseAvForeldrepengerList);
        return this;
    }
    public InntektsmeldingBuilder medGradering(BigDecimal arbeidsprosent, LocalDate fom, LocalDate tom) {
        arbeidsforholdBuilderKladd.medGradering(arbeidsprosent, fom, tom);
        return this;
    }
    public InntektsmeldingBuilder medGradering(List<GraderingIForeldrepenger> graderingIForeldrepengerList){
        this.arbeidsforholdBuilderKladd.medGradering(graderingIForeldrepengerList);
        return this;
    }
    public InntektsmeldingBuilder medAvtaltFerie(List<Periode> avtaltFerieListeList){
        this.arbeidsforholdBuilderKladd.medAvtaltFerie(avtaltFerieListeList);
        return this;
    }
    public InntektsmeldingBuilder medArbeidsforholdId(String arbeidsforholdId) {
        this.arbeidsforholdBuilderKladd.medArbeidsforholdId(arbeidsforholdId);
        return this;
    }


    public String createInntektesmeldingXML() {
        return createInntektsmeldingXML(this.build());
    }
    private String createInntektsmeldingXML(InntektsmeldingM inntektsmelding) {

        StringWriter sw = new StringWriter();
        try {
            ObjectFactory objectFactory = new ObjectFactory();
            JAXBContext jaxbContext = JAXBContext.newInstance(InntektsmeldingM.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // Prettyprinter output
            jaxbMarshaller.marshal(
                    objectFactory.createMelding(inntektsmelding), sw
            );
            String s = "";
            return sw.toString();
        } catch (JAXBException jaxbe) {
            LOG.error("Error while creating dokumentforsendelse: " + jaxbe.getMessage());
        }
        return null; //TODO: Håndtere på en bedre måte.

    }


    public InntektsmeldingM build() {
        this.skjemainnholdBuilderKladd.medArbeidsforhold(arbeidsforholdBuilderKladd.build());
        this.inntektsmeldingKladd.setSkjemainnhold(skjemainnholdBuilderKladd.build());
        return inntektsmeldingKladd;
    }
}
