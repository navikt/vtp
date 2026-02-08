package no.nav.tjeneste.virksomhet.dpsak;

import java.time.LocalDate;
import java.util.List;

public record DagpengerRettighetsperioder(String personIdent, List<Rettighetsperiode> perioder) {

    public record Rettighetsperiode(LocalDate fraOgMedDato, LocalDate tilOgMedDato, DagpengerKilde kilde, DagpengerType ytelseType) {
    }

    public enum DagpengerKilde { DP_SAK, ARENA }
    public enum DagpengerType { DAGPENGER_ARBEIDSSOKER_ORDINAER, DAGPENGER_PERMITTERING_ORDINAER,DAGPENGER_PERMITTERING_FISKEINDUSTRI }
}

