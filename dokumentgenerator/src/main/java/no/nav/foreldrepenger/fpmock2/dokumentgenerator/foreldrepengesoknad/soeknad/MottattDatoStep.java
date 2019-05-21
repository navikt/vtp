package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import java.time.LocalDate;

public interface MottattDatoStep<T extends MottattDatoStep<T>> {
    T withMottattDato(LocalDate mottattDato);
}
