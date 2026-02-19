package no.nav.tjeneste.virksomhet.dpsak;

import java.time.LocalDate;
import java.util.List;

public record DagpengerPerioderDto(String personIdent, List<Rettighetsperiode> perioder) {

    public record Rettighetsperiode(LocalDate fraOgMedDato, LocalDate tilOgMedDato, DagpengerKilde kilde, DagpengerType ytelseType) {
    }

    public enum DagpengerType { DAGPENGER_ARBEIDSSOKER_ORDINAER, DAGPENGER_PERMITTERING_ORDINAER, DAGPENGER_PERMITTERING_FISKEINDUSTRI }
}

