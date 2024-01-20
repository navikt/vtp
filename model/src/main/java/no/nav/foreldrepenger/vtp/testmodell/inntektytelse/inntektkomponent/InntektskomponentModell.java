package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record InntektskomponentModell(List<Inntektsperiode> inntektsperioder,
                                      List<FrilansArbeidsforholdsperiode> frilansarbeidsforholdperioder) {

    public InntektskomponentModell() {
        this(null, null);
    }

    @JsonCreator
    public InntektskomponentModell(List<Inntektsperiode> inntektsperioder, List<FrilansArbeidsforholdsperiode> frilansarbeidsforholdperioder) {
        this.inntektsperioder = Optional.ofNullable(inntektsperioder).orElse(List.of());
        this.frilansarbeidsforholdperioder = Optional.ofNullable(frilansarbeidsforholdperioder).orElse(List.of());
    }

    @JsonIgnore
    public List<FrilansArbeidsforholdsperiode> getFrilansarbeidsforholdperioderSplittMånedlig() {
        return frilansarbeidsforholdperioder.stream()
                .flatMap(ip -> splittFrilansArbeidsforholdTilMånedligeIntervall(ip).stream())
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Inntektsperiode> getInntektsperioderSplittMånedlig() {
        return inntektsperioder.stream()
                .flatMap(ip -> splittInntektsperioderTilMånedligeIntervall(ip).stream())
                .collect(Collectors.toList());
    }

    private List<FrilansArbeidsforholdsperiode> splittFrilansArbeidsforholdTilMånedligeIntervall(FrilansArbeidsforholdsperiode fap) {
        List<FrilansArbeidsforholdsperiode> frilansArbeidsforholdsperioderPerMåned = new ArrayList<>();
        LocalDate tomDato = (fap.frilansTom() != null) ? fap.frilansTom() : LocalDate.now();
        LocalDate dateCounter = fap.frilansFom().withDayOfMonth(1);
        while (!dateCounter.isEqual(tomDato.withDayOfMonth(1))) {
            frilansArbeidsforholdsperioderPerMåned.add(new FrilansArbeidsforholdsperiode(dateCounter.withDayOfMonth(1),
                    dateCounter.withDayOfMonth(dateCounter.lengthOfMonth()),
                    fap.orgnr(), fap.stillingsprosent(), fap.aktorId(), fap.arbeidsgiver()));
            dateCounter = dateCounter.plusMonths(1);
        }
        return frilansArbeidsforholdsperioderPerMåned;
    }

    private List<Inntektsperiode> splittInntektsperioderTilMånedligeIntervall(Inntektsperiode ip) {
        List<Inntektsperiode> inntektsperioderPaaMaaned = new ArrayList<>();
        LocalDate tomDato = (ip.tom() != null) ? ip.tom() : LocalDate.now();
        LocalDate dateCounter = ip.fom().withDayOfMonth(1);
        while (!dateCounter.isEqual(tomDato.withDayOfMonth(1))) {
            inntektsperioderPaaMaaned.add(new Inntektsperiode(dateCounter.withDayOfMonth(1), dateCounter.withDayOfMonth(dateCounter.lengthOfMonth()),
                    ip.aktorId(), ip.beløp(), ip.orgnr(), ip.inntektType(), ip.inntektFordel(), ip.beskrivelse(),
                    ip.inntektYtelseType(), ip.skatteOgAvgiftsregel(),
                    ip.inngaarIGrunnlagForTrekk(), ip.utloeserArbeidsgiveravgift(), ip.arbeidsgiver()));
            dateCounter = dateCounter.plusMonths(1);
        }
        return inntektsperioderPaaMaaned;
    }

}
