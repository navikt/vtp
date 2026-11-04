package no.nav.foreldrepenger.sokos.simulering;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SokosSimuleringRequest(@JsonProperty("OsSimulerBeregningOperation") Operation operation) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Operation(@JsonProperty("ZS1ICont") Container container) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Container(RequestSimulering requestSimulering) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RequestSimulering(Oppdrag oppdrag, List<Oppdragslinje> oppdragslinje) {
        public RequestSimulering {
            oppdragslinje = Optional.ofNullable(oppdragslinje).orElse(List.of());
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Oppdrag(String kodeEndring, String kodeFagomraade, String fagsystemId, String oppdragGjelderId, String saksbehId) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Oppdragslinje(String kodeEndringLinje, LocalDate datoStatusFom, String kodeKlassifik,
                                LocalDate datoVedtakFom, LocalDate datoVedtakTom, BigDecimal sats, String typeSats,
                                List<RefusjonsInfo> refusjonsInfo) {
        public Oppdragslinje {
            refusjonsInfo = Optional.ofNullable(refusjonsInfo).orElse(List.of());
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RefusjonsInfo(String refunderesId, LocalDate maksDato, LocalDate datoFom) { }
}
