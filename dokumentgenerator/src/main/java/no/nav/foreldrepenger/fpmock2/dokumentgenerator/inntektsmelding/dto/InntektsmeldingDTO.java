package no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.dto;

import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakBeregnetInntektEndringKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakInnsendingKodeliste;

import java.time.LocalDate;
import java.util.List;

public class InntektsmeldingDTO {

    private String inntektsmeldingID;

    private String arbeidstakerFNR;

    private EksempelArbeidsgiver eksempelArbeidsgiver;

    private String organisasjonsnummer;

    private YtelseKodeliste inntektsmeldingYtelse;

    private ÅrsakInnsendingKodeliste inntektsmeldingType;

    private Integer beregnetInntektBelop;

    private ÅrsakBeregnetInntektEndringKodeliste arsakEndring;

    private Integer refusjonBeloepPrMnd;

    private LocalDate refusjonOpphoersdato;

    private LocalDate startDatoForeldrepengePerioden;

    private InntektsmeldingSykepengerIArbeidsgiverperiodenDTO inntektsmeldingSykepengerIArbeidsgiverperiodenDTO;

    private List<InntektsmeldingUtsettelseAvForeldrepengerDTO> utsettelseAvForeldrepengerDTOliste;

    private List<InntektsmeldingGraderingForeldrepengerDTO> graderingForeldrepengerDTOliste;

    private List<InntektsmeldingOpphoerAVNaturalytelseDTO> opphoerAVNaturalytelseDTOliste;

    private List<InntektsmeldingGjenopptakelseAvNaturalytelseDTO> gjenopptakelseAvNaturalytelseDTOListe;

    private List<InntektsmeldingArbeidsforholdAvtaltFerieDTO> inntektsmeldingArbeidsforholdAvtaltFerieDTOS;

    private List<InntektsmeldingEndringIRefusjonDTO> inntektsmeldingEndringIRefusjonDTOS;

    private String arbeidsforholdId;

    private Boolean naerRelasjon;




    public String getInntektsmeldingID() {
        return inntektsmeldingID;
    }

    public void setInntektsmeldingID(String inntektsmeldingID) {
        this.inntektsmeldingID = inntektsmeldingID;
    }

    public YtelseKodeliste getInntektsmeldingYtelse() {
        return inntektsmeldingYtelse;
    }

    public void setInntektsmeldingYtelse(YtelseKodeliste inntektsmeldingYtelse) {
        this.inntektsmeldingYtelse = inntektsmeldingYtelse;
    }

    public ÅrsakInnsendingKodeliste getInntektsmeldingType() {
        return inntektsmeldingType;
    }

    public void setInntektsmeldingType(ÅrsakInnsendingKodeliste inntektsmeldingType) {
        this.inntektsmeldingType = inntektsmeldingType;
    }

    public LocalDate getStartDatoForeldrepengePerioden() {
        return startDatoForeldrepengePerioden;
    }

    public void setStartDatoForeldrepengePerioden(LocalDate startDatoForeldrepengePerioden) {
        this.startDatoForeldrepengePerioden = startDatoForeldrepengePerioden;
    }

    public String getArbeidstakerFNR() {
        return arbeidstakerFNR;
    }

    public void setArbeidstakerFNR(String arbeidstakerFNR) {
        this.arbeidstakerFNR = arbeidstakerFNR;
    }

    public EksempelArbeidsgiver getEksempelArbeidsgiver() {
        return eksempelArbeidsgiver;
    }

    public void setEksempelArbeidsgiver(EksempelArbeidsgiver eksempelArbeidsgiver) {
        this.eksempelArbeidsgiver = eksempelArbeidsgiver;
    }


    public Integer getBeregnetInntektBelop() {
        return beregnetInntektBelop;
    }

    public void setBeregnetInntektBelop(Integer beregnetInntektBelop) {
        this.beregnetInntektBelop = beregnetInntektBelop;
    }

    public ÅrsakBeregnetInntektEndringKodeliste getKodelisteArsakEndring() {
        return arsakEndring;
    }

    public void setArsakEndring(ÅrsakBeregnetInntektEndringKodeliste kodelisteArsakEndring) {
        this.arsakEndring = kodelisteArsakEndring;
    }

    public Integer getRefusjonBeloepPrMnd() {
        return refusjonBeloepPrMnd;
    }

    public void setRefusjonBeloepPrMnd(Integer refusjonBeloepPrMnd) {
        this.refusjonBeloepPrMnd = refusjonBeloepPrMnd;
    }

    public LocalDate getRefusjonOpphoersdato() {
        return refusjonOpphoersdato;
    }

