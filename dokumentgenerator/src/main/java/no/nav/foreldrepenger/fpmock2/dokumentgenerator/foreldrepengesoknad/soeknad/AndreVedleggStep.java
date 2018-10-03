package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import no.nav.vedtak.felles.xml.soeknad.felles.v1.Vedlegg;

import java.util.List;

public interface AndreVedleggStep <T extends AndreVedleggStep<T>>{
    T withAndreVedlegg(List<Vedlegg> andreVedlegg);
}
