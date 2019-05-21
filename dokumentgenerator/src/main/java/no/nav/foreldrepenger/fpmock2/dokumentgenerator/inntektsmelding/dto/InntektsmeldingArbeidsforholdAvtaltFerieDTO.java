package no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.dto;

import java.time.LocalDate;

public class InntektsmeldingArbeidsforholdAvtaltFerieDTO {

    private LocalDate fom;

    private LocalDate tom;

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
