package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

public record OmsorgspengerRammemeldingerModell(List<AleneOmOmsorgen> aleneOmOmsorgen,
                                                List<OverføringGitt> overføringerGitt,
                                                List<OverføringFått> overføringerFått,
                                                List<KoronaOverføringGitt> koronaOverføringerGitt,
                                                List<KoronaOverføringFått> koronaOverføringerFått) {

    public OmsorgspengerRammemeldingerModell() {
        this(null, null, null, null, null);
    }

    @JsonCreator
    public OmsorgspengerRammemeldingerModell(List<AleneOmOmsorgen> aleneOmOmsorgen,
                                                List<OverføringGitt> overføringerGitt,
                                                List<OverføringFått> overføringerFått,
                                                List<KoronaOverføringGitt> koronaOverføringerGitt,
                                                List<KoronaOverføringFått> koronaOverføringerFått){
        this.aleneOmOmsorgen = Optional.ofNullable(aleneOmOmsorgen).orElse(List.of());
        this.overføringerGitt = Optional.ofNullable(overføringerGitt).orElse(List.of());
        this.overføringerFått = Optional.ofNullable(overføringerFått).orElse(List.of());
        this.koronaOverføringerGitt = Optional.ofNullable(koronaOverføringerGitt).orElse(List.of());
        this.koronaOverføringerFått = Optional.ofNullable(koronaOverføringerFått).orElse(List.of());
    }
}
