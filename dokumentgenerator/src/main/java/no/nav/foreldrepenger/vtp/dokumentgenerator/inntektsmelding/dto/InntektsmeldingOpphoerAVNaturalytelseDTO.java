package no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.dto;

import java.time.LocalDate;

import no.nav.inntektsmelding.xml.kodeliste._20180702.NaturalytelseKodeliste;

public class InntektsmeldingOpphoerAVNaturalytelseDTO {

    private NaturalytelseKodeliste naturalytelsestype;
    private LocalDate fom;
    private Integer belopPrMnd;

    public NaturalytelseKodeliste getNaturalytelsestype() {
        return naturalytelsestype;
    }

    public void setNaturalytelsestype(NaturalytelseKodeliste naturalytelsestype) {
        this.naturalytelsestype = naturalytelsestype;
    }

    public LocalDate getFom() {
        return fom;
    }

    public void setFom(LocalDate fom) {
        this.fom = fom;
    }

    public Integer getBelopPrMnd() {
        return belopPrMnd;
    }

    public void setBelopPrMnd(Integer belopPrMnd) {
        this.belopPrMnd = belopPrMnd;
    }
}
