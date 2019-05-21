package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import java.util.List;

import no.nav.vedtak.felles.xml.soeknad.felles.v3.Vedlegg;

public interface PaakrevdeVedlegg <T extends PaakrevdeVedlegg<T>>{
    T withPaakrevdeVedlegg(List<Vedlegg> paakrevdeVedlegg);
}
