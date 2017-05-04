package no.nav.tjeneste.virksomhet.journal.v2;

import no.nav.tjeneste.virksomhet.journal.v2.binding.HentJournalpostListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Sak;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.SoekeFilter;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentJournalpostListeResponse;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentJournalpostListeRequest;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentRequest;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentResponse;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class JournalServiceMockTest {

    @Test
    public void testHentJournalpostListeResponse()throws HentJournalpostListeSikkerhetsbegrensning
    {
        HentJournalpostListeRequest request = new HentJournalpostListeRequest();
        SoekeFilter sf = new SoekeFilter();
        request.setSoekeFilter(sf);
        Sak saki = new Sak();
        saki.setSakId("707851843400");
        request.getSakListe().add(saki);
        JournalServiceMockImpl callee = new JournalServiceMockImpl();
        HentJournalpostListeResponse response = callee.hentJournalpostListe(request);
        assertThat(response).isNotNull();
        assertThat(response.getJournalpostListe()).isNotEmpty();
    }

    @Test
    public void testHentDokument() throws HentDokumentDokumentIkkeFunnet, HentDokumentSikkerhetsbegrensning
    {
        HentDokumentRequest request = new HentDokumentRequest();
        request.setDokumentId("393893532");
        request.setJournalpostId("journalpost-inn-707851843400");
        JournalServiceMockImpl callee = new JournalServiceMockImpl();
        HentDokumentResponse response = callee.hentDokument(request);
        assertThat(response).isNotNull();
        assertThat(response.getDokument()).isNotEmpty();

    }


}
