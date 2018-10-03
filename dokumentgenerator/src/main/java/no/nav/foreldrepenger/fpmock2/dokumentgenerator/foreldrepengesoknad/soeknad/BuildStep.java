package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

public interface BuildStep{
    Soeknad build();
}
