package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

class FastsettEndretBeregningsgrunnlagAndel extends RedigerbarAndel  {

    protected FastsatteVerdier fastsatteVerdier;

    public FastsettEndretBeregningsgrunnlagAndel(RedigerbarAndel redigerbarAndel, FastsatteVerdier fastsatteVerdier) {
        super(redigerbarAndel.andel, redigerbarAndel.andelsnr, redigerbarAndel.arbeidsforholdId, redigerbarAndel.nyAndel, redigerbarAndel.lagtTilAvSaksbehandler);
        this.fastsatteVerdier = fastsatteVerdier;
    }


}
