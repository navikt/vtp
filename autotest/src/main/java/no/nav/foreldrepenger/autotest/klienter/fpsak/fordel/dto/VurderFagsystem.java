package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VurderFagsystem {
    public String journalpostId;
    public boolean strukturertSøknad ;
    public String aktørId ;
    public String behandlingstemaOffisiellKode ;
    public List<String> adopsjonsBarnFodselsdatoer;
    public String barnTermindato;
    public String barnFodselsdato;
    public String omsorgsovertakelsedato;
    public String årsakInnsendingInntektsmelding;
    public String saksnummer;
    public String annenPart;
}
