package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun;

import java.util.List;
import java.util.Optional;

public record Inntektsår(String år, List<Oppføring> oppføring) {

    public Inntektsår(String år, List<Oppføring> oppføring) {
        this.år = år;
        this.oppføring = Optional.ofNullable(oppføring).orElse(List.of());
    }
}
