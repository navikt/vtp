package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun;

import java.time.LocalDate;
import java.util.List;

public record PgiFolketrygdenResponse(String norskPersonidentifikator, Integer inntektsaar, List<Pgi> pensjonsgivendeInntekt) {
    public record Pgi(Skatteordning skatteordning, LocalDate datoForFastsetting, Long pensjonsgivendeInntektAvLoennsinntekt,
                      Long pensjonsgivendeInntektAvLoennsinntektBarePensjonsdel, Long pensjonsgivendeInntektAvNaeringsinntekt,
                      Long pensjonsgivendeInntektAvNaeringsinntektFraFiskeFangstEllerFamiliebarnehage) {}

    public enum Skatteordning {
        FASTLAND, SVALBARD
    }

    @Override
    public String toString() {
        return "PgiFolketrygdenResponse{" + "inntektsaar=" + inntektsaar + ", pensjonsgivendeInntekt=" + pensjonsgivendeInntekt + '}';
    }
}
