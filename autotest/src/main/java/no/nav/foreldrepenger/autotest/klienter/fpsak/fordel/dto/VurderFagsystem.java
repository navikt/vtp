package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VurderFagsystem {
    protected String journalpostId;
    protected boolean strukturertSøknad ;
    protected String aktørId ;
    protected String behandlingstemaOffisiellKode ;
    protected List<String> adopsjonsBarnFodselsdatoer;
    protected String barnTermindato;
    protected String barnFodselsdato;
    protected String omsorgsovertakelsedato;
    protected String årsakInnsendingInntektsmelding;
    protected String saksnummer;
    protected String annenPart;
}
