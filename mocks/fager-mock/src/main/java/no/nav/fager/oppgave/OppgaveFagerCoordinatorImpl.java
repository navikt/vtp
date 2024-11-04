package no.nav.fager.oppgave;

import no.nav.fager.NyOppgaveResultat;
import no.nav.fager.NyOppgaveVellykket;
import no.nav.fager.OppgaveUtfoertVellykket;
import no.nav.fager.OppgaveUtgaattVellykket;

import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.OppgaveModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.ArbeidsgiverPortalRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OppgaveFagerCoordinatorImpl implements OppgaveFagerCoordinator{

    private static final Logger LOG = LoggerFactory.getLogger(OppgaveFagerCoordinatorImpl.class);
    private final ArbeidsgiverPortalRepository arbeidsgiverPortalRepository;

    public OppgaveFagerCoordinatorImpl(ArbeidsgiverPortalRepository arbeidsgiverPortalRepository) {
        this.arbeidsgiverPortalRepository = arbeidsgiverPortalRepository;
    }

    @Override
        public NyOppgaveResultat opprettOppgave(String grupperingsid,
                String merkelapp,
                String virksomhetsnummer,
                String tekst,
                String lenke) {
            var oppgaveUuid = arbeidsgiverPortalRepository.nyOppgave(grupperingsid, merkelapp, virksomhetsnummer, tekst, lenke);
            LOG.info("FAGER: nyOppgave med id: {}", oppgaveUuid);
            return new NyOppgaveVellykket(oppgaveUuid.toString(), List.of(), null);
        }

        @Override
        public OppgaveUtfoertVellykket utførttOppgave(String id) {
            var oppgaveUuid = arbeidsgiverPortalRepository.utførOppgave(id);
            LOG.info("FAGER: oppgaveUtført med id: {}", oppgaveUuid);
            return new OppgaveUtfoertVellykket(id);
        }

        @Override
        public OppgaveUtgaattVellykket utgåttOppgave(String id) {
            var oppgaveUuid = arbeidsgiverPortalRepository.utgåttOppgave(id);
            LOG.info("FAGER: utgåttOppgave med id: {}", oppgaveUuid);
            return new OppgaveUtgaattVellykket(id);
        }

    private OppgaveModell.Tilstand mapOppgaveTilstand(OppgaveFagerCoordinator.Tilstand status) {
        return switch (status) {
            case NY -> OppgaveModell.Tilstand.NY;
            case UTGAATT -> OppgaveModell.Tilstand.UTGAATT;
            case UTFOERT -> OppgaveModell.Tilstand.UTFOERT;
        };
    }
}
