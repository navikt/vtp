package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.OppgaveModell;
import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.SakModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.ArbeidsgiverPortalRepository;

public class ArbeidsgiverPortalRepositoryImpl implements ArbeidsgiverPortalRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ArbeidsgiverPortalRepositoryImpl.class);

    private ConcurrentMap<UUID, SakModell> saker;
    private ConcurrentMap<String, SakModell> sakerGrupperingsId;
    private ConcurrentMap<UUID, OppgaveModell> oppgaver;
    private ConcurrentMap<String, OppgaveModell> oppgaverGrupperingsId;

    private static ArbeidsgiverPortalRepository instance;

    public static synchronized ArbeidsgiverPortalRepository getInstance() {
        if (instance == null) {
            instance = new ArbeidsgiverPortalRepositoryImpl();
        }
        return instance;
    }

    private ArbeidsgiverPortalRepositoryImpl() {
        saker = new ConcurrentHashMap<>();
        oppgaver = new ConcurrentHashMap<>();
        sakerGrupperingsId = new ConcurrentHashMap<>();
        oppgaverGrupperingsId = new ConcurrentHashMap<>();
    }

    @Override
    public UUID nySak(String grupperingsid,
                      String merkelapp,
                      String virksomhetsnummer,
                      String tittel,
                      String lenke,
                      String overstyrtStatus) {
        Objects.requireNonNull(grupperingsid, "grupperingsid");
        if (sakerGrupperingsId.containsKey(grupperingsid)) {
            LOG.warn("FAGER repo: saken finnes allerede: {}", grupperingsid);
            throw new IllegalStateException("DuplikatGrupperingsid");
        }
        var uuid = UUID.randomUUID();
        var nySak = opprettSak(grupperingsid, merkelapp, virksomhetsnummer, tittel, lenke, overstyrtStatus, uuid);
        saker.put(uuid, nySak);
        sakerGrupperingsId.put(grupperingsid, nySak);
        return uuid;
    }

    private static SakModell opprettSak(String grupperingsid,
                                        String merkelapp,
                                        String virksomhetsnummer,
                                        String tittel,
                                        String lenke,
                                        String overstyrtStatus,
                                        UUID uuid) {
        return new SakModell(uuid, grupperingsid, merkelapp, virksomhetsnummer, tittel, lenke, SakModell.SakStatus.UNDER_BEHANDLING, overstyrtStatus, null,
                LocalDateTime.now(), null);
    }

    @Override
    public UUID nyOppgave(String grupperingsid,
                          String merkelapp,
                          String virksomhetsnummer,
                          String tekst,
                          String lenke) {
        Objects.requireNonNull(grupperingsid, "grupperingsid");
        if (oppgaverGrupperingsId.containsKey(grupperingsid)) {
            LOG.warn("FAGER repo: oppgaven finnes allerede: {}", grupperingsid);
            throw new IllegalStateException("DuplikatEksternIdOgMerkelapp");
        }
        var uuid = UUID.randomUUID();
        var nyOppgave = opprettOppgave(grupperingsid, merkelapp, virksomhetsnummer, tekst, lenke, uuid);
        oppgaver.put(uuid, nyOppgave);
        oppgaverGrupperingsId.put(grupperingsid, nyOppgave);
        return uuid;
    }

    private static OppgaveModell opprettOppgave(String grupperingsid,
                                                String merkelapp,
                                                String virksomhetsnummer,
                                                String tittel,
                                                String lenke,
                                                UUID uuid) {
        return new OppgaveModell(uuid, grupperingsid, merkelapp, uuid.toString(), virksomhetsnummer, tittel, lenke, OppgaveModell.Tilstand.NY,
                LocalDateTime.now(), null);
    }

    @Override
    public UUID utførOppgave(String id) {
        var oppgave = oppgaver.get(UUID.fromString(id));
        if (oppgave == null) {
            LOG.warn("Fager: Oppgave med {} finnes ikke", id);
            throw new IllegalStateException("OppgavenFinnesIkke");
        }
        oppgave.setTilstand(OppgaveModell.Tilstand.UTFOERT);
        oppgave.setEndretTid(LocalDateTime.now());

        return oppgave.id();
    }

    @Override
    public UUID utgåttOppgave(String id) {
        var oppgave = oppgaver.get(UUID.fromString(id));
        if (oppgave == null) {
            LOG.warn("Fager: Oppgave med {} finnes ikke", id);
            throw new IllegalStateException("OppgavenFinnesIkke");
        }
        oppgave.setTilstand(OppgaveModell.Tilstand.UTGAATT);
        oppgave.setEndretTid(LocalDateTime.now());
        return oppgave.id();
    }

    @Override
    public UUID nyStatusSak(String id, SakModell.SakStatus status, String overstyrtStatusTekst) {
        var sak = saker.get(UUID.fromString(id));
        if (sak == null) {
            LOG.warn("Fager: Sak med {} finnes ikke", id);
            throw new IllegalStateException("SakFinnesIkke");
        }
        sak.setStatus(status);
        sak.setOverstyrtStatustekst(overstyrtStatusTekst);
        sak.setEndretTid(LocalDateTime.now());

        return sak.id();
    }

    @Override
    public UUID tilleggsinformasjonSak(String id, String tilleggsinformasjon) {
        var sak = saker.get(UUID.fromString(id));
        if (sak == null) {
            LOG.warn("Fager: Sak med {} finnes ikke", id);
            throw new IllegalStateException("SakFinnesIkke");
        }
        sak.setOverstyrtTillegsinformasjon(tilleggsinformasjon);
        sak.setEndretTid(LocalDateTime.now());
        return sak.id();
    }

    @Override
    public UUID slettSak(String id) {
        var sak = saker.get(UUID.fromString(id));
        if (sak == null) {
            LOG.warn("Fager: Sak med {} finnes ikke", id);
            throw new IllegalStateException("SakFinnesIkke");
        }
        var sakId = sak.id();
        var sakGrupperingsId = sak.grupperingsid();

        sakerGrupperingsId.remove(sakGrupperingsId);
        saker.remove(sakId);

        return sakId;
    }

    @Override
    public List<SakModell> hentSaker() {
        return saker.values().stream().sorted(Comparator.comparing(SakModell::opprettetTid)).toList();
    }

    @Override
    public OppgaveModell hentOppgaveFor(String grupperingsId) {
        return oppgaverGrupperingsId.get(grupperingsId);
    }
}
