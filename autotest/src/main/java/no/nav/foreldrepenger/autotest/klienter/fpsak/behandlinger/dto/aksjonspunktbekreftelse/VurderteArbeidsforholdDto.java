package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

public class VurderteArbeidsforholdDto {

    protected Long andelsnr;
    protected boolean tidsbegrensetArbeidsforhold;
    protected Boolean opprinneligVerdi;

    public VurderteArbeidsforholdDto(Long andelsnr, boolean tidsbegrensetArbeidsforhold, Boolean opprinneligVerdi) {
        this.andelsnr = andelsnr;
        this.tidsbegrensetArbeidsforhold = tidsbegrensetArbeidsforhold;
        this.opprinneligVerdi = opprinneligVerdi;
    }
}