    public void setRefusjonOpphoersdato(LocalDate refusjonOpphoersdato) {
        this.refusjonOpphoersdato = refusjonOpphoersdato;
    }

    public InntektsmeldingSykepengerIArbeidsgiverperiodenDTO getInntektsmeldingSykepengerIArbeidsgiverperiodenDTO() {
        return inntektsmeldingSykepengerIArbeidsgiverperiodenDTO;
    }

    public void setInntektsmeldingSykepengerIArbeidsgiverperiodenDTO(InntektsmeldingSykepengerIArbeidsgiverperiodenDTO inntektsmeldingSykepengerIArbeidsgiverperiodenDTO) {
        this.inntektsmeldingSykepengerIArbeidsgiverperiodenDTO = inntektsmeldingSykepengerIArbeidsgiverperiodenDTO;
    }

    public List<InntektsmeldingUtsettelseAvForeldrepengerDTO> getUtsettelseAvForeldrepengerDTOliste() {
        return utsettelseAvForeldrepengerDTOliste;
    }

    public void setUtsettelseAvForeldrepengerDTOliste(List<InntektsmeldingUtsettelseAvForeldrepengerDTO> utsettelseAvForeldrepengerDTOliste) {
        this.utsettelseAvForeldrepengerDTOliste = utsettelseAvForeldrepengerDTOliste;
    }

    public List<InntektsmeldingGraderingForeldrepengerDTO> getGraderingForeldrepengerDTOliste() {
        return graderingForeldrepengerDTOliste;
    }

    public void setGraderingForeldrepengerDTOliste(List<InntektsmeldingGraderingForeldrepengerDTO> graderingForeldrepengerDTOliste) {
        this.graderingForeldrepengerDTOliste = graderingForeldrepengerDTOliste;
    }

    public List<InntektsmeldingOpphoerAVNaturalytelseDTO> getOpphoerAVNaturalytelseDTOliste() {
        return opphoerAVNaturalytelseDTOliste;
    }

    public void setOpphoerAVNaturalytelseDTOliste(List<InntektsmeldingOpphoerAVNaturalytelseDTO> opphoerAVNaturalytelseDTOliste) {
        this.opphoerAVNaturalytelseDTOliste = opphoerAVNaturalytelseDTOliste;
    }

    public List<InntektsmeldingGjenopptakelseAvNaturalytelseDTO> getGjenopptakelseAvNaturalytelseDTOListe() {
        return gjenopptakelseAvNaturalytelseDTOListe;
    }

    public void setGjenopptakelseAvNaturalytelseDTOListe(List<InntektsmeldingGjenopptakelseAvNaturalytelseDTO> gjenopptakelseAvNaturalytelseDTOListe) {
        this.gjenopptakelseAvNaturalytelseDTOListe = gjenopptakelseAvNaturalytelseDTOListe;
    }

    public List<InntektsmeldingArbeidsforholdAvtaltFerieDTO> getInntektsmeldingArbeidsforholdAvtaltFerieDTOS() {
        return inntektsmeldingArbeidsforholdAvtaltFerieDTOS;
    }

    public void setInntektsmeldingArbeidsforholdAvtaltFerieDTOS(List<InntektsmeldingArbeidsforholdAvtaltFerieDTO> inntektsmeldingArbeidsforholdAvtaltFerieDTOS) {
        this.inntektsmeldingArbeidsforholdAvtaltFerieDTOS = inntektsmeldingArbeidsforholdAvtaltFerieDTOS;
    }

    public List<InntektsmeldingEndringIRefusjonDTO> getInntektsmeldingEndringIRefusjonDTOS() {
        return inntektsmeldingEndringIRefusjonDTOS;
    }

    public void setInntektsmeldingEndringIRefusjonDTOS(List<InntektsmeldingEndringIRefusjonDTO> inntektsmeldingEndringIRefusjonDTOS) {
        this.inntektsmeldingEndringIRefusjonDTOS = inntektsmeldingEndringIRefusjonDTOS;
    }

    public String getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    public void setOrganisasjonsnummer(String organisasjonsnummer) {
        this.organisasjonsnummer = organisasjonsnummer;
    }

    public String getArbeidsforholdId() {
        return arbeidsforholdId;
    }

    public void setArbeidsforholdId(String arbeidsforholdId) {
        this.arbeidsforholdId = arbeidsforholdId;
    }

    public Boolean getNaerRelasjon() {
        return naerRelasjon;
    }

    public void setNaerRelasjon(Boolean naerRelasjon) {
        this.naerRelasjon = naerRelasjon;
    }
}
