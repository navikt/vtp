package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.opptjening;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Opptjening {
    public List<OpptjeningAktivitet> opptjeningAktivitetList = null;
}
