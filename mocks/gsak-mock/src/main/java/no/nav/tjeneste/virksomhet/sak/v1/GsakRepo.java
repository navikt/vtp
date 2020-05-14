package no.nav.tjeneste.virksomhet.sak.v1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.node.ObjectNode;

import no.nav.foreldrepenger.vtp.felles.ConversionUtils;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.tjeneste.virksomhet.sak.v1.binding.HentSakSakIkkeFunnet;
import no.nav.tjeneste.virksomhet.sak.v1.feil.SakIkkeFunnet;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Aktoer;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Fagomraader;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Fagsystemer;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Person;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Sak;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Sakstyper;

public class GsakRepo {

    private static final String SAKSBEHANDLER_IDENT = "MinSaksbehandler";

    private Map<String, Sak> bySakId;
    private Map<Long, ObjectNode> jsonBySakId;
    private AtomicInteger sakIder;
    private AtomicInteger oppgaveIder;

    public GsakRepo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Mdkm");
        sakIder = new AtomicInteger(Integer.parseInt(LocalDateTime.now().format(formatter)) * 100);
        oppgaveIder = new AtomicInteger(Integer.parseInt(LocalDateTime.now().format(formatter)) * 100);
        bySakId = new HashMap<>();
        jsonBySakId = new ConcurrentHashMap<>();
    }

    public ObjectNode leggTilSak(ObjectNode input) {
        Long sakId = (long) sakIder.incrementAndGet();
        input.put("id", sakId);
        jsonBySakId.put(sakId, input);
        return input;
    }

    public ObjectNode hentSak(Long id) {
        return jsonBySakId.get(id);
    }

    public Map<Long, ObjectNode> alleSaker() {
        return jsonBySakId;
    }

    public Sak hentSak(String sakId) throws HentSakSakIkkeFunnet {
        if (bySakId.containsKey(sakId)) {
            return bySakId.get(sakId);
        } else {
            throw new HentSakSakIkkeFunnet("Finner ikke sak " + sakId, new SakIkkeFunnet());
        }
    }

    public Collection<Sak> getAlleSaker() {
        return bySakId.values();
    }

    public Sak leggTilSak(List<PersonModell> person, String fagomrade, String fagsystem, String saktype) {
        String sakId = String.valueOf(sakIder.incrementAndGet());
        Sak sak = new Sak();

        sak.setSakId(sakId);
        sak.setEndretAv(SAKSBEHANDLER_IDENT);

        Fagomraader fagomraader = new Fagomraader();
        fagomraader.setValue(fagomrade);
        sak.setFagomraade(fagomraader);
        Sakstyper sakstyper = new Sakstyper();
        sakstyper.setValue(saktype);

        sak.setSakstype(sakstyper);
        Fagsystemer fagsystemer = new Fagsystemer();
        fagsystemer.setValue(fagsystem);
        sak.setFagsystem(fagsystemer);

        Aktoer aktoer = new Person();
        person.stream().forEach(p -> {
            aktoer.setIdent(p.getAktørIdent());
            sak.getGjelderBrukerListe().add(aktoer);
        });

        LocalDate endringsTP = LocalDate.now().minusDays(10);
        sak.setEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(endringsTP));

        LocalDate opprettetTP = endringsTP.minusDays(10);
        sak.setOpprettelsetidspunkt(ConversionUtils.convertToXMLGregorianCalendar(opprettetTP));

        LocalDate versjoneringsTP = endringsTP.plusDays(5);
        sak.setOpprettelsetidspunkt(ConversionUtils.convertToXMLGregorianCalendar(versjoneringsTP));

        sak.setOpprettetAv(SAKSBEHANDLER_IDENT);
        sak.setVersjonsnummer("1");

        bySakId.put(sakId, sak);

        return sak;
    }

    public String opprettOppgave(String sakId) {

        String oppgaveId =  sakId + String.valueOf(oppgaveIder.incrementAndGet());
        return oppgaveId;
    }
}
