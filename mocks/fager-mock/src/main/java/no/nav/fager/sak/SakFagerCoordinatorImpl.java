package no.nav.fager.sak;

import java.util.List;

import no.nav.fager.HardDeleteSakVellykket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.fager.NySakResultat;
import no.nav.fager.NySakVellykket;
import no.nav.fager.NyStatusSakVellykket;
import no.nav.fager.TilleggsinformasjonSakVellykket;
import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.SakModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.ArbeidsgiverPortalRepository;

public class SakFagerCoordinatorImpl implements SakFagerCoordinator {
    private static final Logger LOG = LoggerFactory.getLogger(SakFagerCoordinatorImpl.class);

    private final ArbeidsgiverPortalRepository arbeidsgiverPortalRepository;

    public SakFagerCoordinatorImpl(ArbeidsgiverPortalRepository arbeidsgiverPortalRepository) {
        this.arbeidsgiverPortalRepository = arbeidsgiverPortalRepository;
    }

    @Override
    public NySakResultat opprettSak(String grupperingsid,
                                    String merkelapp,
                                    String virksomhetsnummer,
                                    String tittel,
                                    String lenke,
                                    SakStatus initiellStatus,
                                    String overstyrStatustekstMed) {
        var sakUuid = arbeidsgiverPortalRepository.nySak(grupperingsid, merkelapp, virksomhetsnummer, tittel, lenke, overstyrStatustekstMed);
        LOG.info("FAGER: nySak med id: {}", sakUuid);
        return new NySakVellykket(sakUuid.toString());
    }

    @Override
    public NyStatusSakVellykket nyStatusSak(String id, SakModell.SakStatus nyStatus, String overstyrStatustekstMed) {
        var sakId = arbeidsgiverPortalRepository.nyStatusSak(id, nyStatus, overstyrStatustekstMed);
        LOG.info("FAGER: nyStatusSak id: {}, ny status: {}", sakId, nyStatus);
        return new NyStatusSakVellykket(sakId.toString(), List.of());
    }

    @Override
    public TilleggsinformasjonSakVellykket tilleggsinformasjonSak(String id, String tilleggsinformasjon) {
        var sakId = arbeidsgiverPortalRepository.tilleggsinformasjonSak(id, tilleggsinformasjon);
        LOG.info("FAGER: tilleggsinformasjonSAK id: {}, info: {}", sakId, tilleggsinformasjon);
        return new TilleggsinformasjonSakVellykket(sakId.toString());
    }

    @Override
    public HardDeleteSakVellykket hardDeleteSak(String id) {
        var sakId = arbeidsgiverPortalRepository.slettSak(id);
        LOG.info("FAGER: hardDeleteSak id: {}", sakId);
        return new HardDeleteSakVellykket(sakId.toString());
    }
}
