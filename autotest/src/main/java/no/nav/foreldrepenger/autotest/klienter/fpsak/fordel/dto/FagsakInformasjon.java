package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FagsakInformasjon {

    protected String aktørId;
    protected String behandlingstemaOffisiellKode;
}
