package no.nav.fager.beskjed;

import static java.util.Collections.emptyList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.fager.NyBeskjedResultat;
import no.nav.fager.NyBeskjedVellykket;
import no.nav.foreldrepenger.vtp.testmodell.repo.ArbeidsgiverPortalRepository;

public class BeskjedFagerCoordinatorImpl implements BeskjedFagerCoordinator {
    private static final Logger LOG = LoggerFactory.getLogger(BeskjedFagerCoordinatorImpl.class);

    private final ArbeidsgiverPortalRepository arbeidsgiverPortalRepository;

    public BeskjedFagerCoordinatorImpl(ArbeidsgiverPortalRepository arbeidsgiverPortalRepository) {
        this.arbeidsgiverPortalRepository = arbeidsgiverPortalRepository;
    }

    @Override
    public NyBeskjedResultat opprettBeskjed(String grupperingsid,
                                            String merkelapp,
                                            String virksomhetsnummer,
                                            String tittel,
                                            String lenke) {
        var beskjedUuid = arbeidsgiverPortalRepository.nyBeskjed(grupperingsid, merkelapp, virksomhetsnummer, tittel, lenke);
        LOG.info("FAGER: nyBeskjed med id: {}", beskjedUuid);
        return new NyBeskjedVellykket(beskjedUuid.toString(), emptyList());
    }

}
