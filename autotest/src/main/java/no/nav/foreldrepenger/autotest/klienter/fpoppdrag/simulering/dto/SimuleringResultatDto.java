package no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SimuleringResultatDto {

    private LocalDate periodeFom;
    private LocalDate periodeTom;

    private Long sumEtterbetaling;
    private Long sumFeilutbetaling;
    private Long sumInntrekk;
    private boolean ingenPerioderMedAvvik = true;


    public LocalDate getPeriodeFom() {
        return periodeFom;
    }

    public LocalDate getPeriodeTom() {
        return periodeTom;
    }

    public Long getSumEtterbetaling() {
        return sumEtterbetaling;
    }

    public Long getSumFeilutbetaling() {
        return sumFeilutbetaling;
    }

    public Long getSumInntrekk() {
        return sumInntrekk;
    }

    public boolean isIngenPerioderMedAvvik() {
        return ingenPerioderMedAvvik;
    }

}
