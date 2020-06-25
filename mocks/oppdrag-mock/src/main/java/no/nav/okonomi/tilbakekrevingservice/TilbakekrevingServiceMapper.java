package no.nav.okonomi.tilbakekrevingservice;

import no.nav.tilbakekreving.kravgrunnlag.hentliste.v1.ReturnertKravgrunnlagDto;
import no.nav.tilbakekreving.typer.v1.MmelDto;

import java.math.BigDecimal;
import java.math.BigInteger;

class TilbakekrevingServiceMapper {

    private static final String KVITTERING_OK_KODE = "00";

    public static TilbakekrevingsvedtakResponse opprettTilbakekrevingVedtakResponse(TilbakekrevingsvedtakRequest request) {
        TilbakekrevingsvedtakResponse respons = new TilbakekrevingsvedtakResponse();
        respons.setMmel(opprettMmel());
        respons.setTilbakekrevingsvedtak(request.getTilbakekrevingsvedtak());
        return respons;
    }

    public static KravgrunnlagHentDetaljResponse opprettKravgrunnlagHentDetaljResponse() {
        KravgrunnlagHentDetaljResponse respons = new KravgrunnlagHentDetaljResponse();
        respons.setMmel(opprettMmel());
        respons.setDetaljertkravgrunnlag(KravgrunnlagGenerator.hentGrunnlag());
        return respons;
    }

    public static KravgrunnlagHentListeResponse opprettKravgrunnlagHentListeResponse() {
        KravgrunnlagHentListeResponse respons = new KravgrunnlagHentListeResponse();
        respons.setMmel(opprettMmel());
        ReturnertKravgrunnlagDto kravgrunnlag = new ReturnertKravgrunnlagDto();
        kravgrunnlag.setKravgrunnlagId(BigInteger.ONE);
        kravgrunnlag.setKodeStatusKrav("NY");
        kravgrunnlag.setBelopSumFeilutbetalt(BigDecimal.valueOf(20000));
        respons.getKravgrunnlagListe().add(kravgrunnlag);
        return respons;
    }

    public static KravgrunnlagAnnulerResponse opprettKravgrunnlagAnnulerResponse(KravgrunnlagAnnulerRequest kravgrunnlagAnnulerRequest) {
        KravgrunnlagAnnulerResponse respons = new KravgrunnlagAnnulerResponse();
        respons.setMmel(opprettMmel());
        respons.setAnnullerkravgrunnlag(kravgrunnlagAnnulerRequest.getAnnullerkravgrunnlag());
        return respons;
    }

    static MmelDto opprettMmel() {
        MmelDto mmelDto = new MmelDto();
        mmelDto.setAlvorlighetsgrad(KVITTERING_OK_KODE);
        mmelDto.setSystemId("1");
        return mmelDto;
    }
}
