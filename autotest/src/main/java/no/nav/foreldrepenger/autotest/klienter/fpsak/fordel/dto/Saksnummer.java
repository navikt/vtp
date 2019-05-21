package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Saksnummer {

    public long saksnummer;

    public Saksnummer(){}

    public Saksnummer(Long saksnummer){
        this.saksnummer = saksnummer;
    }



}
