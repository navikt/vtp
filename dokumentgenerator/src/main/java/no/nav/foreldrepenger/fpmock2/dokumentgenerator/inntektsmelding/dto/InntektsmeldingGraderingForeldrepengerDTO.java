package no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.dto;

import java.time.LocalDate;

public class InntektsmeldingGraderingForeldrepengerDTO {

    Integer arbeidstidsprosent;
    LocalDate fom;
    LocalDate tom;


    public Integer getArbeidstidsprosent() {
        return arbeidstidsprosent;
    }

    public void setArbeidstidsprosent(Integer arbeidstidsprosent) {
        this.arbeidstidsprosent = arbeidstidsprosent;
    }

    public LocalDate getFom() {
        return fom;
    }

    public void setFom(LocalDate fom) {
        this.fom = fom;
    }

    public LocalDate getTom() {
        return tom;
    }

    public void setTom(LocalDate tom) {
        this.tom = tom;
    }
}
