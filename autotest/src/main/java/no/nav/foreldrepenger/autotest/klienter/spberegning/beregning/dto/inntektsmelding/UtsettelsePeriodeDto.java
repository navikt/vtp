package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.inntektsmelding;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UtsettelsePeriodeDto {

    protected LocalDate fom;
    protected LocalDate tom;
    protected Kode årsak;
    
    public UtsettelsePeriodeDto() {
    }
}
