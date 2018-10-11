package no.nav.tjeneste.virksomhet.journal.v3;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.tjeneste.virksomhet.journal.modell.JournalpostV3Bulider;
import no.nav.tjeneste.virksomhet.journal.v3.feil.DokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.Variantformater;
import no.nav.tjeneste.virksomhet.journal.v3.meldinger.*;
import no.nav.tjeneste.virksomhet.journal.v3.meldinger.HentDokumentResponse;
import no.nav.tjeneste.virksomhet.journal.v3.meldinger.HentDokumentURLResponse;
import no.nav.tjeneste.virksomhet.journal.v3.meldinger.HentKjerneJournalpostListeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.soap.Addressing;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Addressing
@WebService(name = "Journal_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v3")
@HandlerChain(file="Handler-chain.xml")
public class JournalV3ServiceMockImpl implements JournalV3 {

    private static final Logger LOG = LoggerFactory.getLogger(JournalV3ServiceMockImpl.class);

    private JournalRepository journalRepository;

    public JournalV3ServiceMockImpl() {}

    public JournalV3ServiceMockImpl(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Override
    public void ping() {
        LOG.info("Ping mottat og besvart.");
    }

    @Override
    public HentDokumentURLResponse hentDokumentURL(HentDokumentURLRequest hentDokumentURLRequest) throws HentDokumentURLDokumentIkkeFunnet, HentDokumentURLSikkerhetsbegrensning {
        throw new UnsupportedOperationException("Ikke implementert.");
    }

    @Override
    public HentKjerneJournalpostListeResponse hentKjerneJournalpostListe(HentKjerneJournalpostListeRequest request) throws HentKjerneJournalpostListeSikkerhetsbegrensning, HentKjerneJournalpostListeUgyldigInput {
        LOG.info("JournalV3. hentKjerneJournalpostListe.");

        HentKjerneJournalpostListeResponse response = new HentKjerneJournalpostListeResponse();
        List<String> saker = request.getArkivSakListe().stream().map(t -> t.getArkivSakId()).collect(Collectors.toList());
        LOG.info("Henter journalpost for følgende saker: "+ saker.stream().collect(Collectors.joining(",")));

        for(String sak : saker){
            for(JournalpostModell modell : journalRepository.finnJournalposterMedSakId(sak)){
                response.getJournalpostListe().add(JournalpostV3Bulider.buildFrom(modell));
            }
        }
        return response;
    }

    @Override
    public HentDokumentResponse hentDokument(HentDokumentRequest request) throws HentDokumentSikkerhetsbegrensning, HentDokumentDokumentIkkeFunnet, HentDokumentJournalpostIkkeFunnet {
        LOG.info("JournalV3. hentDokument");
        HentDokumentResponse response = new HentDokumentResponse();
        Optional<DokumentModell> dokumentModell = journalRepository.finnDokumentMedDokumentId(request.getDokumentId());
        LOG.info("Henter dokument på følgende dokumentId: " + request.getDokumentId());

        if (dokumentModell.isPresent()) {
            String innhold = dokumentModell.get().getInnhold();
            response.setDokument(innhold.getBytes());
            return response;
        } else {
            throw new HentDokumentDokumentIkkeFunnet("Kunne ikke finne dokument", new DokumentIkkeFunnet());
        }
    }
}
