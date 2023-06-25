package no.nav.okonomi.tilbakekrevingservice;

import jakarta.jws.HandlerChain;
import jakarta.jws.WebMethod;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
@Addressing
@WebService(name = "TilbakekrevingPortType", targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/")
@HandlerChain(file = "Handler-chain.xml")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class TilbakekrevingServiceMockImpl implements TilbakekrevingPortType {

    private static Logger LOG = LoggerFactory.getLogger(TilbakekrevingServiceMockImpl.class);

    @Override
    @WebMethod
    @WebResult(name = "tilbakekrevingsvedtakResponse", targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/", partName = "parameters")
    public TilbakekrevingsvedtakResponse tilbakekrevingsvedtak(TilbakekrevingsvedtakRequest tilbakekrevingsvedtakRequest) {
        LOG.info("Sendt TilbakekrevingsvedtakRequest.");
        return TilbakekrevingServiceMapper.opprettTilbakekrevingVedtakResponse(tilbakekrevingsvedtakRequest);
    }

    @Deprecated
    @Override
    @WebMethod
    @WebResult(name = "kravgrunnlagHentListeResponse", targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/", partName = "parameters")
    public KravgrunnlagHentListeResponse kravgrunnlagHentListe(KravgrunnlagHentListeRequest kravgrunnlagHentListeRequest) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    @WebMethod
    @WebResult(name = "kravgrunnlagHentDetaljResponse", targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/", partName = "parameters")
    public KravgrunnlagHentDetaljResponse kravgrunnlagHentDetalj(KravgrunnlagHentDetaljRequest kravgrunnlagHentDetaljRequest) {
        LOG.info("Hent grunnlag.");
        return TilbakekrevingServiceMapper.opprettKravgrunnlagHentDetaljResponse(kravgrunnlagHentDetaljRequest);
    }

    @Override
    @WebMethod
    @WebResult(name = "kravgrunnlagAnnulerResponse", targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/", partName = "parameters")
    public KravgrunnlagAnnulerResponse kravgrunnlagAnnuler(KravgrunnlagAnnulerRequest kravgrunnlagAnnulerRequest) {
        LOG.info("Annuler grunnlag.");
        return TilbakekrevingServiceMapper.opprettKravgrunnlagAnnulerResponse(kravgrunnlagAnnulerRequest);
    }
}
