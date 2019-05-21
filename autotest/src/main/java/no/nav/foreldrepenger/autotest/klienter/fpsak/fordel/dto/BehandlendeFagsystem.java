package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BehandlendeFagsystem {

    protected boolean behandlesIVedtaksløsningen;
    protected boolean sjekkMotInfotrygd;
    protected boolean manuellVurdering;
    protected Saksnummer saksnummer;
}
