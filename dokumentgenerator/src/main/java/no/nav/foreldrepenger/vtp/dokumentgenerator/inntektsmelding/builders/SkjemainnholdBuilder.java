package no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.builders;

import no.nav.inntektsmelding.xml.kodeliste._20180702.NaturalytelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakInnsendingKodeliste;
import no.seres.xsd.nav.inntektsmelding_m._20181211.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SkjemainnholdBuilder {

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

    public SkjemainnholdBuilder() {
        objectFactory = new ObjectFactory();
    }
    public SkjemainnholdBuilder medPleiepengerPeriodeListe(PleiepengerPeriodeListe pleiepengerPeriodeListe) {
        this.pleiepengerPeriodeListe = pleiepengerPeriodeListe;
        return this;
    }
    public SkjemainnholdBuilder medOmsorgspenger(Omsorgspenger omsorgspenger) {
        this.omsorgspenger = omsorgspenger;
        return this;
    }
    public SkjemainnholdBuilder medArbeidsgiver(String virksomhetsnummer, String kontaktinformasjonTLF){
        this.arbeidsgiver = createArbeidsgiver(virksomhetsnummer, kontaktinformasjonTLF);
        return this;
    }
    public SkjemainnholdBuilder medArbeidsgiver(Arbeidsgiver arbeidsgiver){
        this.arbeidsgiver = arbeidsgiver;
        return this;
    }
    public SkjemainnholdBuilder medArbeidsgiverPrivat(String arbeidsgiverFnr, String kontaktinformasjonTLF) {
        medArbeidsgiverPrivat(createArbeidsgiverPrivat(arbeidsgiverFnr, kontaktinformasjonTLF));
        return this;
    }
    public SkjemainnholdBuilder medArbeidsgiverPrivat(ArbeidsgiverPrivat arbeidsgiverPrivat) {
        this.arbeidsgiverPrivat = arbeidsgiverPrivat;
        return this;
    }
    public SkjemainnholdBuilder medOpphoerAvNaturalytelseListe(BigDecimal belopPrMnd, LocalDate fom, NaturalytelseKodeliste kodelisteNaturalytelse) {
        if (this.opphoerAvNaturalytelseListe == null) {
            opphoerAvNaturalytelseListe = new OpphoerAvNaturalytelseListe();
        }
        this.opphoerAvNaturalytelseListe.getOpphoerAvNaturalytelse()
                .add(createNaturalytelseDetaljer(belopPrMnd, fom, kodelisteNaturalytelse));
        return this;
    }
    public SkjemainnholdBuilder medOpphoerAvNaturalytelseListe(OpphoerAvNaturalytelseListe opphoerAvNaturalytelseListe) {
        this.opphoerAvNaturalytelseListe = opphoerAvNaturalytelseListe;
        return this;
    }
    public SkjemainnholdBuilder medGjenopptakelseNaturalytelseListe(GjenopptakelseNaturalytelseListe gjenopptakelseNaturalytelseListe) {
        this.gjenopptakelseNaturalytelseListe = gjenopptakelseNaturalytelseListe;
        return this;
    }
    public SkjemainnholdBuilder medArbeidstakerFNR(String arbeidstakerFNR) {
        this.arbeidstakerFNR = arbeidstakerFNR;
        return this;
    }
    public SkjemainnholdBuilder medRefusjon(BigDecimal refusjonsBelopPerMnd) {
        this.refusjon = createRefusjon(refusjonsBelopPerMnd, null, null);
        return this;
    }
    public SkjemainnholdBuilder medRefusjon(BigDecimal refusjonsBelopPerMnd,
                                              LocalDate refusjonsOpphordato,
                                              Map<LocalDate, BigDecimal> endringRefusjonMap) {
        medRefusjon(refusjonsBelopPerMnd, refusjonsOpphordato,
                endringRefusjonMap.entrySet().stream().map(
                        entry -> createEndringIRefusjon(entry.getKey(), entry.getValue())
                ).collect(Collectors.toList()));
        return this;
    }
    public SkjemainnholdBuilder medRefusjon(BigDecimal refusjonsBelopPerMnd,
                                              LocalDate refusjonsOpphordato,
                                              List<EndringIRefusjon> endringIRefusjonList) {
        this.refusjon = createRefusjon(refusjonsBelopPerMnd, refusjonsOpphordato, endringIRefusjonList);
        return this;
    }
    public SkjemainnholdBuilder medRefusjon(Refusjon refusjon) {
        this.refusjon = refusjon;
        return this;
    }
    public SkjemainnholdBuilder medSykepengerIArbeidsgiverperioden(SykepengerIArbeidsgiverperioden sykepengerIArbeidsgiverperioden) {
        this.sykepengerIArbeidsgiverperioden = sykepengerIArbeidsgiverperioden;
        return this;
    }
    public SkjemainnholdBuilder medNaerRelasjon(Boolean naerRelasjon) {
        this.naerRelasjon = naerRelasjon;
        return this;
    }
    public SkjemainnholdBuilder medAarsakTilInnsending(ÅrsakInnsendingKodeliste aarsakTilInnsending) {
        this.aarsakTilInnsending = aarsakTilInnsending;
        return this;
    }
    public SkjemainnholdBuilder medStartdatoForeldrepengerperiodenFOM(LocalDate startidspunktForeldrepenger) {
        this.startdatoForeldrepengeperiodenFOM = startidspunktForeldrepenger;
        objectFactory.createSkjemainnholdStartdatoForeldrepengeperiode(startidspunktForeldrepenger);
        return this;
    }
    public SkjemainnholdBuilder medYtelse(YtelseKodeliste ytelse) {
        this.ytelse = ytelse;
        return this;

    }
    public SkjemainnholdBuilder medAvsendersystem(Avsendersystem avsendersystem) {
        this.avsendersystem = avsendersystem;
        return this;

    }
    public SkjemainnholdBuilder medAvsendersystem(String avsenderSystem, String systemVersjon) {
        this.avsendersystem = createAvsendersystem(avsenderSystem, systemVersjon);
        return this;

    }
    public SkjemainnholdBuilder medArbeidsforhold(Arbeidsforhold arbeidsforhold) {
        this.arbeidsforhold = arbeidsforhold;
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
    public Refusjon createRefusjon(BigDecimal refusjonsBelopPerMnd, LocalDate refusjonsOpphordato, List<EndringIRefusjon> endringIRefusjonList) {

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

    public Skjemainnhold build() {
        if (avsendersystem == null) {
            medAvsendersystem(createAvsendersystem("FS22", "1.0"));
        }
        if (arbeidsgiver == null && arbeidsgiverPrivat == null) {
            throw new NullPointerException("Inntektsmelding må ha arbeidsgiver eller arbeidsgiverPrivat");
        }
        Objects.requireNonNull(ytelse, "Ytelse kan ikke være null");
        Objects.requireNonNull(aarsakTilInnsending, "Årsak til innsending kan ikke være null");
        Objects.requireNonNull(arbeidstakerFNR, "arbeidstakerFNR kan ikke være null");

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

}
