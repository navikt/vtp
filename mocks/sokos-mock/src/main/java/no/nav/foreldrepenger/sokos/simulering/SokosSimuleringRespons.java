package no.nav.foreldrepenger.sokos.simulering;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SokosSimuleringRespons(@JsonProperty("OsSimulerBeregningOperationResponse") Operation operation) {

    public record Operation(@JsonProperty("ZS1OCont") Container container) { }

    public record Container(Respons responsSimulering) { }

    public record Respons(int statusKode, String statusMelding, Beregning beregning) { }

    public record Beregning(String gjelderId, String gjelderNavn, String datoBeregnet, String kodeFaggruppe,
                            BigDecimal belopTotalt, List<PeriodeStoppnivaa> periodeStoppnivaa) { }

    public record PeriodeStoppnivaa(BeregningsPeriode beregningsPeriode, Stoppnivaa beregningStoppnivaa) { }

    public record BeregningsPeriode(String periodeFom, String periodeTom) { }

    public record Stoppnivaa(String kodeFagomraade, String utbetalesTilId, String datoForfall, List<Detaljer> stoppnivaaDetaljer) { }

    public record Detaljer(String faktiskFom, String faktiskTom, BigDecimal belop, String typeKlasse) { }
}
