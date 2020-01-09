package no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.dto;

import java.time.LocalDate;

import no.nav.inntektsmelding.xml.kodeliste._20180702.NaturalytelseKodeliste;
@Deprecated
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
