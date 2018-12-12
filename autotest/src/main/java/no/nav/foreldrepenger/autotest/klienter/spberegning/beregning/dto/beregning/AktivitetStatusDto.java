package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AktivitetStatusDto {
    protected Kode aktivitetStatus;
    
    public AktivitetStatusDto() {
    }
}
