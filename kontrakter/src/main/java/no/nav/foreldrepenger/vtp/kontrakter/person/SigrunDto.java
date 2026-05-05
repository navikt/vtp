package no.nav.foreldrepenger.vtp.kontrakter.person;

import java.util.List;

public record SigrunDto(List<InntektsårDto> inntektår) {
    public record InntektsårDto(Integer år, Integer beløp) {
    }
}
