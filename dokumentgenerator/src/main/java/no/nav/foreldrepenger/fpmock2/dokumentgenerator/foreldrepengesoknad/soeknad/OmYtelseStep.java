package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import no.nav.vedtak.felles.xml.soeknad.felles.v1.Ytelse;

public interface OmYtelseStep {
    SoekerStep withForeldrepengerYtelse(Ytelse omYtelse);
    SoekerStep withEndringssoeknadYtelse(Ytelse omYtelse);
    SoekerStep withEngangsstoenadYtelse(Ytelse omYtelse);
}
