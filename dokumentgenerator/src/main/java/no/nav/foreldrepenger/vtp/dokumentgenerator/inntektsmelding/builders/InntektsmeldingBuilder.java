package no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.builders;

import no.nav.inntektsmelding.xml.kodeliste._20180702.BegrunnelseIngenEllerRedusertUtbetalingKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.NaturalytelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakInnsendingKodeliste;
import no.seres.xsd.nav.inntektsmelding_m._20181211.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class InntektsmeldingBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingBuilder.class);
    private InntektsmeldingM inntektsmeldingKladd;
    public ArbeidsforholdBuilder arbeidsforholdBuilderKladd;
    private ObjectFactory objectFactory;

    private ÅrsakInnsendingKodeliste aarsakTilInnsending;
    private YtelseKodeliste ytelse;
    private LocalDate startdatoForeldrepengeperiodenFOM;
    private Skjemainnhold skjemainnhold;
    private PleiepengerPeriodeListe pleiepengerPeriodeListe;
    private Omsorgspenger omsorgspenger;
    private ArbeidsgiverPrivat arbeidsgiverPrivat;
    private OpphoerAvNaturalytelseListe opphoerAvNaturalytelseListe;
    private GjenopptakelseNaturalytelseListe gjenopptakelseNaturalytelseListe;
    private Arbeidsforhold arbeidsforhold;
    private Arbeidsgiver arbeidsgiver;
    private String arbeidstakerFNR;
    private Refusjon refusjon;
    private SykepengerIArbeidsgiverperioden sykepengerIArbeidsgiverperioden;
    private Boolean naerRelasjon = null;
    private Avsendersystem avsendersystem;

    public InntektsmeldingBuilder(ArbeidsforholdBuilder arbeidsforholdBuilderKladd, String arbeidstakerFNR) {
        inntektsmeldingKladd = new InntektsmeldingM();
        objectFactory = new ObjectFactory();
        this.arbeidsforholdBuilderKladd = arbeidsforholdBuilderKladd;
        medArbeidstakerFNR(arbeidstakerFNR);
        medStartdatoForeldrepengerperiodenFOM(startdatoForeldrepengeperiodenFOM);
    }
    public InntektsmeldingBuilder medPleiepengerPeriodeListe(PleiepengerPeriodeListe pleiepengerPeriodeListe) {
        this.pleiepengerPeriodeListe = pleiepengerPeriodeListe;
        return this;
    }
    public InntektsmeldingBuilder medOmsorgspenger(Omsorgspenger omsorgspenger) {
        this.omsorgspenger = omsorgspenger;
        return this;
    }
    public InntektsmeldingBuilder medArbeidsgiver(String virksomhetsnummer, String kontaktinformasjonTLF){
        this.arbeidsgiver = createArbeidsgiver(virksomhetsnummer, kontaktinformasjonTLF);
        return this;
    }
    public InntektsmeldingBuilder medArbeidsgiver(Arbeidsgiver arbeidsgiver){
        this.arbeidsgiver = arbeidsgiver;
        return this;
    }
    public InntektsmeldingBuilder medArbeidsgiverPrivat(String arbeidsgiverFnr, String kontaktinformasjonTLF) {
        medArbeidsgiverPrivat(createArbeidsgiverPrivat(arbeidsgiverFnr, kontaktinformasjonTLF));
        return this;
    }
    public InntektsmeldingBuilder medArbeidsgiverPrivat(ArbeidsgiverPrivat arbeidsgiverPrivat) {
        this.arbeidsgiverPrivat = arbeidsgiverPrivat;
        return this;
    }
    public InntektsmeldingBuilder medOpphoerAvNaturalytelseListe(BigDecimal belopPrMnd, LocalDate fom, NaturalytelseKodeliste kodelisteNaturalytelse) {
        if (this.opphoerAvNaturalytelseListe == null) {
            opphoerAvNaturalytelseListe = new OpphoerAvNaturalytelseListe();
        }
        this.opphoerAvNaturalytelseListe.getOpphoerAvNaturalytelse()
                .add(createNaturalytelseDetaljer(belopPrMnd, fom, kodelisteNaturalytelse));
        return this;
    }
    public InntektsmeldingBuilder medOpphoerAvNaturalytelseListe(OpphoerAvNaturalytelseListe opphoerAvNaturalytelseListe) {
        this.opphoerAvNaturalytelseListe = opphoerAvNaturalytelseListe;
        return this;
    }
    public InntektsmeldingBuilder medGjenopptakelseNaturalytelseListe(GjenopptakelseNaturalytelseListe gjenopptakelseNaturalytelseListe) {
        this.gjenopptakelseNaturalytelseListe = gjenopptakelseNaturalytelseListe;
        return this;
    }
    public InntektsmeldingBuilder medArbeidstakerFNR(String arbeidstakerFNR) {
        this.arbeidstakerFNR = arbeidstakerFNR;
        return this;
    }
    //TODO RefusjonsBuilder
    public InntektsmeldingBuilder medRefusjon(BigDecimal refusjonsBelopPerMnd) {
        this.refusjon = createRefusjon(refusjonsBelopPerMnd, null, null);
        return this;
    }
    public InntektsmeldingBuilder medRefusjon(BigDecimal refusjonsBelopPerMnd,
                                              LocalDate refusjonsOpphordato,
                                              Map<LocalDate, BigDecimal> endringRefusjonMap) {
        medRefusjon(refusjonsBelopPerMnd, refusjonsOpphordato,
                endringRefusjonMap.entrySet().stream().map(
                        entry -> createEndringIRefusjon(entry.getKey(), entry.getValue())
                ).collect(Collectors.toList()));
        return this;
    }
    public InntektsmeldingBuilder medRefusjon(BigDecimal refusjonsBelopPerMnd,
                                              LocalDate refusjonsOpphordato,
                                              List<EndringIRefusjon> endringIRefusjonList) {
        this.refusjon = createRefusjon(refusjonsBelopPerMnd, refusjonsOpphordato, endringIRefusjonList);
        return this;
    }
    public InntektsmeldingBuilder medRefusjon(Refusjon refusjon) {
        this.refusjon = refusjon;
        return this;
    }
    public InntektsmeldingBuilder medSykepengerIArbeidsgiverperioden(SykepengerIArbeidsgiverperioden sykepengerIArbeidsgiverperioden) {
        this.sykepengerIArbeidsgiverperioden = sykepengerIArbeidsgiverperioden;
        return this;
    }
    public InntektsmeldingBuilder medNaerRelasjon(Boolean naerRelasjon) {
        this.naerRelasjon = naerRelasjon;
        return this;
    }
    public InntektsmeldingBuilder medAarsakTilInnsending(ÅrsakInnsendingKodeliste aarsakTilInnsending) {
        this.aarsakTilInnsending = aarsakTilInnsending;
        return this;
    }
    public InntektsmeldingBuilder medStartdatoForeldrepengerperiodenFOM(LocalDate startidspunktForeldrepenger) {
        this.startdatoForeldrepengeperiodenFOM = startidspunktForeldrepenger;
        objectFactory.createSkjemainnholdStartdatoForeldrepengeperiode(startidspunktForeldrepenger);
        return this;
    }
    public InntektsmeldingBuilder medYtelse(YtelseKodeliste ytelse) {
        this.ytelse = ytelse;
        return this;

    }
    public InntektsmeldingBuilder medAvsendersystem(Avsendersystem avsendersystem) {
        this.avsendersystem = avsendersystem;
        return this;

    }
    public InntektsmeldingBuilder medAvsendersystem(String avsenderSystem, String systemVersjon) {
        this.avsendersystem = createAvsendersystem(avsenderSystem, systemVersjon);
        return this;

    }


    private Avsendersystem createAvsendersystem(String avsenderSystem, String systemVersjon) {
        Avsendersystem avsendersystem = new Avsendersystem();
        avsendersystem.setSystemnavn(avsenderSystem);
        avsendersystem.setSystemversjon(systemVersjon);

        return avsendersystem;
    }
    private Arbeidsgiver createArbeidsgiver(String virksomhetsnummer, String kontaktinformasjonTLF) {
        Arbeidsgiver arbeidsgiver = new Arbeidsgiver();
        arbeidsgiver.setVirksomhetsnummer(virksomhetsnummer);
        Kontaktinformasjon kontaktinformasjon = new Kontaktinformasjon();
        kontaktinformasjon.setTelefonnummer(kontaktinformasjonTLF);
        kontaktinformasjon.setKontaktinformasjonNavn("Corpolarsen");
        arbeidsgiver.setKontaktinformasjon(kontaktinformasjon);
        return arbeidsgiver;
    }

    private ArbeidsgiverPrivat createArbeidsgiverPrivat(String arbeidsgiverFnr, String kontaktinformasjonTLF) {
        ArbeidsgiverPrivat arbeidsgiver = new ArbeidsgiverPrivat();
        arbeidsgiver.setArbeidsgiverFnr(arbeidsgiverFnr);
        Kontaktinformasjon kontaktinformasjon = new Kontaktinformasjon();
        kontaktinformasjon.setTelefonnummer(kontaktinformasjonTLF);
        kontaktinformasjon.setKontaktinformasjonNavn("Corpolarsen");
        arbeidsgiver.setKontaktinformasjon(kontaktinformasjon);
        return arbeidsgiver;
    }
    private Refusjon createRefusjon(BigDecimal refusjonsBelopPerMnd, LocalDate refusjonsOpphordato, List<EndringIRefusjon> endringIRefusjonList) {

        ObjectFactory objectFactory = new ObjectFactory();
        Refusjon refusjon = objectFactory.createRefusjon();

        refusjon.setRefusjonsbeloepPrMnd(
                objectFactory.createRefusjonRefusjonsbeloepPrMnd(refusjonsBelopPerMnd));

        if (endringIRefusjonList != null && endringIRefusjonList.size() > 0) {
            EndringIRefusjonsListe endringIRefusjonsListe = objectFactory.createEndringIRefusjonsListe();
            endringIRefusjonsListe.getEndringIRefusjon().addAll(endringIRefusjonList);
            refusjon.setEndringIRefusjonListe(
                    objectFactory.createRefusjonEndringIRefusjonListe(endringIRefusjonsListe)
            );
        }


        if (refusjonsOpphordato != null) {
            refusjon.setRefusjonsopphoersdato(objectFactory.createRefusjonRefusjonsopphoersdato(                   refusjonsOpphordato));
        }
        return refusjon;
    }
    private EndringIRefusjon createEndringIRefusjon(LocalDate endringsdato, BigDecimal refusjonsbeloepPrMnd) {
        ObjectFactory objectFactory = new ObjectFactory();
        EndringIRefusjon endringIRefusjon = objectFactory.createEndringIRefusjon();
        endringIRefusjon.setEndringsdato(objectFactory.createEndringIRefusjonEndringsdato(endringsdato));
        endringIRefusjon.setRefusjonsbeloepPrMnd(objectFactory.createEndringIRefusjonRefusjonsbeloepPrMnd(refusjonsbeloepPrMnd));
        return endringIRefusjon;
    }
    private NaturalytelseDetaljer createNaturalytelseDetaljer(BigDecimal belopPrMnd, LocalDate fom, NaturalytelseKodeliste kodelisteNaturalytelse) {
        ObjectFactory objectFactory = new ObjectFactory();
        NaturalytelseDetaljer naturalytelseDetaljer = objectFactory.createNaturalytelseDetaljer();
        naturalytelseDetaljer.setBeloepPrMnd(objectFactory.createNaturalytelseDetaljerBeloepPrMnd(belopPrMnd));
        naturalytelseDetaljer.setFom(objectFactory.createNaturalytelseDetaljerFom(fom));
        naturalytelseDetaljer.setNaturalytelseType(objectFactory.createNaturalytelseDetaljerNaturalytelseType(kodelisteNaturalytelse.value()));

        return naturalytelseDetaljer;

    }
    private SykepengerIArbeidsgiverperioden createSykepengerIArbeidsgiverperioden(BigDecimal bruttoUtbetalt, List<Periode> arbeidsgiverperiodelist, BegrunnelseIngenEllerRedusertUtbetalingKodeliste begrunnelse) {
        ObjectFactory objectFactory = new ObjectFactory();
        SykepengerIArbeidsgiverperioden sykepengerIArbeidsgiverperioden = new SykepengerIArbeidsgiverperioden();
        sykepengerIArbeidsgiverperioden.setBruttoUtbetalt(objectFactory.createSykepengerIArbeidsgiverperiodenBruttoUtbetalt(bruttoUtbetalt));
        if (begrunnelse != null) {
            sykepengerIArbeidsgiverperioden.setBegrunnelseForReduksjonEllerIkkeUtbetalt(objectFactory.createSykepengerIArbeidsgiverperiodenBegrunnelseForReduksjonEllerIkkeUtbetalt(begrunnelse.value()));
        }
        if (arbeidsgiverperiodelist != null && arbeidsgiverperiodelist.size() > 0) {
            ArbeidsgiverperiodeListe arbeidsgiverperiodeListe = objectFactory.createArbeidsgiverperiodeListe();
            arbeidsgiverperiodeListe.getArbeidsgiverperiode().addAll(arbeidsgiverperiodelist);
            sykepengerIArbeidsgiverperioden.setArbeidsgiverperiodeListe(
                    objectFactory.createSykepengerIArbeidsgiverperiodenArbeidsgiverperiodeListe(arbeidsgiverperiodeListe));
        }
        return sykepengerIArbeidsgiverperioden;
    }
    private Omsorgspenger createOmsorgspenger(DelvisFravaersListe delvisFravaersListe, FravaersPeriodeListe fravaersPeriodeListe, Boolean harUtbetaltPliktigeDager) {
        ObjectFactory objectFactory = new ObjectFactory();
        Omsorgspenger omsorgspenger = objectFactory.createOmsorgspenger();

        if (null != delvisFravaersListe && delvisFravaersListe.getDelvisFravaer().size() > 0) {
            omsorgspenger.setDelvisFravaersListe(objectFactory.createOmsorgspengerDelvisFravaersListe(delvisFravaersListe));
        }

        if (null != fravaersPeriodeListe && fravaersPeriodeListe.getFravaerPeriode().size() > 0) {
            omsorgspenger.setFravaersPerioder(objectFactory.createOmsorgspengerFravaersPerioder(fravaersPeriodeListe));
        }

        omsorgspenger.setHarUtbetaltPliktigeDager(objectFactory.createOmsorgspengerHarUtbetaltPliktigeDager(harUtbetaltPliktigeDager));

        return omsorgspenger;
    }
    private PleiepengerPeriodeListe createPleiepenger(List<Periode> perioder) {
        ObjectFactory objectFactory = new ObjectFactory();
        PleiepengerPeriodeListe pleiepengerPeriodeListe = new PleiepengerPeriodeListe();
        Periode periode = new Periode();
        perioder.forEach(pp -> {
            pleiepengerPeriodeListe.getPeriode().add(pp);

        });

        return pleiepengerPeriodeListe;
    }
    private DelvisFravaer createDelvisFravaer(LocalDate dato, BigDecimal antallTimer) {
        ObjectFactory objectFactory = new ObjectFactory();
        DelvisFravaer delvisFravaer = objectFactory.createDelvisFravaer();
        delvisFravaer.setDato(objectFactory.createDelvisFravaerDato(dato));
        delvisFravaer.setTimer(objectFactory.createDelvisFravaerTimer(antallTimer));

        return delvisFravaer;
    }
    private DelvisFravaersListe createDelvisFravaersListeForOmsorgspenger(List<DelvisFravaer> delvisFravaer) {
        ObjectFactory objectFactory = new ObjectFactory();
        DelvisFravaersListe delvisFravaersListe = objectFactory.createDelvisFravaersListe();
        delvisFravaer.forEach(df -> {
            delvisFravaersListe.getDelvisFravaer().add(df);
        });

        return delvisFravaersListe;
    }

    private Periode createInntektsmeldingPeriode(LocalDate fom, LocalDate tom) {
        ObjectFactory objectFactory = new ObjectFactory();
        Periode periode = objectFactory.createPeriode();
        periode.setFom(objectFactory.createPeriodeFom(fom));
        periode.setTom(objectFactory.createPeriodeTom(tom));
        return periode;
    }
    private JAXBElement<FravaersPeriodeListe> createFravaersPeriodeListeForOmsorgspenger(List<Periode> perioder) {
        ObjectFactory objectFactory = new ObjectFactory();

        FravaersPeriodeListe fravaersPeriodeListe = objectFactory.createFravaersPeriodeListe();

        perioder.forEach(p -> {
            fravaersPeriodeListe.getFravaerPeriode().add(p);
        });

        return objectFactory.createOmsorgspengerFravaersPerioder(fravaersPeriodeListe);
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

    public Skjemainnhold buildSkjemainnhold() {
        if (avsendersystem == null) {
            medAvsendersystem(createAvsendersystem("FS22", "1.0"));
        }
        if (arbeidsgiver == null && arbeidsgiverPrivat == null) {
            throw new NullPointerException("Inntektsmelding må ha arbeidsgiver eller arbeidsgiverPrivat");
        }
        Objects.requireNonNull(ytelse, "Ytelse kan ikke være null");
        Objects.requireNonNull(aarsakTilInnsending, "Årsak til innsending kan ikke være null");

        skjemainnhold = new Skjemainnhold();
        if (this.pleiepengerPeriodeListe != null) {
            skjemainnhold.setPleiepengerPerioder(objectFactory.createSkjemainnholdPleiepengerPerioder(pleiepengerPeriodeListe));
        }
        if (this.omsorgspenger != null) {
            skjemainnhold.setOmsorgspenger(objectFactory.createSkjemainnholdOmsorgspenger(omsorgspenger));
        }
        if (this.arbeidsgiverPrivat != null) {
            skjemainnhold.setArbeidsgiverPrivat(objectFactory.createSkjemainnholdArbeidsgiverPrivat(this.arbeidsgiverPrivat));
        }
        if (this.opphoerAvNaturalytelseListe != null) {
            skjemainnhold.setOpphoerAvNaturalytelseListe(objectFactory.createSkjemainnholdOpphoerAvNaturalytelseListe(this.opphoerAvNaturalytelseListe));
        }
        if (this.gjenopptakelseNaturalytelseListe != null) {
            skjemainnhold.setGjenopptakelseNaturalytelseListe(objectFactory.createSkjemainnholdGjenopptakelseNaturalytelseListe(this.gjenopptakelseNaturalytelseListe));
        }
        if (this.arbeidsforhold != null) {
            skjemainnhold.setArbeidsforhold(objectFactory.createSkjemainnholdArbeidsforhold(this.arbeidsforhold));
        }
        if (this.arbeidsgiver != null) {
            skjemainnhold.setArbeidsgiver(objectFactory.createSkjemainnholdArbeidsgiver(arbeidsgiver));
        }
        if (this.arbeidstakerFNR != null && this.arbeidstakerFNR.length() > 0) {
            skjemainnhold.setArbeidstakerFnr(arbeidstakerFNR);
        }
        if (this.refusjon != null) {
            skjemainnhold.setRefusjon(objectFactory.createSkjemainnholdRefusjon(refusjon));
        }
        if (this.sykepengerIArbeidsgiverperioden != null) {
            skjemainnhold.setSykepengerIArbeidsgiverperioden(
                    objectFactory.createSkjemainnholdSykepengerIArbeidsgiverperioden(
                            this.sykepengerIArbeidsgiverperioden));
        }
        if (this.naerRelasjon != null) {
            skjemainnhold.setNaerRelasjon(this.naerRelasjon);
        }
        if (this.aarsakTilInnsending != null) {
            skjemainnhold.setAarsakTilInnsending(this.aarsakTilInnsending.value());
        }
        if (this.startdatoForeldrepengeperiodenFOM != null) {
            skjemainnhold.setStartdatoForeldrepengeperiode(
                    objectFactory.createSkjemainnholdStartdatoForeldrepengeperiode(startdatoForeldrepengeperiodenFOM));
        }
        if (this.ytelse != null) {
            skjemainnhold.setYtelse(ytelse.value());
        }
        if (this.avsendersystem != null) {
            skjemainnhold.setAvsendersystem(avsendersystem);
        }
        else {
            medAvsendersystem(createAvsendersystem("FS22", "1.0"));
        }

        return skjemainnhold;

    }
    public InntektsmeldingM build() {
        this.arbeidsforhold = arbeidsforholdBuilderKladd.build();
        this.inntektsmeldingKladd.setSkjemainnhold(buildSkjemainnhold());
        return inntektsmeldingKladd;
    }
}
