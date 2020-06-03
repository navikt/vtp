package no.nav.tjeneste.virksomhet.dokumentproduksjon.v2;

import no.nav.foreldrepenger.vtp.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.PdfGenerering.PdfGeneratorUtil;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.AvbrytForsendelseAvbrytelseIkkeTillatt;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.AvbrytForsendelseJournalpostAlleredeAvbrutt;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.AvbrytForsendelseJournalpostIkkeFunnet;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.AvbrytForsendelseJournalpostIkkeUnderArbeid;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.AvbrytVedleggDokumentAlleredeAvbrutt;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.AvbrytVedleggDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.AvbrytVedleggDokumentIkkeVedlegg;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.AvbrytVedleggJournalpostIkkeFunnet;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.AvbrytVedleggJournalpostIkkeUnderArbeid;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.DokumentproduksjonV2;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.EndreDokumentTilRedigerbartDokumentAlleredeRedigerbart;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.EndreDokumentTilRedigerbartDokumentErAvbrutt;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.EndreDokumentTilRedigerbartDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.EndreDokumentTilRedigerbartDokumentIkkeRedigerbart;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.EndreDokumentTilRedigerbartJournalpostIkkeFunnet;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.EndreDokumentTilRedigerbartJournalpostIkkeUnderArbeid;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.FerdigstillForsendelseDokumentUnderRedigering;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.FerdigstillForsendelseJournalpostIkkeFunnet;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.FerdigstillForsendelseJournalpostIkkeUnderArbeid;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.KnyttVedleggTilForsendelseDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.KnyttVedleggTilForsendelseDokumentTillatesIkkeGjenbrukt;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.KnyttVedleggTilForsendelseEksterntVedleggIkkeTillatt;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.KnyttVedleggTilForsendelseJournalpostIkkeFerdigstilt;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.KnyttVedleggTilForsendelseJournalpostIkkeFunnet;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.KnyttVedleggTilForsendelseJournalpostIkkeUnderArbeid;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.KnyttVedleggTilForsendelseUlikeFagomraader;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.ProduserIkkeredigerbartDokumentDokumentErRedigerbart;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.ProduserIkkeredigerbartDokumentDokumentErVedlegg;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.ProduserIkkeredigerbartVedleggDokumentErRedigerbart;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.ProduserIkkeredigerbartVedleggForsendelseIkkeFunnet;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.ProduserIkkeredigerbartVedleggJournalpostIkkeFunnet;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.ProduserIkkeredigerbartVedleggJournalpostIkkeUnderArbeid;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.ProduserIkkeredigerbartVedleggVedleggIkkeTillatt;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.ProduserRedigerbartDokumentDokumentErVedlegg;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.ProduserRedigerbartDokumentDokumentIkkeRedigerbart;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.RedigerDokumentDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.RedigerDokumentPessimistiskLaasing;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.RedigerDokumentRedigeringIkkeTillatt;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.informasjon.Aktoer;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.informasjon.Person;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.AvbrytForsendelseRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.AvbrytVedleggRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.EndreDokumentTilRedigerbartRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.FerdigstillForsendelseRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.KnyttVedleggTilForsendelseRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserDokumentutkastRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserDokumentutkastResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserIkkeredigerbartDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserIkkeredigerbartDokumentResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserIkkeredigerbartVedleggRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserIkkeredigerbartVedleggResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserRedigerbartDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserRedigerbartDokumentResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.RedigerDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.RedigerDokumentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.Addressing;
import java.io.StringWriter;


@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.DokumentproduksjonV2")
@HandlerChain(file = "Handler-chain.xml")
public class DokumentproduksjonV2MockImpl implements DokumentproduksjonV2 {

    private static final Logger LOG = LoggerFactory.getLogger(DokumentproduksjonV2MockImpl.class);


    private JournalRepository journalRepository;

    private PdfGeneratorUtil pdfGeneratorUtil = new PdfGeneratorUtil();

    public DokumentproduksjonV2MockImpl(){}

    public DokumentproduksjonV2MockImpl(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }


    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @Override
    public ProduserDokumentutkastResponse produserDokumentutkast(ProduserDokumentutkastRequest request) {
        String data = xmlToString(((Element) request.getBrevdata()).getOwnerDocument());
        String dokumenttypeId =  request.getDokumenttypeId();

        byte[] bytes = pdfGeneratorUtil.genererPdfByteArrayFraString(data);

        ProduserDokumentutkastResponse response = new ProduserDokumentutkastResponse();
        response.setDokumentutkast(bytes);

        LOG.info("Brev med dokumentTypeId {} returneres til fpformidling for forhåndsvisning", dokumenttypeId);
        return response;
    }

