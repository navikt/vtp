package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Beregningsresultat {
    public int beregnetTilkjentYtelse;
    public int satsVerdi;
    public int antallBarn;
}
