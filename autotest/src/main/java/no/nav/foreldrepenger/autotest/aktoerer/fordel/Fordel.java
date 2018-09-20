package no.nav.foreldrepenger.autotest.aktoerer.fordel;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.FordelKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.JournalpostId;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.JournalpostKnyttning;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.JournalpostMottak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.OpprettSak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.Saksnummer;

public class Fordel extends Aktoer{
    
    /*
     * Klienter
     */
    FordelKlient fordelKlient;
    
    public Fordel() {
        fordelKlient = new FordelKlient(session);
    }
    
    
    /*
     * Sender inn søkand og returnerer saksinformasjon
     */
    public Object sendInnSøknad(Object object) {
        return null;
    }
    
    /*
     * Sender inn søknad og returnerer saksinformasjon
     */
    public Object sendInnPapirsøkand(Object object) {
        return null;
    }


    /*
     * Sender inn inntektsmelding og returnerer saksnummer
     */
    public long sendInnInntektsmelding(String journalpostId, String behandlingstemaOffisiellKode, String dokumentTypeIdOffisiellKode, String aktørId) throws IOException {
        OpprettSak journalpost = new OpprettSak(journalpostId, behandlingstemaOffisiellKode, aktørId);
        Saksnummer saksnummer = fordelKlient.fagsakOpprett(journalpost);
        
        JournalpostId idDto = new JournalpostId(journalpostId);
        JournalpostKnyttning journalpostKnyttning = new JournalpostKnyttning(saksnummer, idDto);
        fordelKlient.fagsakKnyttJournalpost(journalpostKnyttning);
        
        
        JournalpostMottak journalpostMottak = new JournalpostMottak("" + saksnummer.saksnummer, journalpostId, LocalDate.now(), behandlingstemaOffisiellKode);
        journalpostMottak.dokumentTypeIdOffisiellKode = dokumentTypeIdOffisiellKode;
        //String xmlText = readFile();
        //journalpostMottak.payloadXml = new String(Base64.getUrlEncoder().withoutPadding().encode(xmlText.getBytes()));
        //journalpostMottak.payloadLength = xmlText.length();
        fordelKlient.journalpost(journalpostMottak);
        
        return saksnummer.saksnummer;
    }


    // DELETEME
    /*
    private String readFile() throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get("test.xml"));
        return new String(encoded, Charset.defaultCharset());
    }
    */
    
}
