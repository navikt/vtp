package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.time.LocalDate;
import java.util.List;

public record GrunnlagDto(Ytelse ytelse, LocalDate fom, LocalDate tom, Status status, LocalDate fødselsdatoBarn, List<Vedtak> vedtak) {
    public enum Ytelse {
        SP, BS
    }

    public enum Status {
        LØPENDE, AVSLUTTET, I;
    }

    public record Vedtak(LocalDate fom, LocalDate tom, Integer utbetalingsgrad) {
    }
}
