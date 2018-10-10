package no.nav.foreldrepenger.autotest.aktoerer.fordel;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.BehandlingerKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.FordelKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.JournalpostId;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.JournalpostKnyttning;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.JournalpostMottak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.OpprettSak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.Saksnummer;
import no.nav.foreldrepenger.autotest.klienter.vtp.journalpost.JournalforingKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.vent.Vent;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.JournalRepositoryImpl;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

public class Fordel extends Aktoer{
    
    /*
     * Klienter
     */
    FordelKlient fordelKlient;
    BehandlingerKlient behandlingerKlient;
    
    //Vtp Klienter
    JournalforingKlient journalpostKlient;
    
    public Fordel() {
        fordelKlient = new FordelKlient(session);
        behandlingerKlient = new BehandlingerKlient(session);
        journalpostKlient = new JournalforingKlient(new HttpSession());
    }
    
    
    /*
     * Sender inn søkand og returnerer saksinformasjon
     */
    public long sendInnSøknad(Soeknad søknad, TestscenarioDto scenario, DokumenttypeId dokumenttypeId) throws Exception {
        String xml = ForeldrepengesoknadBuilder.tilXML(søknad);
        
        JournalpostModell journalpostModell = JournalpostModellGenerator.lagJournalpost(xml, scenario.getPersonopplysninger().getSøkerIdent(), dokumenttypeId);
        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();
        //String journalpostId = journalRepository.leggTilJournalpost(journalpostModell);
        String journalpostId = journalpostKlient.journalfør(journalpostModell).getJournalpostId();
        
        //TODO: Avled behandlingstema fra kode
        String behandlingstemaOffisiellKode = "ab0050";
        String dokumentTypeIdOffisiellKode = dokumenttypeId.getKode();
        
        String aktørId  = scenario.getPersonopplysninger().getSøkerAktørIdent();

        long sakId = sendInnJournalpost(xml, journalpostId, behandlingstemaOffisiellKode, dokumentTypeIdOffisiellKode, aktørId);
        journalpostModell.setSakId(String.valueOf(sakId));
        System.out.println("Opprettet søknad: " + sakId);
        
        Vent.til(() -> {
            return behandlingerKlient.alle(sakId).size() > 0;
        }, 15, "Saken hadde ingen behandlinger");
        
        
        return sakId;
    }
    
    public long sendInnSøknad(ForeldrepengesoknadBuilder builder,TestscenarioDto scenario, DokumenttypeId dokumenttypeId) throws Exception{
        return sendInnSøknad(builder.build(), scenario, dokumenttypeId);
    }
    
    /*
     * Sender inn søknad og returnerer saksinformasjon
     */
    public long sendInnPapirsøkand(Soeknad søknad, String fnr) {
        return -1;
    }


    /*
     * Sender inn inntektsmelding og returnerer saksnummer
     */
    public long sendInnInntektsmelding(InntektsmeldingBuilder inntektsmelding, TestscenarioDto scenario) throws IOException {
        String xml = inntektsmelding.createInntektesmeldingXML();
        String aktørId  = scenario.getPersonopplysninger().getSøkerAktørIdent();
        String behandlingstemaOffisiellKode = "ab0047";
        String dokumentTypeIdOffisiellKode = DokumenttypeId.INNTEKTSMELDING.getKode();
        
        JournalpostModell journalpostModell = JournalpostModellGenerator.lagJournalpost(xml, scenario.getPersonopplysninger().getSøkerIdent(), DokumenttypeId.INNTEKTSMELDING);
        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();
        String journalpostId = journalRepository.leggTilJournalpost(journalpostModell);
        
        long sakId = sendInnJournalpost(xml, journalpostId, behandlingstemaOffisiellKode, dokumentTypeIdOffisiellKode, aktørId);
        journalpostModell.setSakId(String.valueOf(sakId));
        return sakId;
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
        journalpostMottak.setDokumentTypeIdOffisiellKode(dokumentTypeIdOffisiellKode);
        
        if(null != xml) {
            journalpostMottak.setPayloadXml(new String(Base64.getUrlEncoder().withoutPadding().encode(xml.getBytes())));
            journalpostMottak.setPayloadLength(xml.length());
        }
        
        fordelKlient.journalpost(journalpostMottak);
        
        return saksnummer.saksnummer;
    }
}
