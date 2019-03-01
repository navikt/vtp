package no.nav.foreldrepenger.autotest.aktoerer.fordel;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugSenderInnDokument;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.BehandlingerKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.FagsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.FordelKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.JournalpostId;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.JournalpostKnyttning;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.JournalpostMottak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.OpprettSak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.Saksnummer;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.HistorikkKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.autotest.klienter.vtp.journalpost.JournalforingKlient;
import no.nav.foreldrepenger.autotest.klienter.vtp.sak.SakKlient;
import no.nav.foreldrepenger.autotest.klienter.vtp.sak.dto.OpprettSakRequestDTO;
import no.nav.foreldrepenger.autotest.klienter.vtp.sak.dto.OpprettSakResponseDTO;
import no.nav.foreldrepenger.autotest.klienter.vtp.tpsFeed.TpsFeedKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.vent.Vent;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.PersonhendelseDto;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.ControllerHelper;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Dokumentkategori;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.v3.Soeknad;

public class Fordel extends Aktoer {

    /*
     * Klienter
     */
    FordelKlient fordelKlient;
    BehandlingerKlient behandlingerKlient;
    SakKlient sakKlient;
    FagsakKlient fagsakKlient;
    HistorikkKlient historikkKlient;
    TpsFeedKlient tpsFeedKlient;


    //Vtp Klienter
    JournalforingKlient journalpostKlient;

    public Fordel() {
        fordelKlient = new FordelKlient(session);
        behandlingerKlient = new BehandlingerKlient(session);
        journalpostKlient = new JournalforingKlient(new HttpSession());
        sakKlient = new SakKlient(session);
        fagsakKlient = new FagsakKlient(session);
        historikkKlient = new HistorikkKlient(session);
        tpsFeedKlient = new TpsFeedKlient(session);
    }

    /*
     * Sender inn søkand og returnerer saksinformasjon
     */
    @Step("Sender inn søknad")
    public long sendInnSøknad(Soeknad søknad, String aktørId, String fnr, DokumenttypeId dokumenttypeId, Long saksnummer) throws Exception {
        String xml = null;
        if (null != søknad) {
            xml = ForeldrepengesoknadBuilder.tilXML(søknad);
        }

        JournalpostModell journalpostModell = JournalpostModellGenerator.lagJournalpost(xml == null ? "" : xml, fnr, dokumenttypeId);
        if (saksnummer != null && saksnummer.longValue() != 0L) {
            journalpostModell.setSakId(saksnummer.toString());
        }
        String journalpostId = journalpostKlient.journalfør(journalpostModell).getJournalpostId();

        String behandlingstemaOffisiellKode = ControllerHelper.translateSøknadDokumenttypeToBehandlingstema(dokumenttypeId).getKode();
        String dokumentTypeIdOffisiellKode = dokumenttypeId.getKode();
        debugSenderInnDokument("Foreldrepengesøknad", xml);
        long sakId = sendInnJournalpost(xml, journalpostId, behandlingstemaOffisiellKode, dokumentTypeIdOffisiellKode, "SOK", aktørId, saksnummer);
        journalpostModell.setSakId(String.valueOf(sakId));
        System.out.println("Opprettet søknad: " + sakId);

        Vent.til(() -> {
            List<Behandling> behandlinger = behandlingerKlient.alle(sakId);
            //TODO: Gjøre denne asynkron
            if (behandlinger.size() > 1) {
                Thread.sleep(5000);
            }
            return !behandlinger.isEmpty() && behandlingerKlient.statusAsObject(behandlinger.get(0).id, null) == null;
        }, 60, "Saken hadde ingen behandlinger");

        if (DokumenttypeId.FORELDREPENGER_ENDRING_SØKNAD.equals(dokumenttypeId)) {
            //TODO: Vent.til fungerer ikke med endringssøknad. Venter ikke til behandlingen er opprettet
            Thread.sleep(5000);
        }

        return sakId;
    }

    /*
     * Sender inn søknad og opretter ny sak
     */

    public long sendInnSøknad(Soeknad søknad, String aktørId, String fnr, DokumenttypeId dokumenttypeId) throws Exception {
        return sendInnSøknad(søknad, aktørId, fnr, dokumenttypeId, null);
    }

    public long sendInnSøknad(Soeknad søknad, TestscenarioDto scenario, DokumenttypeId dokumenttypeId) throws Exception {
        return sendInnSøknad(søknad, scenario, dokumenttypeId, null);
    }

    public long sendInnSøknad(Soeknad søknad, TestscenarioDto scenario, DokumenttypeId dokumenttypeId, Long saksnummer) throws Exception {
        String aktørId = scenario.getPersonopplysninger().getSøkerAktørIdent();
        String fnr = scenario.getPersonopplysninger().getSøkerIdent();
        return sendInnSøknad(søknad, aktørId, fnr, dokumenttypeId, saksnummer);
    }

