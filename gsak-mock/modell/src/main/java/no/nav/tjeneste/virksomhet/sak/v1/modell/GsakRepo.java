package no.nav.tjeneste.virksomhet.sak.v1.modell;


import no.nav.tjeneste.virksomhet.person.v2.modell.TpsRepo;
import no.nav.tjeneste.virksomhet.sak.v1.binding.FinnSakUgyldigInput;
import no.nav.tjeneste.virksomhet.sak.v1.binding.HentSakSakIkkeFunnet;
import no.nav.tjeneste.virksomhet.sak.v1.feil.SakIkkeFunnet;
import no.nav.tjeneste.virksomhet.sak.v1.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Aktoer;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Fagomraader;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Fagsystemer;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Person;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Sak;
import no.nav.tjeneste.virksomhet.sak.v1.meldinger.FinnSakRequest;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GsakRepo {

    public static final String FAGOMRÅDE_KODE = "FOR";
    public static final String FAGSYSTEM_KODE = "FS22";

    private Map<String, Sak> repository;

    public GsakRepo() {
        repository = new HashMap<>();
        loadRepoData();
    }

    public Sak hentSak(String sakId) throws HentSakSakIkkeFunnet {
        if (repository.containsKey(sakId)) {
            return repository.get(sakId);
        } else {
            throw new HentSakSakIkkeFunnet("Finner ikke sak " + sakId, new SakIkkeFunnet());
        }
    }

    public List<Sak> finnSak(FinnSakRequest request) throws FinnSakUgyldigInput {

        Aktoer bruker = request.getBruker();
        Fagsystemer fagsystem = request.getFagsystem();
        String fagsystemSakID = request.getFagsystemSakId();
        List<Fagomraader> fagomraadeListe = request.getFagomraadeListe();

        if (bruker == null && (request.getFagsystemSakId() == null || fagsystemSakID == null)) {
            throw new FinnSakUgyldigInput("enten Bruker eller FagsystemSakID+Fagsystem må være satt", new UgyldigInput());
        }

        return repository.values().stream()
                .filter(sak -> bruker == null || sakGjelderBruker(sak, bruker))
                .filter(sak -> fagsystem == null || sak.getFagsystem().getValue().equals(fagsystem.getValue()))
                .filter(sak -> fagsystemSakID == null || sak.getFagsystemSakId().equals(fagsystemSakID))
                .filter(sak -> (fagomraadeListe == null || fagomraadeListe.isEmpty()) || fagomraadeListe.stream()
                        .anyMatch(fagomraade -> fagomraade.getValue().equals(sak.getFagomraade().getValue())))
                .collect(Collectors.toList());
    }

    private boolean sakGjelderBruker(Sak sak, Aktoer bruker) {
        for (Aktoer gjelderBruker : sak.getGjelderBrukerListe()) {
            if (bruker.getIdent().equals(gjelderBruker.getIdent())) {
                return true;
            }
        }
        return false;
    }

    private void loadRepoData() {
        String sakId = String.valueOf(TpsRepo.STD_KVINNE_FNR + "00");
        Sak sak = new Sak();

        sak.setSakId(sakId);
        sak.setEndretAv("z999678");

        Fagomraader fagomraader = new Fagomraader();
        fagomraader.setValue(FAGOMRÅDE_KODE);
        sak.setFagomraade(fagomraader);

        Fagsystemer fagsystemer = new Fagsystemer();
        fagsystemer.setValue(FAGSYSTEM_KODE);
        sak.setFagsystem(fagsystemer);

        Aktoer aktoer = new Person();
        aktoer.setIdent(String.valueOf(TpsRepo.STD_KVINNE_AKTØR_ID));
        sak.getGjelderBrukerListe().add(aktoer);

        try {
            DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();

            LocalDate endringsTP = LocalDate.of(2017, Month.JANUARY, 1);
            XMLGregorianCalendar xmlTP = dataTypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(endringsTP.atStartOfDay(ZoneId.systemDefault())));
            sak.setEndringstidspunkt(xmlTP);

            LocalDate opprettetTP = LocalDate.of(2016, Month.FEBRUARY, 12);
            xmlTP = dataTypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(opprettetTP.atStartOfDay(ZoneId.systemDefault())));
            sak.setOpprettelsetidspunkt(xmlTP);

            LocalDate versjoneringsTP = LocalDate.of(2017, Month.MARCH, 22);
            xmlTP = dataTypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(versjoneringsTP.atStartOfDay(ZoneId.systemDefault())));
            sak.setOpprettelsetidspunkt(xmlTP);

        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        sak.setOpprettetAv("z999876");
        sak.setVersjonsnummer("1");

        sak.setFagsystemSakId("FS22_98");

        repository.put(sakId, sak);
    }
}
