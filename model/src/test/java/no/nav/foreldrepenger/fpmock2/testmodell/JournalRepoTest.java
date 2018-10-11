package no.nav.foreldrepenger.fpmock2.testmodell;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.JournalRepositoryImpl;

public class JournalRepoTest {

    @Test
    public void skalFinneJournalposterPåSaksid(){

        String sakId = "12381";

        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();

        DokumentModell dokumentModell = new DokumentModell();
        dokumentModell.setDokumentId("1235");
        dokumentModell.setDokumentTilknyttetJournalpost(DokumentTilknyttetJournalpost.HOVEDDOKUMENT);
        dokumentModell.setDokumentType(DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD);
        dokumentModell.setErSensitiv(Boolean.FALSE);
        dokumentModell.setInnhold("<xml/>");
        dokumentModell.setTittel("Tittel på dokumentet");

        JournalpostModell journalpostModell = new JournalpostModell();
        journalpostModell.getDokumentModellList().add(dokumentModell);
        journalpostModell.setSakId(sakId);
        journalpostModell.setKommunikasjonsretning("INN");
        journalpostModell.setFagsystemId("FS32");

        journalRepository.leggTilJournalpost(journalpostModell);
        List<JournalpostModell> journalpostModells = journalRepository.finnJournalposterMedSakId(sakId);
        assertThat(journalpostModells).size().isEqualTo(1);
        assertThat(journalpostModells.get(0)).isEqualToComparingFieldByField(journalpostModell);
    }

    @Test
    public void skalFinneJournalposterPåAvsenderFnr(){
        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();

        String avsenderFnrSøker = "01020304056";
        String avsenderFnrIkkeSøker = "99999999999";

        JournalpostModell journalpostModellSøker_1 = new JournalpostModell();
        journalpostModellSøker_1.setAvsenderFnr(avsenderFnrSøker);
        journalRepository.leggTilJournalpost(journalpostModellSøker_1);

        JournalpostModell journalpostModellSøker_2 = new JournalpostModell();
        journalpostModellSøker_2.setAvsenderFnr(avsenderFnrSøker);
        journalRepository.leggTilJournalpost(journalpostModellSøker_2);

        JournalpostModell journalpostModellIkkeSøker_1 = new JournalpostModell();
        journalpostModellIkkeSøker_1.setAvsenderFnr(avsenderFnrIkkeSøker);
        journalRepository.leggTilJournalpost(journalpostModellIkkeSøker_1);

        List<JournalpostModell> journalpostModells = journalRepository.finnJournalposterMedFnr(avsenderFnrSøker);
        assertThat(journalpostModells).size().isEqualTo(2);

    }

    @Test
    public void skalSetteJournalpostIdVedLagring() {
        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();

        JournalpostModell journalpostModell = new JournalpostModell();
        journalpostModell.setKommunikasjonsretning("Inn");
        journalpostModell.setAvsenderFnr("00115522447");
        String journalpostId = journalRepository.leggTilJournalpost(journalpostModell);

        Optional<JournalpostModell> resultatModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId);
        assertThat(resultatModell).isPresent();
        assertThat(resultatModell.get().getJournalpostId()).isEqualTo(journalpostId);

    }
}