    @Override
    public void avbrytVedlegg(AvbrytVedleggRequest avbrytVedleggRequest) throws AvbrytVedleggJournalpostIkkeUnderArbeid, AvbrytVedleggDokumentAlleredeAvbrutt, AvbrytVedleggJournalpostIkkeFunnet, AvbrytVedleggDokumentIkkeVedlegg, AvbrytVedleggDokumentIkkeFunnet {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ProduserIkkeredigerbartDokumentResponse produserIkkeredigerbartDokument(ProduserIkkeredigerbartDokumentRequest request) throws ProduserIkkeredigerbartDokumentDokumentErRedigerbart, ProduserIkkeredigerbartDokumentDokumentErVedlegg {
        Aktoer bruker = request.getDokumentbestillingsinformasjon().getBruker();

        String data = xmlToString(((Element) request.getBrevdata()).getOwnerDocument());

        LOG.info("Dokument produsert: " + data);

        String dokumenttypeId = request.getDokumentbestillingsinformasjon().getDokumenttypeId();



        LOG.info("produsererIkkeredigerbartDokument med dokumenttypeId {} bestilt for bruker {}({})", dokumenttypeId, ((Person) bruker).getIdent(), ((Person) bruker).getNavn());
        ProduserIkkeredigerbartDokumentResponse response = new ProduserIkkeredigerbartDokumentResponse();
        String journalpostId = journalRepository.leggTilJournalpost(
                JournalpostModellGenerator.lagJournalpostUstrukturertDokument(((Person) bruker).getIdent(), new DokumenttypeId(dokumenttypeId)));
        String dokumentId = journalRepository.finnJournalpostMedJournalpostId(journalpostId).get().getDokumentModellList().get(0).getDokumentId();
        LOG.info("produsererIkkeredigerbartDokument generer journalpost {} med dokument {})", journalpostId, dokumentId);

        response.setDokumentId(dokumentId);
        response.setJournalpostId(journalpostId);
        return response;
    }

    @Override
    public void knyttVedleggTilForsendelse(KnyttVedleggTilForsendelseRequest knyttVedleggTilForsendelseRequest) throws KnyttVedleggTilForsendelseJournalpostIkkeFerdigstilt, KnyttVedleggTilForsendelseUlikeFagomraader, KnyttVedleggTilForsendelseJournalpostIkkeUnderArbeid, KnyttVedleggTilForsendelseJournalpostIkkeFunnet, KnyttVedleggTilForsendelseDokumentIkkeFunnet, KnyttVedleggTilForsendelseEksterntVedleggIkkeTillatt, KnyttVedleggTilForsendelseDokumentTillatesIkkeGjenbrukt {
        //throw new UnsupportedOperationException("Ikke implementert");
        return;
    }

    @Override
    public ProduserRedigerbartDokumentResponse produserRedigerbartDokument(ProduserRedigerbartDokumentRequest produserRedigerbartDokumentRequest) throws ProduserRedigerbartDokumentDokumentIkkeRedigerbart, ProduserRedigerbartDokumentDokumentErVedlegg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public void avbrytForsendelse(AvbrytForsendelseRequest avbrytForsendelseRequest) throws AvbrytForsendelseAvbrytelseIkkeTillatt, AvbrytForsendelseJournalpostIkkeUnderArbeid, AvbrytForsendelseJournalpostIkkeFunnet, AvbrytForsendelseJournalpostAlleredeAvbrutt {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public void ferdigstillForsendelse(FerdigstillForsendelseRequest request) throws FerdigstillForsendelseDokumentUnderRedigering, FerdigstillForsendelseJournalpostIkkeUnderArbeid, FerdigstillForsendelseJournalpostIkkeFunnet {
        LOG.info("ferdigstillForsendelse ferdigstiller journalpost: {}", request.getJournalpostId());
    }

    @Override
    public RedigerDokumentResponse redigerDokument(RedigerDokumentRequest redigerDokumentRequest) throws RedigerDokumentPessimistiskLaasing, RedigerDokumentRedigeringIkkeTillatt, RedigerDokumentDokumentIkkeFunnet {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ProduserIkkeredigerbartVedleggResponse produserIkkeredigerbartVedlegg(ProduserIkkeredigerbartVedleggRequest produserIkkeredigerbartVedleggRequest) throws ProduserIkkeredigerbartVedleggVedleggIkkeTillatt, ProduserIkkeredigerbartVedleggJournalpostIkkeUnderArbeid, ProduserIkkeredigerbartVedleggForsendelseIkkeFunnet, ProduserIkkeredigerbartVedleggJournalpostIkkeFunnet, ProduserIkkeredigerbartVedleggDokumentErRedigerbart {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public void endreDokumentTilRedigerbart(EndreDokumentTilRedigerbartRequest endreDokumentTilRedigerbartRequest) throws EndreDokumentTilRedigerbartJournalpostIkkeUnderArbeid, EndreDokumentTilRedigerbartJournalpostIkkeFunnet, EndreDokumentTilRedigerbartDokumentIkkeRedigerbart, EndreDokumentTilRedigerbartDokumentIkkeFunnet, EndreDokumentTilRedigerbartDokumentAlleredeRedigerbart, EndreDokumentTilRedigerbartDokumentErAvbrutt {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    private String xmlToString(final Document document) {
        try {
            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            String message = "Kunne ikke produsere text fra xml: " + e.getMessage();
            LOG.warn(message);
            return message;
        }
    }

}
