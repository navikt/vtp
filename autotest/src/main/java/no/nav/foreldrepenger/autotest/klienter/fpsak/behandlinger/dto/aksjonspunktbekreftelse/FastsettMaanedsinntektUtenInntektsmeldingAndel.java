package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

public class FastsettMaanedsinntektUtenInntektsmeldingAndel {

    protected long andelsnr;

    protected FastsatteVerdierDto fastsatteVerdier;

    public FastsettMaanedsinntektUtenInntektsmeldingAndel(long andelsnr, int arbeidsinntekt) {
        this.andelsnr = andelsnr;
        this.fastsatteVerdier = new FastsatteVerdierDto(arbeidsinntekt);
    }

    public long getAndelsnr() {
        return andelsnr;
    }

}
