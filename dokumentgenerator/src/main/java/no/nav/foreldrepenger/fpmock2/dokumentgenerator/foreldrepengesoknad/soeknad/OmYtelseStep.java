package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import no.nav.vedtak.felles.xml.soeknad.felles.v1.Ytelse;

public interface OmYtelseStep <T extends OmYtelseStep<T>>{
    T withForeldrepengerYtelse(Ytelse omYtelse);
    T withEndringssoeknadYtelse(Ytelse omYtelse);
    T withEngangsstoenadYtelse(Ytelse omYtelse);
}
