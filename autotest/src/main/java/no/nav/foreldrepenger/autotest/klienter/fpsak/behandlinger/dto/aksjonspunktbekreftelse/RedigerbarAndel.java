package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

class RedigerbarAndel {

    protected String andel;
    protected int andelsnr;
    protected String arbeidsforholdId;
    protected Boolean nyAndel;
    protected Boolean lagtTilAvSaksbehandler;

    public RedigerbarAndel(String andel, int andelsnr, String arbeidsforholdId, Boolean nyAndel, Boolean lagtTilAvSaksbehandler) {
        this.andel = andel;
        this.andelsnr = andelsnr;
        this.arbeidsforholdId = arbeidsforholdId;
        this.nyAndel = nyAndel;
        this.lagtTilAvSaksbehandler = lagtTilAvSaksbehandler;
    }

    public String getAndel() {
        return andel;
    }

    public void setAndel(String andel) {
        this.andel = andel;
    }

    public int getAndelsnr() {
        return andelsnr;
    }

    public void setAndelsnr(int andelsnr) {
        this.andelsnr = andelsnr;
    }

    public String getArbeidsforholdId() {
        return arbeidsforholdId;
    }

    public void setArbeidsforholdId(String arbeidsforholdId) {
        this.arbeidsforholdId = arbeidsforholdId;
    }

    public Boolean getNyAndel() {
        return nyAndel;
    }

    public void setNyAndel(Boolean nyAndel) {
        this.nyAndel = nyAndel;
    }

    public Boolean getLagtTilAvSaksbehandler() {
        return lagtTilAvSaksbehandler;
    }

    public void setLagtTilAvSaksbehandler(Boolean lagtTilAvSaksbehandler) {
        this.lagtTilAvSaksbehandler = lagtTilAvSaksbehandler;
    }
}
