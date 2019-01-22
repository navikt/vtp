package no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper;


import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;
import no.seres.xsd.nav.inntektsmelding_m._20181211.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.inntektsmelding.xml.kodeliste._20180702.BegrunnelseIngenEllerRedusertUtbetalingKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.NaturalytelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakBeregnetInntektEndringKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakInnsendingKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakUtsettelseKodeliste;


public class InntektsmeldingBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingBuilder.class);
    InntektsmeldingM inntektsmelding;
    Skjemainnhold skjemainnhold;
    Arbeidsforhold arbeidsforhold;
    Arbeidsgiver arbeidsgiver;
    YtelseKodeliste ytelse;
    Avsendersystem avsendersystem;
    String arbeidstakerFNR;
    String inntektsmeldingID;
    ÅrsakInnsendingKodeliste aarsakTilInnsending;
    Refusjon refusjon;
    LocalDate startdatoForeldrepengeperiodenFOM;
    OpphoerAvNaturalytelseListe opphoerAvNaturalytelseListe;
    GjenopptakelseNaturalytelseListe gjenopptakelseNaturalytelseListe;
    SykepengerIArbeidsgiverperioden sykepengerIArbeidsgiverperioden;
    Boolean naerRelasjon = null;
    PleiepengerPeriodeListe pleiepengerPeriodeListe;
    Omsorgspenger omsorgspenger;
    ArbeidsgiverPrivat arbeidsgiverPrivat;


    public InntektsmeldingM createInntektsmelding() {
        ObjectFactory objectFactory = new ObjectFactory();
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
                    objectFactory.createSkjemainnholdStartdatoForeldrepengeperiode(
                            makeXMLGregorianCalenderFromLocalDate(startdatoForeldrepengeperiodenFOM)
                    ));
        }


        if (this.ytelse != null) {
            skjemainnhold.setYtelse(ytelse.value());
        }

        if (this.avsendersystem != null) {
            skjemainnhold.setAvsendersystem(avsendersystem);
        }

        inntektsmelding.setSkjemainnhold(skjemainnhold);
        return inntektsmelding;
    }


    public InntektsmeldingBuilder(String inntektsmeldingID,
                                  YtelseKodeliste ytelse,
                                  ÅrsakInnsendingKodeliste aarsakTilInnsending,
                                  String arbeidstakerFNR,
                                  LocalDate startdatoForeldrepengeperiodenFOM) {
        this.inntektsmelding = new InntektsmeldingM();
        this.opphoerAvNaturalytelseListe = new OpphoerAvNaturalytelseListe();
        this.inntektsmeldingID = inntektsmeldingID;
        this.ytelse = isNull(ytelse) ? YtelseKodeliste.FORELDREPENGER : ytelse;
        this.aarsakTilInnsending = aarsakTilInnsending;
        this.arbeidstakerFNR = arbeidstakerFNR;
        this.startdatoForeldrepengeperiodenFOM = startdatoForeldrepengeperiodenFOM;
    }

    public InntektsmeldingBuilder(String inntektsmeldingID,
                                  YtelseKodeliste ytelse,
                                  ÅrsakInnsendingKodeliste aarsakTilInnsending,
                                  String arbeidstakerFNR) {
        this.inntektsmelding = new InntektsmeldingM();
        this.opphoerAvNaturalytelseListe = new OpphoerAvNaturalytelseListe();
        this.gjenopptakelseNaturalytelseListe = new GjenopptakelseNaturalytelseListe();
        this.inntektsmeldingID = inntektsmeldingID;
        this.ytelse = isNull(ytelse) ? YtelseKodeliste.FORELDREPENGER : ytelse;
        this.aarsakTilInnsending = aarsakTilInnsending;
        this.arbeidstakerFNR = arbeidstakerFNR;
        this.startdatoForeldrepengeperiodenFOM = null;
    }


    public InntektsmeldingBuilder setAarsakTilInnsending(ÅrsakInnsendingKodeliste aarsakTilInnsending) {
        this.aarsakTilInnsending = aarsakTilInnsending;
        return this;
    }


    public InntektsmeldingBuilder setInntektsmelding(InntektsmeldingM inntektsmelding) {
        this.inntektsmelding = inntektsmelding;
        return this;
    }

    public void setArbeidsgiverPrivat(ArbeidsgiverPrivat arbeidsgiverPrivat) {
        this.arbeidsgiverPrivat = arbeidsgiverPrivat;
    }

    public ArbeidsgiverPrivat createArbeidsgiverPrivat(String arbeidsgiverFnr) {
        ArbeidsgiverPrivat arbeidsgiverPrivat = new ArbeidsgiverPrivat();
        arbeidsgiverPrivat.setArbeidsgiverFnr(arbeidsgiverFnr);
        Kontaktinformasjon kontaktinformasjon = new Kontaktinformasjon();
        kontaktinformasjon.setKontaktinformasjonNavn("Privatus Bedriftus Vtpus");
        kontaktinformasjon.setTelefonnummer("33334444");
        arbeidsgiverPrivat.setKontaktinformasjon(kontaktinformasjon);

        return arbeidsgiverPrivat;
    }

    public Arbeidsforhold getArbeidsforhold() {
        return arbeidsforhold;
    }

    public InntektsmeldingBuilder setArbeidsforhold(Arbeidsforhold arbeidsforhold) {
        this.arbeidsforhold = arbeidsforhold;
        return this;
    }


    public InntektsmeldingBuilder setArbeidsgiver(Arbeidsgiver arbeidsgiver) {
        this.arbeidsgiver = arbeidsgiver;
        return this;
    }


    public InntektsmeldingBuilder setArbeidstakerFNR(String arbeidstakerFNR) {
        this.arbeidstakerFNR = arbeidstakerFNR;
        return this;
    }


    public InntektsmeldingBuilder setInntektsmeldingID(String inntektsmeldingID) {
        this.inntektsmeldingID = inntektsmeldingID;
        return this;
    }


    public InntektsmeldingBuilder setAvsendersystem(Avsendersystem avsendersystem) {
        this.avsendersystem = avsendersystem;
        return this;
    }


    public InntektsmeldingBuilder setRefusjon(Refusjon refusjon) {
        this.refusjon = refusjon;
        return this;
    }

    public InntektsmeldingBuilder setSykepengerIArbeidsgiverperioden(SykepengerIArbeidsgiverperioden sykepengerIArbeidsgiverperioden) {
        this.sykepengerIArbeidsgiverperioden = sykepengerIArbeidsgiverperioden;
        return this;
    }

    public Arbeidsgiver getArbeidsgiver() {
        return arbeidsgiver;
    }

    public InntektsmeldingBuilder setNaaerRelasjon(Boolean naerRelasjon) {
        this.naerRelasjon = naerRelasjon;
        return this;
    }

    public InntektsmeldingBuilder addGradertperiode(BigDecimal arbeidsprosent, LocalDate fom, LocalDate tom) {
        if (null == this.arbeidsforhold.getGraderingIForeldrepengerListe()) {
            ObjectFactory objectFactory = new ObjectFactory();
            arbeidsforhold.setGraderingIForeldrepengerListe(objectFactory.createArbeidsforholdGraderingIForeldrepengerListe(objectFactory.createGraderingIForeldrepengerListe()));
        }

        GraderingIForeldrepenger gradering = createGraderingIForeldrepenger(arbeidsprosent, createPeriode(fom, tom));
        this.arbeidsforhold.getGraderingIForeldrepengerListe().getValue().getGraderingIForeldrepenger().add(gradering);
        return this;
    }


    public InntektsmeldingBuilder setStartdatoForeldrepengeperiodenFOM(LocalDate startdatoForeldrepengeperiodenFOM) {
        this.startdatoForeldrepengeperiodenFOM = startdatoForeldrepengeperiodenFOM;
        return this;
    }

    public OpphoerAvNaturalytelseListe getOpphoerAvNaturalytelsesList() {
        return opphoerAvNaturalytelseListe;
    }

    public GjenopptakelseNaturalytelseListe getGjenopptakelseNaturalytelseListe() {
        return gjenopptakelseNaturalytelseListe;
    }


    public void setOpphoerAvNaturalytelsesList(List<NaturalytelseDetaljer> naturalytelsesList) {
        if (naturalytelsesList != null && naturalytelsesList.size() > 0) {
            ObjectFactory objectFactory = new ObjectFactory();
            OpphoerAvNaturalytelseListe opphoerAvNaturalytelseListe = objectFactory.createOpphoerAvNaturalytelseListe();
            opphoerAvNaturalytelseListe.getOpphoerAvNaturalytelse().addAll(naturalytelsesList);
            this.opphoerAvNaturalytelseListe = opphoerAvNaturalytelseListe;
        }
    }

    public void setGjenopptakelseNaturalytelseList(List<NaturalytelseDetaljer> naturalytelseList) {
        if (naturalytelseList != null && naturalytelseList.size() > 0) {
            ObjectFactory objectFactory = new ObjectFactory();
            GjenopptakelseNaturalytelseListe gjenopptakelseNaturalytelseListe = objectFactory.createGjenopptakelseNaturalytelseListe();
            gjenopptakelseNaturalytelseListe.getNaturalytelseDetaljer().addAll(naturalytelseList);
            this.gjenopptakelseNaturalytelseListe = gjenopptakelseNaturalytelseListe;
        }
    }

    public static Arbeidsgiver createArbeidsgiver(String virksomhetsnummer,
                                                  String kontaktinformasjonTLF) {

        Arbeidsgiver arbeidsgiver = new Arbeidsgiver();
        arbeidsgiver.setVirksomhetsnummer(virksomhetsnummer);

        Kontaktinformasjon kontaktinformasjon = new Kontaktinformasjon();
        kontaktinformasjon.setTelefonnummer(kontaktinformasjonTLF);
        kontaktinformasjon.setKontaktinformasjonNavn("Corpolarsen");

        arbeidsgiver.setKontaktinformasjon(kontaktinformasjon);

        return arbeidsgiver;
    }

    public static Periode createPeriode(LocalDate periodeFom, LocalDate periodeTom) {
        ObjectFactory objectFactory = new ObjectFactory();
        Periode periode = objectFactory.createPeriode();

        periode.setTom(objectFactory.createPeriodeTom(makeXMLGregorianCalenderFromLocalDate(periodeTom)));
        periode.setFom(objectFactory.createPeriodeFom(makeXMLGregorianCalenderFromLocalDate(periodeFom)));

        return periode;
    }


    public static UtsettelseAvForeldrepenger createUtsettelseAvForeldrepenger(ÅrsakUtsettelseKodeliste aarsakTilUtsettelse,
                                                                              Periode periode) {

        ObjectFactory objectFactory = new ObjectFactory();
        UtsettelseAvForeldrepenger utsettelseAvForeldrepenger = objectFactory.createUtsettelseAvForeldrepenger();
        utsettelseAvForeldrepenger.setAarsakTilUtsettelse(objectFactory
                .createUtsettelseAvForeldrepengerAarsakTilUtsettelse(aarsakTilUtsettelse.value())
        );
        utsettelseAvForeldrepenger.setPeriode(objectFactory.createUtsettelseAvForeldrepengerPeriode(periode));
        return utsettelseAvForeldrepenger;
    }


    public static GraderingIForeldrepenger createGraderingIForeldrepenger(BigDecimal arbeidstidIProsent,
                                                                          Periode periode) {

        ObjectFactory objectFactory = new ObjectFactory();

        GraderingIForeldrepenger graderingIForeldrepenger = new GraderingIForeldrepenger();
        graderingIForeldrepenger.setPeriode(objectFactory.createGraderingIForeldrepengerPeriode(periode));
        graderingIForeldrepenger.setArbeidstidprosent(objectFactory.createGraderingIForeldrepengerArbeidstidprosent(arbeidstidIProsent.toBigInteger()));
        return graderingIForeldrepenger;

    }

    public static Avsendersystem createAvsendersystem(String avsenderSystem,
                                                      String systemVersjon) {

        Avsendersystem avsendersystem = new Avsendersystem();
        avsendersystem.setSystemnavn(avsenderSystem);
        avsendersystem.setSystemversjon(systemVersjon);

        return avsendersystem;
    }

    public static SykepengerIArbeidsgiverperioden createSykepengerIArbeidsgiverperioden(BigDecimal bruttoUtbetalt,
                                                                                        List<Periode> arbeidsgiverperiodelist,
                                                                                        BegrunnelseIngenEllerRedusertUtbetalingKodeliste begrunnelse
    ) {
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

    public static ArbeidsgiverperiodeListe createArbeidsgiverperiodeListe(List<Periode> perioder) {
        ArbeidsgiverperiodeListe arbeidsgiverperiodeListe = new ArbeidsgiverperiodeListe();
        arbeidsgiverperiodeListe.getArbeidsgiverperiode().addAll(perioder);
        return arbeidsgiverperiodeListe;
    }

    public static Arbeidsforhold createArbeidsforhold(String arbeidsforholdId,
                                                      ÅrsakBeregnetInntektEndringKodeliste aarsakVedEndring,
                                                      BigDecimal beregnetInntektBelop,
                                                      List<UtsettelseAvForeldrepenger> utsettelseAvForeldrepengerList,
                                                      List<GraderingIForeldrepenger> graderingIForeldrepengerList,
                                                      List<Periode> avtaltFerieListeList) {


        ObjectFactory objectFactory = new ObjectFactory();
        Arbeidsforhold arbeidsforhold = objectFactory.createArbeidsforhold();

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

        if (arbeidsforholdId != null && arbeidsforholdId.length() > 0) {
            arbeidsforhold.setArbeidsforholdId(objectFactory
                    .createArbeidsforholdArbeidsforholdId(arbeidsforholdId)
            );

        }

        if (beregnetInntektBelop != null) {
            Inntekt inntekt = objectFactory.createInntekt();
            inntekt.setBeloep(
                    objectFactory.createInntektBeloep(
                            beregnetInntektBelop));

            if (aarsakVedEndring != null) {
                inntekt.setAarsakVedEndring(
                        objectFactory.createInntektAarsakVedEndring(aarsakVedEndring.value()));
            }
            arbeidsforhold.setBeregnetInntekt(objectFactory.createArbeidsforholdBeregnetInntekt(inntekt));
        }
        return arbeidsforhold;

    }


    public static Refusjon createRefusjon(BigDecimal refusjonsBelopPerMnd,
                                          LocalDate refusjonsOpphordato,
                                          List<EndringIRefusjon> endringIRefusjonList) {

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
            refusjon.setRefusjonsopphoersdato(objectFactory.createRefusjonRefusjonsopphoersdato(
                    makeXMLGregorianCalenderFromLocalDate(refusjonsOpphordato)
            ));
        }
        return refusjon;
    }


    public static NaturalytelseDetaljer createNaturalytelseDetaljer(BigDecimal belopPrMnd,
                                                                    LocalDate fom,
                                                                    NaturalytelseKodeliste kodelisteNaturalytelse) {
        ObjectFactory objectFactory = new ObjectFactory();
        NaturalytelseDetaljer naturalytelseDetaljer = objectFactory.createNaturalytelseDetaljer();
        naturalytelseDetaljer.setBeloepPrMnd(objectFactory.createNaturalytelseDetaljerBeloepPrMnd(belopPrMnd));
        naturalytelseDetaljer.setFom(objectFactory.createNaturalytelseDetaljerFom(makeXMLGregorianCalenderFromLocalDate(fom)));
        naturalytelseDetaljer.setNaturalytelseType(objectFactory.createNaturalytelseDetaljerNaturalytelseType(kodelisteNaturalytelse.value()));

        return naturalytelseDetaljer;

    }

    public static EndringIRefusjon createEndringIRefusjon(LocalDate endringsdato, BigDecimal refusjonsbeloepPrMnd) {
        ObjectFactory objectFactory = new ObjectFactory();
        EndringIRefusjon endringIRefusjon = objectFactory.createEndringIRefusjon();
        endringIRefusjon.setEndringsdato(objectFactory.createEndringIRefusjonEndringsdato(makeXMLGregorianCalenderFromLocalDate(endringsdato)));
        endringIRefusjon.setRefusjonsbeloepPrMnd(objectFactory.createEndringIRefusjonRefusjonsbeloepPrMnd(refusjonsbeloepPrMnd));
        return endringIRefusjon;
    }

    public String createInntektesmeldingXML() {
        return createInntektsmeldingXML(this.createInntektsmelding());
    }


    public static String createInntektsmeldingXML(InntektsmeldingM inntektsmelding) {

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


    private static XMLGregorianCalendar makeXMLGregorianCalenderFromLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        try {
            XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(localDate.toString());
            return xcal;
        } catch (DatatypeConfigurationException dtce) {
            LOG.warn("Error while handling date: " + dtce.getMessage());
        }
        return null; //TODO: Håndtere på en bedre måte.
    }

    private boolean isNull(Object o) {
        return o == null;
    }

    public static InntektsmeldingBuilder createDefaultForeldrepenger(Integer beløp, String fnr, String orgnummer, LocalDate startDatoForeldrepenger) {
        InntektsmeldingBuilder builder = new InntektsmeldingBuilder(
                UUID.randomUUID().toString().substring(0, 7),
                YtelseKodeliste.FORELDREPENGER,
                ÅrsakInnsendingKodeliste.NY.NY,
                fnr,
                startDatoForeldrepenger);
        builder.setArbeidsgiver(InntektsmeldingBuilder.createArbeidsgiver(orgnummer, "41925090"));
        builder.setAvsendersystem(InntektsmeldingBuilder.createAvsendersystem("FS22", "1.0"));
        builder.setArbeidsforhold(InntektsmeldingBuilder.createArbeidsforhold(
                "", //TODO arbeidsforhold id
                null,
                new BigDecimal(beløp),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()));
        return builder;
    }

    public static InntektsmeldingBuilder createDefaultSykepenger(Integer beløp, String fnr, String orgnummer, String arbeidsgiverFnr, int bruttoUtbetalt, LocalDate sykepengerFra, LocalDate sykepengerTil) {
        InntektsmeldingBuilder builder = new InntektsmeldingBuilder(
                UUID.randomUUID().toString().substring(0, 7),
                YtelseKodeliste.SYKEPENGER,
                ÅrsakInnsendingKodeliste.NY.NY,
                fnr);

        List<Periode> perioder = new ArrayList<>();
        perioder.add(InntektsmeldingBuilder.createPeriode(sykepengerFra, sykepengerTil));

        builder.setArbeidstakerFNR(fnr);
        builder.setSykepengerIArbeidsgiverperioden(InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                new BigDecimal(bruttoUtbetalt),
                perioder,
                BegrunnelseIngenEllerRedusertUtbetalingKodeliste.LOVLIG_FRAVAER));
        builder.setArbeidsgiver(InntektsmeldingBuilder.createArbeidsgiver(orgnummer, "41925090"));
        builder.setAvsendersystem(InntektsmeldingBuilder.createAvsendersystem("FS32", "1.0"));
        builder.setArbeidsforhold(InntektsmeldingBuilder.createArbeidsforhold(
                "", //TODO arbeidsforhold id
                null,
                new BigDecimal(beløp),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()));
        return builder;
    }

    public static InntektsmeldingBuilder createDefaultOMS(Integer beløp, String fnr, String orgnummer, LocalDate startDatoForeldrepenger) {
        InntektsmeldingBuilder builder = new InntektsmeldingBuilder(UUID.randomUUID().toString().substring(0, 7),
                YtelseKodeliste.FORELDREPENGER,
                ÅrsakInnsendingKodeliste.NY.NY,
                fnr,
                startDatoForeldrepenger);
        return builder;
    }

    public InntektsmeldingBuilder addUtsettelseperiode(String årsak, LocalDate fom, LocalDate tom) {
        if (null == this.arbeidsforhold.getUtsettelseAvForeldrepengerListe()) {
            ObjectFactory objectFactory = new ObjectFactory();
            arbeidsforhold.setUtsettelseAvForeldrepengerListe(objectFactory.createArbeidsforholdUtsettelseAvForeldrepengerListe(objectFactory.createUtsettelseAvForeldrepengerListe()));
        }

        UtsettelseAvForeldrepenger utsettelse = createUtsettelseAvForeldrepenger(map(årsak), createPeriode(fom, tom));
        this.arbeidsforhold.getUtsettelseAvForeldrepengerListe().getValue().getUtsettelseAvForeldrepenger().add(utsettelse);
        return this;
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

    public PleiepengerPeriodeListe getPleiepengerPeriodeListe() {
        return pleiepengerPeriodeListe;
    }

    public void setPleiepengerPeriodeListe(PleiepengerPeriodeListe pleiepengerPeriodeListe) {
        this.pleiepengerPeriodeListe = pleiepengerPeriodeListe;
    }

    public Omsorgspenger getOmsorgspenger() {
        return omsorgspenger;
    }

    public void setOmsorgspenger(Omsorgspenger omsorgspenger) {
        this.omsorgspenger = omsorgspenger;
    }


    public DelvisFravaer createDelvisFravaer(LocalDate dato, BigDecimal antallTimer) {
        ObjectFactory objectFactory = new ObjectFactory();
        DelvisFravaer delvisFravaer = new DelvisFravaer();
        delvisFravaer.setDato(objectFactory.createDelvisFravaerDato(DateUtil.convertToXMLGregorianCalendar(dato)));
        delvisFravaer.setTimer(objectFactory.createDelvisFravaerTimer(antallTimer));

        return delvisFravaer;
    }

    public DelvisFravaersListe createDelvisFravaersListeForOmsorgspenger(List<DelvisFravaer> delvisFravaer) {
        ObjectFactory objectFactory = new ObjectFactory();
        DelvisFravaersListe delvisFravaersListe = objectFactory.createDelvisFravaersListe();
        delvisFravaer.forEach(df -> {
            delvisFravaersListe.getDelvisFravaer().add(df);
        });

        return delvisFravaersListe;
    }

    public Periode createFravaersPeriode(LocalDate fom, LocalDate tom) {
        ObjectFactory objectFactory = new ObjectFactory();
        Periode periode = new Periode();
        periode.setFom(objectFactory.createPeriodeFom(DateUtil.convertToXMLGregorianCalendar(fom)));
        periode.setTom(objectFactory.createPeriodeFom(DateUtil.convertToXMLGregorianCalendar(tom)));
        return periode;
    }

    public JAXBElement<FravaersPeriodeListe> createFravaersPeriodeListeForOmsorgspenger(List<Periode> perioder) {
        ObjectFactory objectFactory = new ObjectFactory();
        FravaersPeriodeListe fravaersPeriodeListe = new FravaersPeriodeListe();
        perioder.forEach(p -> {
            fravaersPeriodeListe.getFravaerPeriode().add(p);
        });

        return objectFactory.createOmsorgspengerFravaersPerioder(fravaersPeriodeListe);
    }

    public Omsorgspenger createOmsorgspenger(DelvisFravaersListe delvisFravaersListe, FravaersPeriodeListe fravaersPeriodeListe, Boolean harUtbetaltPliktigeDager) {
        ObjectFactory objectFactory = new ObjectFactory();
        Omsorgspenger omsorgspenger = new Omsorgspenger();

        if (null != delvisFravaersListe && delvisFravaersListe.getDelvisFravaer().size() > 0) {
            omsorgspenger.setDelvisFravaersListe(objectFactory.createOmsorgspengerDelvisFravaersListe(delvisFravaersListe));
        }

        if (null != fravaersPeriodeListe && fravaersPeriodeListe.getFravaerPeriode().size() > 0) {
            omsorgspenger.setFravaersPerioder(objectFactory.createOmsorgspengerFravaersPerioder(fravaersPeriodeListe));
        }

        omsorgspenger.setHarUtbetaltPliktigeDager(objectFactory.createOmsorgspengerHarUtbetaltPliktigeDager(harUtbetaltPliktigeDager));

        return omsorgspenger;
    }

    //todo lag pleiepenger etter delvisfravær og fravær i omsorg

}
