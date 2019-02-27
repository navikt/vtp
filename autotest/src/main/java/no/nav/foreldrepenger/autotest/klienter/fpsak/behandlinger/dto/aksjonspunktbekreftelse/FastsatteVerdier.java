package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class FastsatteVerdier {

    protected Integer refusjon;
    protected Integer fastsattBeløp;
    protected Kode inntektskategori;

    public FastsatteVerdier(Integer refusjon, Integer fastsattBeløp, Kode inntektskategori) {
        this.refusjon = refusjon;
        this.fastsattBeløp = fastsattBeløp;
        this.inntektskategori = inntektskategori;
    }

    public Integer getRefusjon() {
        return refusjon;
    }

    public void setRefusjon(Integer refusjon) {
        this.refusjon = refusjon;
    }

    public Integer getFastsattBeløp() {
        return fastsattBeløp;
    }

    public void setFastsattBeløp(Integer fastsattBeløp) {
        this.fastsattBeløp = fastsattBeløp;
    }

    public Kode getInntektskategori() {
        return inntektskategori;
    }

    public void setInntektskategori(Kode inntektskategori) {
        this.inntektskategori = inntektskategori;
    }
}
