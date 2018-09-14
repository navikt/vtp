package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import no.nav.vedtak.felles.xml.soeknad.felles.v1.Vedlegg;

import java.util.List;

public interface AndreVedleggStep {
    PaakrevdeVedlegg withAndreVedlegg(List<Vedlegg> andreVedlegg);
}
