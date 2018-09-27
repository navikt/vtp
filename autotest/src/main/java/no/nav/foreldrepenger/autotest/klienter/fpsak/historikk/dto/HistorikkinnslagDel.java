package no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HistorikkinnslagDel {

    protected Hendelse hendelse;
}
