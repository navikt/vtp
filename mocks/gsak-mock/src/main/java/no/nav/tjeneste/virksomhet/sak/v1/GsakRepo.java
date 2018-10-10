package no.nav.tjeneste.virksomhet.sak.v1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonModell;
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
    private AtomicInteger sakIder;

    public GsakRepo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Mdkm");
        sakIder = new AtomicInteger(Integer.parseInt(LocalDateTime.now().format(formatter)) * 100);
        bySakId = new HashMap<>();
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
        // TODO cache disse?
        Integer oppgavenrGenerert = new Random().nextInt(899 + 1) + 100;
        String oppgaveId =  sakId + String.valueOf(oppgavenrGenerert);
        return oppgaveId;
    }
}
