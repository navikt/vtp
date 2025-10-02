package no.nav.foreldrepenger.vtp.testmodell;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.Tilleggsopplysning;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.JournalRepositoryImpl;

public class JournalRepoTest {

    @Test
    public void skalFinneJournalposterPåSaksid(){

        String sakId = "12381";

        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();

        DokumentModell dokumentModell = new DokumentModell();
        dokumentModell.setDokumentId("1235");
        dokumentModell.setDokumentTilknyttetJournalpost(DokumentTilknyttetJournalpost.HOVEDDOKUMENT);
        dokumentModell.setDokumentType(DokumenttypeId.SØKNAD_FORELDREPENGER_ADOPSJON);
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
        assertThat(journalpostModells.get(0)).isEqualTo(journalpostModell);
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

    @Test
    public void testTilleggsopplysningerField() {
        JournalpostModell journalpostModell = new JournalpostModell();

        // Sjekker at felt er initialisert som tom liste
        assertThat(journalpostModell.getTilleggsopplysninger()).isNotNull();
        assertThat(journalpostModell.getTilleggsopplysninger()).isEmpty();

        // Tester setter
        List<Tilleggsopplysning> testData = List.of(
                new Tilleggsopplysning("k9.kilde", "SKANNING"),
                new Tilleggsopplysning("k9.type", "SØKNAD")

        );

        journalpostModell.setTilleggsopplysninger(testData);
        assertThat(journalpostModell.getTilleggsopplysninger()).isEqualTo(testData);

        // Tester equals funksjonalitet
        JournalpostModell journalpostModell2 = new JournalpostModell();
        journalpostModell2.setTilleggsopplysninger(testData);
        assertThat(journalpostModell.getTilleggsopplysninger()).isEqualTo(journalpostModell2.getTilleggsopplysninger());

        // Tester hashCode (dekker hashCode linje)
        assertThat(journalpostModell.hashCode()).isEqualTo(journalpostModell2.hashCode());

        journalpostModell.setAvsenderFnr("12345678901");
        journalpostModell2.setAvsenderFnr("12345678901");

        // Tester toString (dekker toString linje)
        String toString = journalpostModell.toString();
        assertThat(toString).contains("tilleggsopplysninger");
        assertThat(toString).contains("k9.kilde");
    }
}
