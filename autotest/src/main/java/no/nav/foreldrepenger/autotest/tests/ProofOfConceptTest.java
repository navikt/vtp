package no.nav.foreldrepenger.autotest.tests;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengesoknadXmlErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.JournalRepositoryImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

public class ProofOfConceptTest {


    @Test
    public void proofOfConcept() throws Exception {

    }

    @Test
    public void opprettSøknadOgLagreJournalpost() throws Exception {
        TestscenarioRepositoryImpl testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        TestscenarioImpl testscenario = testscenarioRepository.opprettTestscenario(TestscenarioTemplateRepositoryImpl.getInstance().finn("50"));
        ForeldrepengesoknadXmlErketyper fxe = new ForeldrepengesoknadXmlErketyper();
        String fnrSøker = testscenario.getPersonopplysninger().getSøker().getAktørIdent();

        Soeknad soeknad = fxe.termindatoUttakKunMor(fnrSøker);
        String søknadXml = ForeldrepengesoknadBuilder.tilXML(soeknad);


        JournalpostModell journalpostModell = JournalpostModellGenerator.foreldrepengeSøknadFødselJournalpost(søknadXml, testscenario.getPersonopplysninger().getSøker().getIdent());
        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();
        String journalpostId = journalRepository.leggTilJournalpost(journalpostModell);

        //TODO: Forsett ved å sende journalpostId (og anndre nødvendige data) til FPSAK for å starte behandling

    }
}
