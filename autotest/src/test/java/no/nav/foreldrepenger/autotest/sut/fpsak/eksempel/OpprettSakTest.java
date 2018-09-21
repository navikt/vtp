package no.nav.foreldrepenger.autotest.sut.fpsak.eksempel;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.sut.fpsak.FpsakTestBase;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.JournalRepositoryImpl;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

@Tag("smokeBLA")
public class OpprettSakTest extends FpsakTestBase{
    
    @Test
    public void opprettInntektsmeldingForeldrepenger() throws IOException {
        fordel.erLoggetInnMedRolle("Saksbehandler");
        
        long saksnummer = fordel.sendInnInntektsmelding("409593578", "ab0047", "I000067", "1000104117747");
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak("" + saksnummer);
        
        verifiser(saksbehandler.valgtFagsak.saksnummer == saksnummer, "Kunne ikke hente fagsak");
        
        System.out.println("Saksnummer: " + saksnummer);
    }

    @Test
    public void opprettSøknadForeldrepenger() throws IOException, DatatypeConfigurationException, JAXBException {
        //Funksjonalitet for å opprette en instans av testdata-template
        //fordel.erLoggetInnMedRolle("Saksbehandler");
        Soeknad build = ForeldrepengesoknadBuilder.startBuilding().build();
        //Bytte med Klient for å lagre journalpost. Sjekk med FC om dette er riktig approach.
        JournalpostModell journalpostModell = JournalpostModellGenerator.foreldrepengeSøknadFødselJournalpost(soknadXml, "105891918");
        JournalRepository journalRepository = new JournalRepositoryImpl();
        journalRepository.leggTilJournalpost(journalpostModell);

        String s = "";



    }


}
