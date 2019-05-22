package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import java.util.List;

import no.nav.vedtak.felles.xml.soeknad.felles.v3.Vedlegg;


public interface AndreVedleggStep <T extends AndreVedleggStep<T>>{
    T withAndreVedlegg(List<Vedlegg> andreVedlegg);
}
