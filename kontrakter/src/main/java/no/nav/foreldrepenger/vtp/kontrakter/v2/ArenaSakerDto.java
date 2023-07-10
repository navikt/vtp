package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.util.List;

public record ArenaSakerDto(YtelseTema tema, SakStatus status, List<ArenaVedtakDto> vedtak) {

    public enum YtelseTema {
        AAP,
        DAG,
        FA,
        EF,
        SP,
        BS
    }

    public enum SakStatus {
        AKTIV,
        INAKT,
        AVSLU
    }
}
