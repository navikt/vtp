package no.nav.foreldrepenger.autotest.aktoerer.fordel;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.FordelKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.JournalpostId;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.JournalpostKnyttning;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.JournalpostMottak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.OpprettSak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.Saksnummer;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.JournalRepositoryImpl;

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
    public long sendInnSøknad(Object søknad) {
        String xml = "</xml>";
        String fnr = "105891918";
        JournalpostModell journalpostModell = JournalpostModellGenerator.foreldrepengeSøknadFødselJournalpost(xml, fnr);
        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();
        journalRepository.leggTilJournalpost(journalpostModell);
        
        return -1;
    }
    
    /*
     * Sender inn søknad og returnerer saksinformasjon
     */
    public long sendInnPapirsøkand(Object søknad) {
        return -1;
    }


    /*
     * Sender inn inntektsmelding og returnerer saksnummer
     */
    public long sendInnInntektsmelding(Object inntektsmelding) throws IOException {
        return -1;
    }
    
    public long sendInnInntektsmelding(String journalpostId, String behandlingstemaOffisiellKode, String dokumentTypeIdOffisiellKode, String aktørId) throws IOException {
        return sendInnJournalpost(null, journalpostId, behandlingstemaOffisiellKode, dokumentTypeIdOffisiellKode, aktørId);
    }
    
    /*
     * Sender inn journalpost og returnerer saksnummer
     */
    private long sendInnJournalpost(String xml, String journalpostId, String behandlingstemaOffisiellKode, String dokumentTypeIdOffisiellKode, String aktørId) throws IOException {
        OpprettSak journalpost = new OpprettSak(journalpostId, behandlingstemaOffisiellKode, aktørId);
        Saksnummer saksnummer = fordelKlient.fagsakOpprett(journalpost);
        
        JournalpostId idDto = new JournalpostId(journalpostId);
        JournalpostKnyttning journalpostKnyttning = new JournalpostKnyttning(saksnummer, idDto);
        fordelKlient.fagsakKnyttJournalpost(journalpostKnyttning);
        
        
        JournalpostMottak journalpostMottak = new JournalpostMottak("" + saksnummer.saksnummer, journalpostId, LocalDate.now(), behandlingstemaOffisiellKode);
        journalpostMottak.dokumentTypeIdOffisiellKode = dokumentTypeIdOffisiellKode;
        
        if(null != xml) {
            journalpostMottak.payloadXml = new String(Base64.getUrlEncoder().withoutPadding().encode(xml.getBytes()));
            journalpostMottak.payloadLength = xml.length();
        }
        
        fordelKlient.journalpost(journalpostMottak);
        
        return saksnummer.saksnummer;
    }
}
