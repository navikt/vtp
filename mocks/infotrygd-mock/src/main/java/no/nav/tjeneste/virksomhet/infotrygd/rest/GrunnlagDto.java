package no.nav.tjeneste.virksomhet.infotrygd.rest;

import java.time.LocalDate;
import java.util.List;

public record GrunnlagDto(Status status,
                          Tema tema,
                          Integer dekningsgrad,
                          LocalDate fødselsdatoBarn,
                          Arbeidskategori kategori,
                          List<Arbeidsforhold> arbeidsforhold,
                          Periode periode,
                          Behandlingstema behandlingstema,
                          LocalDate identdato,
                          LocalDate iverksatt,
                          LocalDate opphørFom,
                          Integer gradering,
                          LocalDate opprinneligIdentdato,
                          LocalDate registrert,
                          String saksbehandlerId,
                          List<Vedtak> vedtak) {

    public record Status(StatusKode kode, String termnavn) {
    }

    public record Tema(TemaKode kode, String termnavn) {
    }

    public record Arbeidskategori(ArbeidskategoriKode kode, String termnavn) {
    }

    public record Arbeidsforhold(String orgnr, Integer inntekt, Inntektsperiode inntektsperiode, Boolean refusjon) {
    }

    public record Inntektsperiode(InntektsperiodeKode kode, String termnavn) {
    }

    public record Periode(LocalDate fom, LocalDate tom) {
    }

    public record Behandlingstema(BehandlingstemaKode kode, String termnavn) {
    }

    public record Vedtak(Periode periode, Integer utbetalingsgrad, String orgnr, Boolean erRefusjon, Integer dagsats) {
    }
}