    /*
     * Sender inn søknad og returnerer saksinformasjon
     */
    @Step("Sender inn papirsøknad")
    public long sendInnPapirsøkand(TestscenarioDto testscenario, DokumenttypeId dokumenttypeId) throws Exception {
        return sendInnSøknad(null, testscenario, dokumenttypeId);
    }

    /*
     * Opprett sak
     */

    public String opprettSak(TestscenarioDto testscenarioDto, String fagområde) throws IOException {

        List<String> aktører = new ArrayList<>();
        aktører.add(testscenarioDto.getPersonopplysninger().getSøkerIdent());
        if (testscenarioDto.getPersonopplysninger().getAnnenpartIdent() != null) {
            aktører.add(testscenarioDto.getPersonopplysninger().getAnnenpartIdent());
        }

        OpprettSakRequestDTO request = new OpprettSakRequestDTO(aktører, fagområde, "FS22", "MFS");


        OpprettSakResponseDTO responseDTO = sakKlient.opprettSak(request);
        return responseDTO.getSaksnummer();

    }


    /*
     * Sender inn inntektsmelding og returnerer saksnummer
     */
    @Step("Sender inn inntektsmelding")
    public long sendInnInntektsmelding(InntektsmeldingBuilder inntektsmelding, String aktørId, String fnr, Long gammeltSaksnummer) throws Exception {
        String xml = inntektsmelding.createInntektesmeldingXML();
        debugSenderInnDokument("Inntektsmelding", xml);
        String behandlingstemaOffisiellKode = "ab0047";
        String dokumentKategori = Dokumentkategori.IKKE_TOLKBART_SKJEMA.getKode();
        String dokumentTypeIdOffisiellKode = DokumenttypeId.INNTEKTSMELDING.getKode();

        JournalpostModell journalpostModell = JournalpostModellGenerator.lagJournalpost(xml, fnr, DokumenttypeId.INNTEKTSMELDING);
        String journalpostId = journalpostKlient.journalfør(journalpostModell).getJournalpostId();

        long nyttSaksnummer = sendInnJournalpost(xml, journalpostId, behandlingstemaOffisiellKode, dokumentTypeIdOffisiellKode, dokumentKategori, aktørId, gammeltSaksnummer);
        journalpostModell.setSakId(String.valueOf(nyttSaksnummer));

        //vent til inntektsmelding er mottatt
        if (gammeltSaksnummer != null) {
            final long saksnummerF = gammeltSaksnummer;
            Vent.til(() -> {
                List<HistorikkInnslag> historikk = historikkKlient.hentHistorikk(saksnummerF);
                return historikk.stream().anyMatch(h -> h.getTekst().equals("Vedlegg mottatt"));
            }, 30, "Saken har ikke mottatt inntektsmeldingen");
        } else {
            Vent.til(() -> {
                return fagsakKlient.søk("" + nyttSaksnummer).size() > 0;
            }, 30, "Oprettet ikke fagsag for inntektsmelding");
        }

        return nyttSaksnummer;
    }

    @Step("Sender inn inntektsmelding")
    public long sendInnInntektsmelding(InntektsmeldingBuilder inntektsmelding, TestscenarioDto testscenario, Long saksnummer) throws Exception {
        return sendInnInntektsmelding(inntektsmelding, testscenario.getPersonopplysninger().getSøkerAktørIdent(), testscenario.getPersonopplysninger().getSøkerIdent(), saksnummer);
    }

    public Long sendInnInntektsmeldinger(List<InntektsmeldingBuilder> inntektsmeldinger, TestscenarioDto scenario) throws Exception {
        Long saksnummer = sendInnInntektsmeldinger(inntektsmeldinger, scenario, null);
        return saksnummer;
    }

    public Long sendInnInntektsmeldinger(List<InntektsmeldingBuilder> inntektsmeldinger, String aktørId, String fnr, Long saksnummer) throws Exception {
        int gammelAntallIM = 0;
        if (saksnummer != null) {
            gammelAntallIM = antallInntektsmeldingerMottatt(saksnummer);
        }
        for (InntektsmeldingBuilder builder : inntektsmeldinger) {
            saksnummer = sendInnInntektsmelding(builder, aktørId, fnr, saksnummer);
            if (inntektsmeldinger.size() > 1) {
                Thread.sleep(4000); //TODO finn ut hva man må vente på her...
            }
        }
        final int gammelAntallIMF = gammelAntallIM;
        final long saksnummerF = saksnummer;
        Vent.til(() -> {
            return (antallInntektsmeldingerMottatt(saksnummerF) - gammelAntallIMF == inntektsmeldinger.size());
        }, 20, "har ikke mottat alle inntektsmeldinger");
        return saksnummer;
    }

