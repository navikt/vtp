package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

public class FastsettMaanedsinntektUtenInntektsmeldingAndel {

    protected long andelsnr;
    protected int arbeidsinntekt;

    public FastsettMaanedsinntektUtenInntektsmeldingAndel(long andelsnr, int arbeidsinntekt) {
        this.andelsnr = andelsnr;
        this.arbeidsinntekt = arbeidsinntekt;
    }

    public long getAndelsnr() {
        return andelsnr;
    }

    public int getArbeidsinntekt() {
        return arbeidsinntekt;
    }
}
