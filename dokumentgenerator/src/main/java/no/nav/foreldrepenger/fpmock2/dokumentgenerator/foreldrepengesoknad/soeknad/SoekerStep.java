package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import no.nav.vedtak.felles.xml.soeknad.felles.v1.Bruker;

public interface SoekerStep <T extends SoekerStep<T>>{
    T withSoeker(Bruker soeker);
}
