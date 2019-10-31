package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InntektskomponentModell {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("inntektsperioder")
    List<Inntektsperiode> inntektsperioder = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("frilansarbeidsforholdperioder")
    List<FrilansArbeidsforholdsperiode> frilansarbeidsforholdperioder = new ArrayList<>();

    public List<FrilansArbeidsforholdsperiode> getFrilansarbeidsforholdperioder() {
        return frilansarbeidsforholdperioder;
    }

    @JsonIgnore
    public List<FrilansArbeidsforholdsperiode> getFrilansarbeidsforholdperioderSplittMånedlig() {
        return frilansarbeidsforholdperioder.stream().flatMap(ip -> splittFrilansArbeidsforholdTilMånedligeIntervall(ip).stream()).collect(Collectors.toList());
    }

    public List<Inntektsperiode> getInntektsperioder() {
        return inntektsperioder;
    }

    @JsonIgnore
    public List<Inntektsperiode> getInntektsperioderSplittMånedlig() {
        return inntektsperioder.stream().flatMap(ip -> splittInntektsperioderTilMånedligeIntervall(ip).stream()).collect(Collectors.toList());
    }

    public void setFrilansarbeidsforholdperioder(List<FrilansArbeidsforholdsperiode> frilansarbeidsforholdperioder) {
        this.frilansarbeidsforholdperioder.clear();
        this.frilansarbeidsforholdperioder.addAll(frilansarbeidsforholdperioder);
    }

    public void setInntektsperioder(List<Inntektsperiode> inntektsperioder) {
        this.inntektsperioder.clear();
        this.inntektsperioder.addAll(inntektsperioder);
    }

    private List<FrilansArbeidsforholdsperiode> splittFrilansArbeidsforholdTilMånedligeIntervall(FrilansArbeidsforholdsperiode fap) {
        List<FrilansArbeidsforholdsperiode> frilansArbeidsforholdsperioderPerMåned = new ArrayList<>();
        LocalDate tomDato = (fap.getFrilansTom() != null) ? fap.getFrilansTom() : LocalDate.now();
        LocalDate dateCounter = fap.getFrilansFom().withDayOfMonth(1);
        while (!dateCounter.isEqual(tomDato.withDayOfMonth(1))) {
            frilansArbeidsforholdsperioderPerMåned.add(new FrilansArbeidsforholdsperiode(dateCounter.withDayOfMonth(1),
                    dateCounter.withDayOfMonth(dateCounter.lengthOfMonth()),
                    fap.getOrgnr(), fap.getStillingsprosent(), fap.getAktorId(), fap.getPersonligArbeidsgiver()));
            dateCounter = dateCounter.plusMonths(1);
        }
        return frilansArbeidsforholdsperioderPerMåned;
    }

    private List<Inntektsperiode> splittInntektsperioderTilMånedligeIntervall(Inntektsperiode ip) {
        List<Inntektsperiode> inntektsperioderPaaMaaned = new ArrayList<>();
        LocalDate tomDato = (ip.getTom() != null) ? ip.getTom() : LocalDate.now();
        LocalDate dateCounter = ip.getFom().withDayOfMonth(1);
        while (!dateCounter.isEqual(tomDato.withDayOfMonth(1))) {
            inntektsperioderPaaMaaned.add(new Inntektsperiode(dateCounter.withDayOfMonth(1), dateCounter.withDayOfMonth(dateCounter.lengthOfMonth()),
                    ip.getBeløp(), ip.getOrgnr(), ip.getType(), ip.getFordel(), ip.getBeskrivelse(), ip.getSkatteOgAvgiftsregel(),
                    ip.getInngaarIGrunnlagForTrekk(), ip.getUtloeserArbeidsgiveravgift(), ip.getPersonligArbeidsgiver()));
            dateCounter = dateCounter.plusMonths(1);
        }
        return inntektsperioderPaaMaaned;
    }

}
