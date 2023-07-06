package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.time.LocalDate;
import java.util.List;

public record ArenaVedtakDto(LocalDate fom, LocalDate tom, VedtakStatus status, Integer dagsats, List<ArenaMeldekort> meldekort) {

    public enum VedtakStatus {
        IVERK,
        OPPRE,
        INNST,
        REGIS,
        MOTAT,
        GODKJ,
        AVSLU,
        INAKT,
        AKTIV
    }
}
