package no.nav.vtp.inntektskomponenten;

import java.util.List;

public record InntektBulkResponse(List<InntektBulk> bulk) {
    public record InntektBulk(String filter, List<Inntektsinformasjon> data) { }
}
