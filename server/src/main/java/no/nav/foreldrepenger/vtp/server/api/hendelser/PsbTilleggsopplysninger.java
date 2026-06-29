package no.nav.foreldrepenger.vtp.server.api.hendelser;

import java.time.LocalDate;
import java.util.List;

record PsbTilleggsopplysninger(String pleietrengende, List<Innleggelsesperiode> innleggelsesPerioder) {

    record Innleggelsesperiode(LocalDate fom, LocalDate tom) {}
}
