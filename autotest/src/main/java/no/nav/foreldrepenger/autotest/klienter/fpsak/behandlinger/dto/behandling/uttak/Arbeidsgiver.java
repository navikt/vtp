package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arbeidsgiver {

    protected String identifikator;
    protected String navn;
    protected String aktørId;
    protected Boolean virksomhet;
}
