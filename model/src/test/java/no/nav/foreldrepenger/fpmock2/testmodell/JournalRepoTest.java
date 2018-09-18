package no.nav.foreldrepenger.fpmock2.testmodell;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.JournalRepositoryImpl;

public class JournalRepoTest {

    @Test
    public void skalHenteJournalposterPåSaksId(){

        String sakId = "12381";

        JournalRepository journalRepository = new JournalRepositoryImpl();

        DokumentModell dokumentModell = new DokumentModell();
        dokumentModell.setDokumentId("1235");
        dokumentModell.setDokumentTilknyttetJournalpost("HOVEDOKUMENT");
        dokumentModell.setDokumentType("FLG");
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
    public void skalSetteJournalpostId() {
        JournalRepository journalRepository = new JournalRepositoryImpl();

        JournalpostModell journalpostModell = new JournalpostModell();
        journalpostModell.setKommunikasjonsretning("Inn");
        journalpostModell.setAvsenderFnr("00115522447");
        String journalpostId = journalRepository.leggTilJournalpost(journalpostModell);

        Assert.assertTrue(journalRepository.finnJournalpostMedJournalpostId(journalpostId).get().getId().equals(journalpostId));

    }


}
