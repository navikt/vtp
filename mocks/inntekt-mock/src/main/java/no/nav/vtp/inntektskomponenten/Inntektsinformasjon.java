package no.nav.vtp.inntektskomponenten;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public record Inntektsinformasjon(YearMonth maaned, String opplysningspliktig, String underenhet, String norskident, List<Inntekt> inntektListe) {

    public record Inntekt(String type, BigDecimal beloep, String beskrivelse, String skatteOgAvgiftsregel,
                          LocalDate opptjeningsperiodeFom, LocalDate opptjeningsperiodeTom, Tilleggsinformasjon tilleggsinformasjon) { }

    public record Tilleggsinformasjon(String type, LocalDate startdato, LocalDate sluttdato) { }
}
