package no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.dto;

import java.time.LocalDate;

import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakUtsettelseKodeliste;

public class InntektsmeldingUtsettelseAvForeldrepengerDTO {


    private ÅrsakUtsettelseKodeliste utesettelseAvForeldrepenger;
    private LocalDate fom;
    private LocalDate tom;

    public ÅrsakUtsettelseKodeliste getUtesettelseAvForeldrepenger() {
        return utesettelseAvForeldrepenger;
    }

    public void setUtesettelseAvForeldrepenger(ÅrsakUtsettelseKodeliste utesettelseAvForeldrepenger) {
        this.utesettelseAvForeldrepenger = utesettelseAvForeldrepenger;
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
