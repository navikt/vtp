package no.nav.vtp.inntektskomponenten;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public record AbonnementHendelse(
        long sekvensnummer,
        String norskident,
        YearMonth maaned,
        LocalDateTime behandlet,
        List<String> filter
) {
}
