package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BehandlendeFagsystem {

    public boolean behandlesIVedtaksløsningen;
    public boolean sjekkMotInfotrygd;
    public boolean manuellVurdering;
    public Saksnummer saksnummer;
}
