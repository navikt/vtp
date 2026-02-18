package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record InntektskomponentModell(List<Inntektsperiode> inntektsperioder) {

    public InntektskomponentModell() {
        this(null);
    }

    @JsonCreator
    public InntektskomponentModell(List<Inntektsperiode> inntektsperioder) {
        this.inntektsperioder = Optional.ofNullable(inntektsperioder).orElse(List.of());
    }

    @JsonIgnore
    public List<Inntektsperiode> getInntektsperioderSplittMånedlig() {
        return inntektsperioder.stream()
                .flatMap(ip -> splittInntektsperioderTilMånedligeIntervall(ip).stream())
                .collect(Collectors.toList());
    }

    private List<Inntektsperiode> splittInntektsperioderTilMånedligeIntervall(Inntektsperiode ip) {
        List<Inntektsperiode> inntektsperioderPaaMaaned = new ArrayList<>();
        LocalDate tomDato = (ip.tom() != null) ? ip.tom() : LocalDate.now();
        LocalDate dateCounter = ip.fom().withDayOfMonth(1);
        while (!dateCounter.isEqual(tomDato.plusMonths(1).withDayOfMonth(1))) {
            inntektsperioderPaaMaaned.add(new Inntektsperiode(dateCounter.withDayOfMonth(1), dateCounter.withDayOfMonth(dateCounter.lengthOfMonth()),
                    ip.aktorId(), ip.beløp(), ip.orgnr(), ip.inntektType(), ip.inntektFordel(), ip.beskrivelse(),
                    ip.inntektYtelseType(), ip.skatteOgAvgiftsregel(),
                    ip.inngaarIGrunnlagForTrekk(), ip.utloeserArbeidsgiveravgift(), ip.arbeidsgiver()));
            dateCounter = dateCounter.plusMonths(1);
        }
        return inntektsperioderPaaMaaned;
    }

}
