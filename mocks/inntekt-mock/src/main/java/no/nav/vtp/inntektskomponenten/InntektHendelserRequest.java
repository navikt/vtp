package no.nav.vtp.inntektskomponenten;

import java.util.List;

public record InntektHendelserRequest(long fra,
                                      int antall,
                                      List<String> filter) {
}
