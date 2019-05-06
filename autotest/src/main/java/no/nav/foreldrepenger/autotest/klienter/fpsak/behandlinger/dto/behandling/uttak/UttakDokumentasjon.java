package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UttakDokumentasjon {

    protected LocalDate fom;
    protected LocalDate tom;
    protected Kode dokumentasjonType;

    public UttakDokumentasjon() {}

    public UttakDokumentasjon(LocalDate fom, LocalDate tom, Kode dokumentasjonType) {
        super();
        this.fom = fom;
        this.tom = tom;
        this.dokumentasjonType = dokumentasjonType;
    }
}
