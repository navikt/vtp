package no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.dto;

import no.nav.inntektsmelding.xml.kodeliste._20180702.NaturalytelseKodeliste;

import java.time.LocalDate;

public class InntektsmeldingGjenopptakelseAvNaturalytelseDTO {

    private NaturalytelseKodeliste naturalytelsetype;
    private LocalDate fom;
    private Integer belopPrMnd;

    public NaturalytelseKodeliste getNaturalytelsetype() {
        return naturalytelsetype;
    }

    public void setNaturalytelsetype(NaturalytelseKodeliste naturalytelsetype) {
        this.naturalytelsetype = naturalytelsetype;
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
