package no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Kode {
    public String kodeverk;
    public String kode;
    public String navn;
}
