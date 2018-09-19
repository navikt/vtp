package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import java.time.LocalDate;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class UttakDokumentasjon {

    public LocalDate fom = null;
    public LocalDate tom = null;
    public Kode dokumentasjonType = null;
    
    public UttakDokumentasjon(LocalDate fom, LocalDate tom, Kode dokumentasjonType) {
        super();
        this.fom = fom;
        this.tom = tom;
        this.dokumentasjonType = dokumentasjonType;
    }
}