    private int antallInntektsmeldingerMottatt(long saksnummer) throws IOException {
        List<HistorikkInnslag> historikk = historikkKlient.hentHistorikk(saksnummer);
        int antall = historikk.stream().filter(h -> h.getTekst().equals("Vedlegg mottatt")).collect(Collectors.toList()).size();
        return antall;
    }

    public Long sendInnInntektsmeldinger(List<InntektsmeldingBuilder> inntektsmeldinger, TestscenarioDto testscenario, Long saksnummer) throws Exception {
        return sendInnInntektsmeldinger(inntektsmeldinger, testscenario.getPersonopplysninger().getSøkerAktørIdent(), testscenario.getPersonopplysninger().getSøkerIdent(), saksnummer);
    }

    public String journalførInnektsmelding(InntektsmeldingBuilder inntektsmelding, TestscenarioDto scenario, Long saksnummer) throws IOException {
        String xml = inntektsmelding.createInntektesmeldingXML();
        String aktørId = scenario.getPersonopplysninger().getSøkerAktørIdent();
        JournalpostModell journalpostModell = JournalpostModellGenerator.lagJournalpost(xml, aktørId, DokumenttypeId.INNTEKTSMELDING);
        journalpostModell.setSakId(saksnummer.toString());
        String id = journalpostKlient.journalfør(journalpostModell).getJournalpostId();
        if (saksnummer != null) {
            journalpostKlient.knyttSakTilJournalpost(id, "" + saksnummer);
        }
        return id;
    }

    @Step("Sender inn klage for bruker")
    public long sendInnKlage(String xmlstring, TestscenarioDto scenario, Long saksnummer) throws IOException {
        String xml = xmlstring;
        String aktørId = scenario.getPersonopplysninger().getSøkerAktørIdent();
        String behandlingstemaOffisiellKode = "ab0047";
        String dokumentKategori = Dokumentkategori.KLAGE_ANKE.getKode();
        String dokumentTypeIdOffisiellKode = DokumenttypeId.KLAGEANKE.getKode();

        JournalpostModell journalpostModell = JournalpostModellGenerator.makeUstrukturertDokumentJournalpost(scenario.getPersonopplysninger().getSøkerIdent(), DokumenttypeId.KLAGEANKE);
        String journalpostId = journalpostKlient.journalfør(journalpostModell).getJournalpostId();

        long sakId = sendInnJournalpost(xml, journalpostId, behandlingstemaOffisiellKode, dokumentTypeIdOffisiellKode, dokumentKategori, aktørId, saksnummer);
        journalpostModell.setSakId(String.valueOf(sakId));
        return sakId;
    }


    /*
     * Sender inn journalpost og returnerer saksnummer
     */
    private Long sendInnJournalpost(String xml, String journalpostId, String behandlingstemaOffisiellKode, String dokumentTypeIdOffisiellKode, String dokumentKategori, String aktørId, Long saksnummer) throws IOException {

        if (saksnummer == null || saksnummer.longValue() == 0L) {
            OpprettSak journalpost = new OpprettSak(journalpostId, behandlingstemaOffisiellKode, aktørId);
            saksnummer = fordelKlient.fagsakOpprett(journalpost).saksnummer;
            journalpostKlient.knyttSakTilJournalpost(journalpostId, saksnummer.toString());
        }


        JournalpostId idDto = new JournalpostId(journalpostId);
        JournalpostKnyttning journalpostKnyttning = new JournalpostKnyttning(new Saksnummer(saksnummer), idDto);
        fordelKlient.fagsakKnyttJournalpost(journalpostKnyttning);


        JournalpostMottak journalpostMottak = new JournalpostMottak("" + saksnummer, journalpostId, LocalDate.now(), behandlingstemaOffisiellKode);
        journalpostMottak.setDokumentTypeIdOffisiellKode(dokumentTypeIdOffisiellKode);
        journalpostMottak.setForsendelseId(UUID.randomUUID().toString());
        journalpostMottak.setDokumentKategoriOffisiellKode(dokumentKategori);

        if (null != xml) {
            journalpostMottak.setPayloadXml(new String(Base64.getUrlEncoder().withoutPadding().encode(xml.getBytes())));
            journalpostMottak.setPayloadLength(xml.length());
        } else {
            journalpostMottak.setPayloadLength(1);
        }

        fordelKlient.journalpost(journalpostMottak);

        return saksnummer;
    }

    /*
     * Opretter en personhendelse
     */
    @Step("Oppretter tps-hendelse")
    public void opprettTpsHendelse(PersonhendelseDto personhendelseDto) throws Exception {
        tpsFeedKlient.leggTilHendelse(personhendelseDto);
    }
}
