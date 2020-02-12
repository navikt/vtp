package no.nav.tjeneste.virksomhet.dokumentproduksjon.v2;

import no.nav.foreldrepenger.vtp.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.PdfGenerering.PdfGenerator;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.*;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.informasjon.Aktoer;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.informasjon.Person;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserDokumentutkastResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserIkkeredigerbartDokumentResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserIkkeredigerbartVedleggResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserRedigerbartDokumentResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.RedigerDokumentResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.*;
import org.apache.pdfbox.pdmodel.PDDocument;
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
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.DokumentproduksjonV2")
@HandlerChain(file = "Handler-chain.xml")
public class DokumentproduksjonV2MockImpl implements DokumentproduksjonV2 {

    private static final Logger LOG = LoggerFactory.getLogger(DokumentproduksjonV2MockImpl.class);
    private static final String OUTPUT_PDF = "statiskBrev.pdf";

    private JournalRepository journalRepository;

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

        genererPdfFraString(data, OUTPUT_PDF);
        byte[] bytes = pdfToByte(OUTPUT_PDF);

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
        throw new UnsupportedOperationException("Ikke implementert");
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

    private void genererPdfFraString(String brev, String filepath) {
        try {
            PDDocument doc = new PDDocument();
            PdfGenerator renderer = new PdfGenerator(doc, brev);
            renderer.renderText(60);
            renderer.close();
            doc.save(new File(filepath));
            doc.close();
        } catch (IOException e) {
            LOG.warn("Kunne ikke generer PDF fra string: " + e.getMessage());
        }
    }

    static byte[] pdfToByte(String filePath) {
        try {
            Path pdfPath = Paths.get(filePath);
            return Files.readAllBytes(pdfPath);
        } catch (IOException e) {
            String message = "Noe gikk galt når pdfen skulle konverters til byte array: " + e.getMessage();
            LOG.warn(message);
            return null;
        }
    }
}
