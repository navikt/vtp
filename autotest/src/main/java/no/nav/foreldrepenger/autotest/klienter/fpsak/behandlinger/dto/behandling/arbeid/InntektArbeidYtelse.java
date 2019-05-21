package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.arbeid;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InntektArbeidYtelse {

    public List<Arbeidsforhold> arbeidsforhold = null;
    
}
