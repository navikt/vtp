package no.nav.foreldrepenger.vtp.testmodell.repo;

import java.util.List;
import java.util.UUID;

import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.OppgaveModell;
import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.SakModell;

public interface ArbeidsgiverPortalRepository {

    UUID nySak(String grupperingsid,
               String merkelapp,
               String virksomhetsnummer,
               String tittel,
               String lenke,
               String overstyrtStatus);

    UUID nyOppgave(String grupperingsid,
                   String merkelapp,
                   String virksomhetsnummer,
                   String tekst,
                   String lenke);

    UUID utførOppgave(String id);

    UUID utgåttOppgave(String id);

    UUID nyStatusSak(String id, SakModell.SakStatus status, String overstyrtStatusTekst);

    UUID tilleggsinformasjonSak(String id, String tilleggsinformasjon);

    UUID slettSak(String id);

    List<SakModell> hentSaker();

    OppgaveModell hentOppgaveFor(String grupperingsid);
}
